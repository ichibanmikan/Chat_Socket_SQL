// Server.java
package Server;

import Bean.User;

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketServer{
    public static List<String> ListOnlineUser=new ArrayList<String>();
    static ServerSocket server = null;
    static Socket socket = null;
    static List<Socket> list = new ArrayList<Socket>();  // 存储客户端

    public static Boolean addUserOnline(String userName){
        if(isTheUserOnline(userName)){
            return false;
        }
        System.out.println(ListOnlineUser.size());
        ListOnlineUser.add(userName);
        System.out.println(ListOnlineUser.size());
        return true;
    }

    public static Boolean isTheUserOnline(String userName){
        return ListOnlineUser.contains(userName);
    }

    public static void rmvNowUser(String userName){
        ListOnlineUser.remove(userName);
    }

    public static void main(String[] args) {
        try {
            // 在服务器端对客户端开启文件传输的线程
            ServerFile serverFileThread = new ServerFile();
            serverFileThread.start();
            server = new ServerSocket(8081);  // 服务器端套接字（只能建立一次）
            // 等待连接并开启相应线程
            while (true) {
                socket = server.accept();  // 等待连接
                list.add(socket);  // 添加当前客户端到列表
                // 在服务器端对客户端开启相应的线程
                System.out.println(socket.toString());
                ServerThread readAndPrint = new ServerThread(socket);
                readAndPrint.start();
            }
        } catch (IOException e1) {
            e1.printStackTrace();  // 出现异常则打印出异常的位置
        }
    }
}