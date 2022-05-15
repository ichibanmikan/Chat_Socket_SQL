package Server;

import Bean.User;
import Bean.UserSocket;
import Dao.userDao;
import Util.DButil;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class ServerSingleChatThread extends Thread {
    Socket nowSocket = null;
    BufferedReader in =null;
    PrintWriter out = null;
    DButil dbu=new DButil();
    userDao ud=new userDao();
    String username;
    String hisName;

    public ServerSingleChatThread(Socket socket) {
        this.nowSocket = socket;
    }
    public void run() {
        Boolean isContinue=false;
        try {
            out = new PrintWriter(nowSocket.getOutputStream());
            BufferedReader SingleChatStartBuff = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));
            username=SingleChatStartBuff.readLine();
            hisName=SingleChatStartBuff.readLine();
            UserSocket us=new UserSocket(username, nowSocket);
            ServerSingleChat.allUserSocket.add(us);
            if(!ud.isUserExists(dbu, hisName)) {
                out.println("false");
            } else {
                out.println("true");
                isContinue=true;
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isContinue){
            try {
                in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));
                while (true) {
                    String str = in.readLine();
                    Date nowDate = new Date();
                    Boolean isSend=false;
                    String sourceName = "";
                    int posI;
                    for (int j = 0; j < str.length(); j++) {
                        if (str.charAt(j) == ':' && str.charAt(j + 1) == ' ') {
                            posI=j;
                            break;
                        }
                        sourceName += str.charAt(j);
                    }
                    for (int i = 0; i < ServerSingleChat.allUserSocket.size(); i++) {
                        if (ServerSingleChat.allUserSocket.get(i).getThisSocket() == nowSocket) {
                            String spaces = "          ";
                            out.println(spaces + spaces + spaces + spaces + spaces + sourceName);
                            out.println(spaces + spaces + spaces + spaces + spaces + nowDate);
                            out.println(spaces + spaces + spaces + spaces + spaces + str);
                            out.println();
                            out.flush();
                        } else if (ServerSingleChat.allUserSocket.get(i).getThisUser().equals(hisName)) {
                            System.out.println("****+  "+hisName);
                            PrintWriter newOut = new PrintWriter(ServerSingleChat.allUserSocket.get(i).getThisSocket().getOutputStream());
                            newOut.println(sourceName);
                            newOut.println(nowDate);
                            newOut.println(str);
                            newOut.println();
                            newOut.flush();
                            isSend=true;
                        }
                    }
                    System.out.println("++++ "+isSend);
                    if(!isSend){
                        for(int i=0; i<SocketServer.allUserSocket.size(); i++){
                            if(hisName.equals(SocketServer.allUserSocket.get(i).getThisUser())){
                                PrintWriter newOut = new PrintWriter(SocketServer.allUserSocket.get(i).getThisSocket().getOutputStream());
                                newOut.println("Open the door!!!");
                                newOut.println(username);
                                newOut.println(str);
                                newOut.flush();
//                                in.readLine();
                                break;
                            }
                        }
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
                ServerSingleChat.list.remove(nowSocket);
            }
        }
    }
}