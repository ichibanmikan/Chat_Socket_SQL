package Server;

import Bean.User;
import Dao.userDao;
import Util.DButil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;


public class ServerThread extends Thread{
    Socket nowSocket = null;
    BufferedReader in =null;
    PrintWriter out = null;
    User nowUser;
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
                userDao ud=new userDao();
                DButil dbu=new DButil();
                User anUser=new User(username, password);
                if(!ud.isUserExists(dbu, anUser.getUsername())){
                    ud.addElement(anUser, dbu);
                }
                if(ud.logIn(dbu, anUser)&&SocketServer.addUserOnline(anUser.getUsername())){
                    out.println("true");
                    out.flush();
                    nowUser=anUser;
                    break;
                } else {
                    out.println("false");
                }
                out.flush();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));
            while (true) {
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