// ChatView.java
package Client.Graphics;

import Client.SingleChatThread;
import Client.SocketClientThread;
import javax.swing.*;


public class SingleChatView {
    private final String myName;
    private final String hisName;
    public JTextField text;
    public JTextArea textArea;
    SingleChatThread.SingleChatViewListen listenerSingleChat;

    // 构造函数
    public SingleChatView(String userName, String hisName) {
        this.myName = userName;
        this.hisName = hisName;
        init();
    }

    public String getHisName() {
        return hisName;
    }

    public String getMyName() {
        return myName;
    }

    // 初始化函数
    void init() {
        JFrame jf = new JFrame("私聊窗口");
        jf.setBounds(250,100,400,330);  //设置坐标和大小
        jf.setResizable(false);  // 缩放为不能缩放

        JPanel jp = new JPanel();
        JLabel labelTheName = new JLabel("from: " + myName+"   "+"to: "+hisName);
        labelTheName.setHorizontalAlignment(SwingConstants.CENTER);
        textArea = new JTextArea(12, 35);
        textArea.setEditable(false);  // 设置为不可修改
        JScrollPane scroll = new JScrollPane(textArea);  // 设置滚动面板（装入textArea）
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // 显示垂直条
        jp.add(labelTheName);
        jp.add(scroll);
        text = new JTextField(20);
        JButton button = new JButton("发送");
        jp.add(text);
        jp.add(button);

        // 设置“发送”监听
        listenerSingleChat = new SingleChatThread.SingleChatViewListen();
        listenerSingleChat.setJTextField(text);  // 调用PoliceListen类的方法
        listenerSingleChat.setJTextArea(textArea);
        listenerSingleChat.setChatViewJf(jf);
        listenerSingleChat.setNowHisName(hisName);

        text.addActionListener(listenerSingleChat);  // 文本框添加监听
        button.addActionListener(listenerSingleChat);  // 按钮添加监听

        jf.add(jp);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置右上角关闭图标的作用
        jf.setVisible(true);  // 设置可见
    }
    // “打开文件”调用函数
}
