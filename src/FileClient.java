import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5001;
    private static final String DOWNLOAD_DIR = "client_files/";
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Scanner scanner;

    public FileClient() {
        // Create download directory if it doesn't exist
        new File(DOWNLOAD_DIR).mkdirs();
        scanner = new Scanner(System.in);
    }

    public void connect() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected to server.");

            showMenu();
        } catch (IOException e) {
            System.out.println("Could not connect to server.");
            e.printStackTrace();
        }
    }

    private void showMenu() {
        while (true) {
            try {
                System.out.println("\n=== P2P File Sharing Client ===");
                System.out.println("1. Upload file");
                System.out.println("2. Download file");
                System.out.println("3. List available files");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        uploadFile();
                        break;
                    case 2:
                        downloadFile();
                        break;
                    case 3:
                        listFiles();
                        break;
                    case 4:
                        dos.writeUTF("EXIT");
                        socket.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (IOException e) {
                System.out.println("Connection lost to server.");
                break;
            }
        }
    }

    private void uploadFile() throws IOException {
        System.out.print("Enter file path to upload: ");
        String filePath = scanner.nextLine();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File does not exist!");
            return;
        }

        dos.writeUTF("UPLOAD");
        dos.writeUTF(file.getName());
        dos.writeLong(file.length());

        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int read;

        while ((read = fis.read(buffer)) > 0) {
            dos.write(buffer, 0, read);
        }

        fis.close();
        String response = dis.readUTF();
        System.out.println(response);
    }

    private void downloadFile() throws IOException {
        dos.writeUTF("LIST");
        int fileCount = dis.readInt();

        if (fileCount == 0) {
            System.out.println("No files available on server.");
            return;
        }

        System.out.println("\nAvailable files:");
        for (int i = 0; i < fileCount; i++) {
            System.out.println((i + 1) + ". " + dis.readUTF());
        }

        System.out.print("Enter file name to download: ");
        String fileName = scanner.nextLine();

        dos.writeUTF("DOWNLOAD");
        dos.writeUTF(fileName);

        if (!dis.readBoolean()) {
            System.out.println("File not found on server!");
            return;
        }

        long fileSize = dis.readLong();
        FileOutputStream fos = new FileOutputStream(DOWNLOAD_DIR + fileName);
        byte[] buffer = new byte[4096];
        long remaining = fileSize;
        int read;

        while (remaining > 0 && (read = dis.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
            fos.write(buffer, 0, read);
            remaining -= read;
        }

        fos.close();
        System.out.println("File downloaded successfully to " + DOWNLOAD_DIR + fileName);
    }

    private void listFiles() throws IOException {
        dos.writeUTF("LIST");
        int fileCount = dis.readInt();

        if (fileCount == 0) {
            System.out.println("No files available on server.");
            return;
        }

        System.out.println("\nAvailable files:");
        for (int i = 0; i < fileCount; i++) {
            System.out.println((i + 1) + ". " + dis.readUTF());
        }
    }

    public static void main(String[] args) {
        FileClient client = new FileClient();
        client.connect();
    }
}
