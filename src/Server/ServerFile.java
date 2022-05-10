// ServerFileThread.java
package Server;

import Server.ServerFileThread;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerFile extends Thread{
    ServerSocket server = null;
    Socket socket = null;
    static List<Socket> list = new ArrayList<Socket>();  // 存储客户端

    public void run() {
        try {
            server = new ServerSocket(8090);
            while(true) {
                socket = server.accept();
                list.add(socket);
                // 开启文件传输线程
                ServerFileThread fileReadAndWrite = new ServerFileThread(socket);
                fileReadAndWrite.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
