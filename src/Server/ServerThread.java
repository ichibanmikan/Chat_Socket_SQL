package Server;

import Bean.User;
import Bean.UserSocket;
import Dao.userDao;
import Util.DButil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ServerThread extends Thread{
    Socket nowSocket = null;
    BufferedReader in =null;
    PrintWriter out = null;
    User nowUser;
    DButil dbu=new DButil();
    userDao ud=new userDao();
    public ServerThread(Socket s) {
        this.nowSocket = s;
    }

    public void run() {
        while(true){
            try {
                out = new PrintWriter(nowSocket.getOutputStream());
                BufferedReader loginBuff = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));
                String username=loginBuff.readLine();
                String password=loginBuff.readLine();
                User anUser=new User(username, password);
                if(!ud.isUserExists(dbu, anUser.getUsername())){
                    ud.addElement(anUser, dbu);
                }
                if(ud.logIn(dbu, anUser)&&SocketServer.addUserOnline(anUser.getUsername())){
                    out.println("true");
                    List<User> arrayUser=ud.queAllUser(dbu);
                    out.println(Integer.toString(arrayUser.size()));
                    for (User user : arrayUser) {
                        out.println(user.getUsername());
                    }
                    out.flush();
                    nowUser=anUser;
                    UserSocket us=new UserSocket(nowUser.getUsername(), nowSocket);
                    SocketServer.allUserSocket.add(us);
//                    System.out.println(159753);
                    break;
                } else {
                    out.println("false");
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));
            while (true) {
//                System.out.println(333);
                String str = in.readLine();
                for(Socket socket: SocketServer.list) {
                    out = new PrintWriter(socket.getOutputStream());
                    Date nowDate=new Date();
                    if(socket == nowSocket) {
                        String spaces="          ";
                        out.println(spaces+spaces+spaces+spaces+spaces+nowDate);
                        out.println(spaces+spaces+spaces+spaces+spaces+str);
                    } else {
                        out.println(nowDate);
                        out.println(str);
                    }
                    out.println();
                    out.flush();
                }
            }
        } catch (Exception e) {
            if(nowUser!=null){
                SocketServer.rmvNowUser(nowUser.getUsername());
            }
            SocketServer.list.remove(nowSocket);
        }
    }
}