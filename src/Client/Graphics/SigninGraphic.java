// Login.java
package Client.Graphics;

import Client.SocketClientThread;
import Util.DButil;

import java.awt.*;
import javax.swing.*;

public class SigninGraphic {
    JTextField textField = null;
    JPasswordField pwdField = null;
    SocketClientThread.LoginListen listener=null;

    // 构造函数
    public SigninGraphic() {
        init();
    }

    void init() {
        JFrame jf = new JFrame("登录");
        jf.setBounds(500, 250, 310, 210);
        jf.setResizable(false);  // 设置是否缩放

        JPanel jp1 = new JPanel();
        JLabel headJLabel = new JLabel("登录界面");
        JLabel registerJLabel = new JLabel("如果用户不存在将自动注册");
        headJLabel.setFont(new Font(null, 0, 35));  // 设置文本的字体类型、样式 和 大小
        registerJLabel.setFont(new Font(null, 0, 20));
        jp1.add(headJLabel);

        JPanel jp2 = new JPanel();
        JLabel nameJLabel = new JLabel("用户名：");
        textField = new JTextField(20);
        JLabel pwdJLabel = new JLabel("密码：    ");
        pwdField = new JPasswordField(20);
        JButton loginButton = new JButton("登录");
//        JButton registerButton = new JButton("注册");
        jp2.add(nameJLabel);
        jp2.add(textField);
        jp2.add(pwdJLabel);
        jp2.add(pwdField);
        jp2.add(registerJLabel);
        jp2.add(loginButton);
//        jp2.add(registerButton);

        JPanel jp = new JPanel(new BorderLayout());  // BorderLayout布局
        jp.add(jp1, BorderLayout.NORTH);
        jp.add(jp2, BorderLayout.CENTER);

        // 设置监控
        listener = new SocketClientThread.LoginListen();  // 新建监听类
        listener.setJTextField(textField);  // 调用PoliceListen类的方法
        listener.setJPasswordField(pwdField);
        listener.setJFrame(jf);
        pwdField.addActionListener(listener);  // 密码框添加监听
        loginButton.addActionListener(listener);  // 按钮添加监听
//        registerButton.addActionListener(listener);

        jf.add(jp);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置关闭图标作用
        jf.setVisible(true);  // 设置可见
    }
}
