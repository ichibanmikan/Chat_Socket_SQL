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
                    UserSocket us=new UserSocket(anUser, nowSocket);
                    SocketServer.allUserSocket.add(us);
                    out.flush();
                    nowUser=anUser;
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
                String headInfo = in.readLine();
                if(headInfo.equals("sendMessage")){
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
                } else if(headInfo.equals("singleChat")){
                    String str = in.readLine();
                    String destinyName = in.readLine();
                    Date nowDate=new Date();
                    for(int i=0; i<SocketServer.allUserSocket.size(); i++) {
                        if(SocketServer.allUserSocket.get(i).getThisSocket() == nowSocket) {
                            String spaces="          ";
                            out.println(spaces+spaces+spaces+spaces+spaces+nowDate);
                            out.println(spaces+spaces+spaces+spaces+spaces+str);
                            out.println();
                            out.flush();
                        } else if(SocketServer.allUserSocket.get(i).getThisUser().getUsername().equals(destinyName)) {
                            out.println(nowDate);
                            out.println(str);
                            out.println();
                            out.flush();
                        }
                    }
                } else if (headInfo.equals("singleChatStart")){
                    String destinyName = in.readLine();
                    System.out.println("   "+destinyName);
                    if(ud.isUserExists(dbu, destinyName)){
                        out.println("true");
                        out.flush();
                    } else {
                        out.println("false");
                        out.flush();
                    }
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