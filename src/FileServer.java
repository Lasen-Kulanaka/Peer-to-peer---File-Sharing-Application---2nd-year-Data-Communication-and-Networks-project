import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;

public class FileServer {
    private static final int PORT = 5001;
    private static final String UPLOAD_DIR = "server_files/";
    private ServerSocket serverSocket;

    public FileServer() {
        // Create upload directory if it doesn't exist
        new File(UPLOAD_DIR).mkdirs();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Handle each client in a new thread
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataInputStream dis;
        private DataOutputStream dos;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String command = dis.readUTF();

                    switch (command) {
                        case "UPLOAD":
                            receiveFile();
                            break;
                        case "DOWNLOAD":
                            sendFile();
                            break;
                        case "LIST":
                            listFiles();
                            break;
                        case "EXIT":
                            clientSocket.close();
                            return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            }
        }

        private void receiveFile() throws IOException {
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            FileOutputStream fos = new FileOutputStream(UPLOAD_DIR + fileName);
            byte[] buffer = new byte[4096];
            long remaining = fileSize;
            int read;

            while (remaining > 0 && (read = dis.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
                fos.write(buffer, 0, read);
                remaining -= read;
            }

            fos.close();
            System.out.println("File received: " + fileName);
            dos.writeUTF("File uploaded successfully");
        }

        private void sendFile() throws IOException {
            String fileName = dis.readUTF();
            File file = new File(UPLOAD_DIR + fileName);

            if (!file.exists()) {
                dos.writeBoolean(false);
                return;
            }

            dos.writeBoolean(true);
            dos.writeLong(file.length());

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int read;

            while ((read = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, read);
            }

            fis.close();
            System.out.println("File sent: " + fileName);
        }

        private void listFiles() throws IOException {
            File directory = new File(UPLOAD_DIR);
            String[] files = directory.list();

            if (files == null) {
                dos.writeInt(0);
                return;
            }

            dos.writeInt(files.length);
            for (String file : files) {
                dos.writeUTF(file);
            }
        }
    }

    public static void main(String[] args) {
        FileServer server = new FileServer();
        server.start();
    }
}