// ChatView.java
package Client.Graphics;

import Client.SingleChatThread;
import Client.SocketClientThread;
import Client.ClientFileThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ConversationGraphic {
    private final String userName;  //由客户端登录时设置
//    private String password;
    JTextField text;
    JTextArea textArea;
    JTextArea onlineArea;
    SocketClientThread.ChatViewListen listener;
    SingleChatThread.chooseSingleChat chooseSingleChatListener;
    JTextField textSingleChat;

    // 构造函数
    public ConversationGraphic(String userName) {
        this.userName = userName;
//        this.password = password;
        init();
    }
    // 初始化函数
    void init() {
        JFrame jf = new JFrame("客户端");
        jf.setBounds(500,200,800,330);  //设置坐标和大小
        jf.setResizable(false);  // 缩放为不能缩放

        JPanel jp = new JPanel();
        JLabel label = new JLabel("用户：" + userName);
        textArea = new JTextArea("***************登录成功，欢迎来到多人聊天室！****************\n",12, 35);
        textArea.setEditable(false);  // 设置为不可修改
        onlineArea = new JTextArea("当前用户\n", 10, 30);
        onlineArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);  // 设置滚动面板（装入textArea）
        JScrollPane onlineScroll = new JScrollPane(onlineArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // 显示垂直条
        onlineScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jp.add(label);
        jp.add(scroll);
        jp.add(onlineScroll);
        text = new JTextField(20);
        JButton button = new JButton("发送");
        JButton openFileBtn = new JButton("发送文件");
        jp.add(text);
        jp.add(button);
        jp.add(openFileBtn);
        textSingleChat = new JTextField(20);
        JButton buttonSingleChat = new JButton("私聊");
        jp.add(textSingleChat);
        jp.add(buttonSingleChat);
        // 设置“打开文件”监听
        openFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFileOpenDialog(jf);
            }
        });

        // 设置“发送”监听
        listener = new SocketClientThread.ChatViewListen();
        listener.setJTextField(text);  // 调用PoliceListen类的方法
        listener.setJTextArea(textArea);
        listener.setOnlineJTextArea(onlineArea);
        listener.setChatViewJf(jf);
        text.addActionListener(listener);  // 文本框添加监听
        button.addActionListener(listener);  // 按钮添加监听

        chooseSingleChatListener = new SingleChatThread.chooseSingleChat();
        chooseSingleChatListener.setJFrame(jf);
        chooseSingleChatListener.setJTextField(textSingleChat);
        chooseSingleChatListener.setUserName(this.userName);
        textSingleChat.addActionListener(chooseSingleChatListener);
        buttonSingleChat.addActionListener(chooseSingleChatListener);

        jf.add(jp);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置右上角关闭图标的作用
        jf.setVisible(true);  // 设置可见
    }
    // “打开文件”调用函数
    void showFileOpenDialog(JFrame parent) {
        // 创建一个默认的文件选择器
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹
        fileChooser.setCurrentDirectory(new File("D:/OneDrive - hnu.edu.cn/mylearn/vscode/Chat_Socket_SQL/UserFiles"));
        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名）
        // 设置默认使用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
        fileChooser.setFileFilter(new FileNameExtensionFilter("(txt)", "txt"));
        // 打开文件选择框（线程将被堵塞，知道选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);  // 对话框将会尽量显示在靠近 parent 的中心
        // 点击确定
        if(result == JFileChooser.APPROVE_OPTION) {
            // 获取路径
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            ClientFileThread.outFileToServer(path);
        }
    }
}
