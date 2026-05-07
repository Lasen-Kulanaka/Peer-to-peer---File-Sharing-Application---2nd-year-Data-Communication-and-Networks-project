# Peer-To-Peer File Sharing Application

## Overview
This is a Java-based Peer-to-Peer (P2P) file-sharing application developed for the **CSCI 21023** (Data Communication and Networks) course.  
The system utilizes **socket programming** to allow multiple clients to connect to a remote multi-threaded server for:

- Uploading files
- Downloading files
- Browsing available files
- Handling concurrent client connections

---

# System Architecture

## FileServer Component
The `FileServer` is a multi-threaded server application that listens for incoming client connections on **port 5001**.

### Responsibilities
- Accept incoming client connections
- Create a dedicated thread for each client
- Handle secure and independent file transfers
- Store uploaded files in the server directory

### Key Features
- Multi-threaded architecture
- Continuous client listening
- Centralized file storage
- Concurrent request handling

---

## FileClient Component
The `FileClient` is a command-line interface (CLI) application that connects to the server.

### Features
- Interactive menu-driven interface
- Upload functionality
- Download functionality
- File browsing support
- Local file storage management

### Client Operations
Users can:
- Upload files to the server
- Download files from the server
- View available files
- Exit safely from the application

---

# Key Features & Technical Solutions

## Concurrent Client Handling
To ensure one user's activity does not interrupt another user's file transfer, the server uses **multi-threading**.

### Implementation Details
- A separate handler thread is created for each client
- Multiple users can connect simultaneously
- Independent file transfer sessions are maintained

### Benefits
- Improved responsiveness
- Better scalability
- Stable concurrent communication

---

## Optimized Data Transmission
Large file transfers may cause:
- Memory overflow
- Slow transmission
- Connection timeouts

### Solution
The application uses **buffered streams** with fixed-size chunks.

### Technical Details
- Data is transferred using **4096-byte buffers**
- Files are read and written incrementally
- Memory usage remains optimized

### Advantages
- Faster transfer speeds
- Reduced memory consumption
- Stable large-file handling

---

## Cross-System Compatibility
The application is designed to work reliably across different operating systems.

### Techniques Used
- Relative directory paths
- File existence validation
- Platform-independent file handling

### Benefits
- Reduced path-related errors
- Better portability
- Improved reliability

---

# Setup and Execution Instructions

## Prerequisites
Before running the application, ensure the following are installed:

- Java Development Kit (JDK)
- Terminal or Command Prompt

---

# Compilation Steps

Compile both Java source files using the following commands:

```bash
javac FileServer.java
javac FileClient.java
```

---

# Running the Server

Start the server application first:

```bash
java FileServer
```

## Server Behavior
The server will:
- Start listening on port **5001**
- Automatically create storage directories
- Wait for incoming client connections

## Important Notes
If the client and server are running on different machines:

- Ensure firewall access is enabled
- Allow traffic through port **5001**

---

# Running the Client

Open a separate terminal window and execute:

```bash
java FileClient
```

## Client Behavior
The client will:
- Connect to the server
- Display an interactive menu
- Allow file operations

---

# Usage Guide

The client application provides four main operational options.

---

## 1. Upload File

### Steps
- Select option `1`
- Enter the exact local file path

### Result
- The file will be uploaded to the server directory

---

## 2. Download File

### Steps
- Select option `2`
- Enter the exact filename available on the server

### Result
- The file will be downloaded
- Saved to the local client directory

---

## 3. List Available Files

### Steps
- Select option `3`

### Result
- Displays all files currently stored on the server

---

## 4. Exit Application

### Steps
- Select option `4`

### Result
- Safely disconnects from the server
- Terminates the client application

---

# Technologies Used

- Java
- Socket Programming
- Multi-threading
- Buffered Streams
- File I/O Operations
- Client-Server Architecture

---

# Project Highlights

- Multi-client support
- Efficient file transfer mechanism
- Stable server-client communication
- Platform-independent design
- Optimized memory usage
- Interactive command-line interface

---

# Contributors

| Name | Registration Number |
|------|---------------------|
| Deshan W.C. | CS/2021/026 |
| Kulanaka W.A.L. | CS/2021/013 |

---
