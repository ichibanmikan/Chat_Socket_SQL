// Server.java
package Server;

import Bean.User;

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketServer{
    public static List<String> ListOnlineUser=new ArrayList<String>(); //这个list存储所有在线用户的用户名
    static ServerSocket server = null; //表示本机的服务器
    static Socket socket = null; //线程
    static List<Socket> list = new ArrayList<Socket>();  // 存储客户端

    public static Boolean addUserOnline(String userName){
        if(isTheUserOnline(userName)){
            return false;
        }
        System.out.println(ListOnlineUser.size());
        ListOnlineUser.add(userName);
        System.out.println(ListOnlineUser.size());
        return true;
    } //如果已经在线就不能登录 不在线就可以登录

    public static Boolean isTheUserOnline(String userName){
        return ListOnlineUser.contains(userName);
    } //当前用户是否在线

    public static void rmvNowUser(String userName){
        ListOnlineUser.remove(userName);
    }//当客户端下线了，就把它对应的用户名从上线用户表里删除

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
                ServerThread readAndPrint = new ServerThread(socket); //循环等待客户端，给每一个客户端分配一个线程
                readAndPrint.start(); //Thread类的特点 start就是表示线程开始，调用run()函数
            }
        } catch (IOException e1) {
            e1.printStackTrace();  // 出现异常则打印出异常的位置
        }
    }
}