package Bean;

import java.net.Socket;

public class UserSocket {
    private User thisUser;
    private Socket thisSocket;
    public UserSocket(User anUser, Socket thisSocket){
        this.thisSocket=thisSocket;
        this.thisUser=anUser;
    }

    public Socket getThisSocket() {
        return thisSocket;
    }

    public User getThisUser() {
        return thisUser;
    }

    @Override
    public String toString() {
        return "UserSocket{" +
                "thisUser=" + thisUser.toString() +
                ", thisSocket=" + thisSocket.toString() +
                '}';
    }

    public void setThisSocket(Socket thisSocket) {
        this.thisSocket = thisSocket;
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }
}
