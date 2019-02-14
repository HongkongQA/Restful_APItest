package socket;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args){

        try {
            Socket socket = new Socket("localhost", 8889);

            OutputStream outputStream = socket.getOutputStream();

            PrintWriter printWriter = new PrintWriter(outputStream);

            printWriter.write("user: admin;password: 123");

            printWriter.flush();

            socket.shutdownOutput();

            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String info = null;
            while ((info=bufferedReader.readLine())!=null){
                System.out.println("Server--->>>"+info);
            }

            bufferedReader.close();
            inputStream.close();
            printWriter.close();
            outputStream.close();
            socket.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
