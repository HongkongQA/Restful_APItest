package socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(8889);
            Socket socket=null;
            int Count = 0;
            System.out.println("Server start.....");

            while (true){
                socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();

                Count++;
                System.out.println("Client : "+Count);
                InetAddress address = socket.getInetAddress();
                System.out.println("IP : "+address.getHostAddress());
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
