// Server.java
package Server;

import Bean.UserSocket;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerSingleChat extends Thread{
    static ServerSocket server = null; //表示本机的服务器
    static Socket socket = null; //线程
    static List<Socket> list = new ArrayList<Socket>();  // 存储客户端
    static List<UserSocket> allUserSocket=new ArrayList<UserSocket>();

    public void run() {
        try {
            server = new ServerSocket(8095);
            while(true) {
                socket = server.accept();
                list.add(socket);
                ServerSingleChatThread SingleChatReadAndWrite = new ServerSingleChatThread(socket);
                SingleChatReadAndWrite.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}