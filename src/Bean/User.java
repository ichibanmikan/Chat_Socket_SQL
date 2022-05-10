package Bean;

public class User {
    private String password;
    private String username;
    public User(String usn, String pwd){
        this.password=pwd;
        this.username=usn;
    }
    public User(){}

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
