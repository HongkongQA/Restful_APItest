package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

    Socket socket = null;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    public void run() {

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        OutputStream outputStream = null;
        PrintWriter printWriter = null;

        try {

            inputStream = socket.getInputStream();

            inputStreamReader = new InputStreamReader(inputStream);

            bufferedReader = new BufferedReader(inputStreamReader);

            String info = null;
            while ((info = bufferedReader.readLine()) != null) {
                System.out.println("Client--->>>" + info);
            }

            socket.shutdownInput();


            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream);
            printWriter.write("welcome !");
            printWriter.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (printWriter != null)
                    printWriter.close();
                if (outputStream != null)
                    outputStream.close();
                if (bufferedReader != null)
                    bufferedReader.close();
                if (inputStream != null) ;
                inputStream.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (socket != null) ;
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
