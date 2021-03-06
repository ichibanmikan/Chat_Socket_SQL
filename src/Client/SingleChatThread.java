package Client;

import Client.Graphics.SingleChatView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleChatThread extends Thread{
    static Socket mySocket = null;  // 一定要加上static，否则新建线程时会清空

    static JFrame chatViewJFrame;
    static BufferedReader in = null;
    static PrintWriter out = null;
    static String userName;
    static String[] allUser;
    static int numAllUser;
    static String nowHisName;
    static List<SingleChatView> scwList;
    static JTextField textInputSingleChat;
    static JTextArea textShowSingleChat;
    static JFrame chatViewJFrameSingleChat;

    public void run() {
        try {
            if(mySocket==null){
                InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
                mySocket = new Socket(addr,8095);  // 客户端套接字
                out = new PrintWriter(mySocket.getOutputStream());
            }
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));  // 输入流
            while (true) {
                String sourceName=in.readLine();
                int pos=-1;
                if(scwList==null){
                    scwList=new ArrayList<SingleChatView>();
                    addNewSingleChat(nowHisName);
                    pos=0;
                } else {
                    System.out.println("----");
                    System.out.println(pos);
                    for(int i=0; i<scwList.size(); i++){
                        if(scwList.get(i).getHisName()==nowHisName&&scwList.get(i).getMyName()==userName){
                            pos=i;
                            break;
                        }
                    }
                    if(pos==-1){
                        pos=scwList.size();
                        addNewSingleChat(sourceName);
                    }
                }
                String contents=in.readLine();
                contents+='\n';
                contents+=in.readLine();
                contents+=in.readLine();
                contents+='\n';
                scwList.get(pos).textArea.append(contents + '\n');  // 添加进聊天客户端的文本区域
                scwList.get(pos).textArea.setCaretPosition(scwList.get(pos).textArea.getDocument().getLength());  // 设置滚动条在最下面
//                String str = in.readLine();  // 获取服务端发送的信息
//                textShowSingleChat.append(str + '\n');  // 添加进聊天客户端的文本区域
//                textShowSingleChat.setCaretPosition(textShowSingleChat.getDocument().getLength());  // 设置滚动条在最下面
            }
        } catch (Exception ignored) {}
    }

    private void addNewSingleChat(String sourceName) throws IOException {
        SingleChatView anScw = new SingleChatView(userName, sourceName);
        scwList.add(anScw);
    }

    /***********************私聊选项监听***********************/
    public static class chooseSingleChat implements ActionListener {
        JTextField textField;
        JFrame SingleChatJFrame;  // 登录窗口本身
        SingleChatView singleChatView = null;

        public void setJTextField(JTextField textField) {
            this.textField = textField;
        }
        public void setJFrame(JFrame jFrame) {
            this.SingleChatJFrame = jFrame;
        }
        public void setUserName(String UserName){
            userName=UserName;
        }
        public void actionPerformed(ActionEvent event) {
            try {
                InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
                mySocket = new Socket(addr,8095);  // 客户端套接字
                out = new PrintWriter(mySocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            nowHisName = textField.getText();
            try {
                out.println(userName);
                out.println(nowHisName);
                out.flush();
                BufferedReader SingleChatBuff = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                String isTrue=SingleChatBuff.readLine();
                if(Objects.equals(isTrue, "false")){
                    JOptionPane.showMessageDialog(SingleChatJFrame, "查无此人！", "ん？", JOptionPane.WARNING_MESSAGE);
                } else {
                    singleChatView = new SingleChatView(userName, nowHisName);  // 新建聊天窗口,设置聊天窗口的用户名（静态）
                    scwList=new ArrayList<SingleChatView>();
                    SingleChatThread.scwList.add(singleChatView);
                    SingleChatThread readAndPrint = new SingleChatThread();
                    readAndPrint.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class SingleChatViewListen implements ActionListener{

        public void setJTextField(JTextField text) {
            textInputSingleChat = text;  // 放在外部类，因为其它地方也要用到
        }
        public void setJTextArea(JTextArea textArea) {
            textShowSingleChat = textArea;  // 放在外部类，因为其它地方也要用到
        }
        public void setChatViewJf(JFrame jFrame) {
            chatViewJFrameSingleChat = jFrame;  // 放在外部类，因为其它地方也要用到
        }
        public void setNowHisName(String hisName){
            nowHisName=hisName;
        }
        // 监听执行函数
        public void actionPerformed(ActionEvent event) {
            try {
                String str = textInputSingleChat.getText();
                // 文本框内容为空
                if("".equals(str)) {
                    textInputSingleChat.grabFocus();  // 设置焦点（可行）
                    // 弹出消息对话框（警告消息）
                    JOptionPane.showMessageDialog(chatViewJFrame, "输入为空，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                out.println(userName + ": " + str);  // 输出给服务端
                out.flush();  // 清空缓冲区out中的数据

                textInputSingleChat.setText("");  // 清空文本框
                textInputSingleChat.grabFocus();  // 设置焦点（可行）
            } catch (Exception ignored) {}
        }
    }
}
