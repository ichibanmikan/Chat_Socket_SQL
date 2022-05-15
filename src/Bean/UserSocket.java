package Bean;

import java.net.Socket;

public class UserSocket {
    private String thisUser;
    private Socket thisSocket;
    public UserSocket(String anUserName, Socket thisSocket){
        this.thisSocket=thisSocket;
        this.thisUser=anUserName;
    }

    public Socket getThisSocket() {
        return thisSocket;
    }

    public String getThisUser() {
        return thisUser;
    }

    @Override
    public String toString() {
        return "UserSocket{" +
                "thisUser=" + thisUser +
                ", thisSocket=" + thisSocket.toString() +
                '}';
    }

    public void setThisSocket(Socket thisSocket) {
        this.thisSocket = thisSocket;
    }

    public void setThisUser(String thisUser) {
        this.thisUser = thisUser;
    }
}
