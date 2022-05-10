package Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Bean.*;
import Util.*;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.Result;

public class userDao {
    public userDao(){}
    public int addElement(User anUser, DButil dbu){
        String sql = "insert into users(username, passwords) values(?, ?)";
        Object[] obj = {anUser.getUsername(), anUser.getPassword()};
        return dbu.executeUpdate(sql, obj);
    }//增

    public int delUser(String username, DButil dbu) throws Exception {
        String sql = "delete from users where username = ?";
        return dbu.executeUpdate(sql, username);
    }//删

    public int updatePassword(User anUser, DButil dbu){
        String sql = "update users set passwords=? where username=?";
        return dbu.executeUpdate(sql, anUser.getPassword(), anUser.getUsername());
    }//改

    public Boolean logIn(DButil dbu, User anUser) throws SQLException {
        String sql="select * from users where username='"+anUser.getUsername()+"' and passwords='"+anUser.getPassword()+"'";
        Statement stmt=null;
        stmt = dbu.getConnection().createStatement();
        ResultSet rs=null;
        rs = stmt.executeQuery(sql);
        if(rs.next()){
            String uname= rs.getString(1);
            return uname!=null;
        }
        return false;
    }

    public Boolean isUserExists(DButil dbu, String userName) throws SQLException {
        String sql="select * from users where username='"+userName+"'";
        Statement stmt=null;
        stmt = dbu.getConnection().createStatement();
        ResultSet rs=null;
        rs = stmt.executeQuery(sql);
        if(rs.next()){
            String uname= rs.getString(1);
            return uname!=null;
        }
        return false;
    }

    public List<User> queAllUser(DButil dbu) throws Exception {
        String sql = "select * from users";
        List<Map<String,Object>> list = dbu.query(sql);
        List<User> UserList = new ArrayList<>();
        User anUser = null;
        for(Map<String,Object> map:list){
            anUser = new User((String)map.get("username"), (String)map.get("passwords"));
            UserList.add(anUser);
        }
        return UserList;
    }//查
}
