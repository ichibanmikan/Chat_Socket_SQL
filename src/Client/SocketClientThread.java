package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Client.Graphics.*;

public class SocketClientThread extends Thread{
    static Socket mySocket = null;  // 一定要加上static，否则新建线程时会清空
    static JTextField textInput;
    static JTextArea textShow;
    static JTextArea onlineUser;
    static JFrame chatViewJFrame;
    static BufferedReader in = null;
    static PrintWriter out = null;
    static String userName;
    static String userPwd;
    static String[] allUser;
    static int numAllUser;
    static List<SingleChatView> scwList;


    // 用于接收从服务端发送来的消息
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));  // 输入流
            onlineUser.append(userName+'\n');
            for(int i=0; i<numAllUser; i++){
                if(!allUser[i].equals(userName)){
                    onlineUser.append(allUser[i]+'\n');
                }
            }
            while (true) {
                String str = in.readLine();  // 获取服务端发送的信息
                if(str.equals("Open the door!!!")){
                    String souName=in.readLine();
                    SingleChatView singleChatView = new SingleChatView(userName, souName);  // 新建聊天窗口,设置聊天窗口的用户名（静态）
                    SingleChatThread readAndPrint = new SingleChatThread();
                    readAndPrint.start();
                    continue;
                }
                textShow.append(str + '\n');  // 添加进聊天客户端的文本区域
                textShow.setCaretPosition(textShow.getDocument().getLength());  // 设置滚动条在最下面
            }
        } catch (Exception ignored) {}
    }

    /**********************登录监听(内部类)**********************/
    public static class LoginListen implements ActionListener {
        JTextField textField;
        JFrame loginJFrame;  // 登录窗口本身
        JPasswordField pwdField;
        ConversationGraphic chatView = null;

        public void setJTextField(JTextField textField) {
            this.textField = textField;
        }
        public void setJFrame(JFrame jFrame) {
            this.loginJFrame = jFrame;
        }
        public void setJPasswordField(JPasswordField pwdField) {
            this.pwdField = pwdField;
        }

        public void actionPerformed(ActionEvent event) {
            userName = textField.getText();
            userPwd = String.valueOf(pwdField.getPassword());  // getPassword方法获得char数组

            if(userPwd.length()>=5) {
                // 建立和服务器的联系
                try {
                    InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
                    mySocket = new Socket(addr,8081);  // 客户端套接字
                    out = new PrintWriter(mySocket.getOutputStream());  // 输出流
                    out.println(userName);
                    out.println(userPwd);// 发送用户名给服务器
                    out.flush();  // 清空缓冲区out中的数据
                    BufferedReader loginBuff = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                    String isTrue=loginBuff.readLine();
                    if(Objects.equals(isTrue, "false")){
                        JOptionPane.showMessageDialog(loginJFrame, "账号或密码错误，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // 新建普通读写线程并启动
                        String lengthStr=loginBuff.readLine();
                        numAllUser=Integer.parseInt(lengthStr);
                        allUser=new String[1005];
                        for(int i=0; i<numAllUser; i++){
                            allUser[i]=loginBuff.readLine();
                        }
                        loginJFrame.setVisible(false);
                        chatView = new ConversationGraphic(userName);  // 新建聊天窗口,设置聊天窗口的用户名（静态）
                        SocketClientThread readAndPrint = new SocketClientThread();
                        readAndPrint.start();
                        // 新建文件读写线程并启动
                        ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
                        fileThread.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Too short the password is.");
            }
        }
    }

    /**********************聊天界面监听(内部类)**********************/
    public static class ChatViewListen implements ActionListener{
        public void setJTextField(JTextField text) {
            textInput = text;  // 放在外部类，因为其它地方也要用到
        }
        public void setJTextArea(JTextArea textArea) {
            textShow = textArea;  // 放在外部类，因为其它地方也要用到
        }
        public void setOnlineJTextArea(JTextArea textArea) {
            onlineUser = textArea;  // 放在外部类，因为其它地方也要用到
        }
        public void setChatViewJf(JFrame jFrame) {
            chatViewJFrame = jFrame;  // 放在外部类，因为其它地方也要用到
            // 设置关闭聊天界面的监听
            chatViewJFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    out.println("用户 " + userName + " 离开聊天室！");
                    out.flush();
                    System.exit(0);
                }
            });
        }
        // 监听执行函数
        public void actionPerformed(ActionEvent event) {
            try {
                String str = textInput.getText();
                // 文本框内容为空
                if("".equals(str)) {
                    textInput.grabFocus();  // 设置焦点（可行）
                    // 弹出消息对话框（警告消息）
                    JOptionPane.showMessageDialog(chatViewJFrame, "输入为空，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                out.println(userName + ": " + str);  // 输出给服务端
                out.flush();  // 清空缓冲区out中的数据

                textInput.setText("");  // 清空文本框
                textInput.grabFocus();  // 设置焦点（可行）
            } catch (Exception ignored) {}
        }
    }
}
