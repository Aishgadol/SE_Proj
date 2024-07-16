package entities;

import java.io.Serializable;

public class UserInfo implements Serializable {
    String id;
    String role;
    String name;
    String password;
    int connected;

    public UserInfo(){}

    public UserInfo(String id, String role,String name,String password){
        this.id=id;
        this.role=role;
        this.name=name;
        this.password=password;
        this.connected=0;
    }
    public UserInfo(String id, String role,String name){
        this.id=id;
        this.role=role;
        this.name=name;
        this.password="";
        this.connected=0;
    }

    public UserInfo(String id, String name){
        this.id=id;
        this.name=name;
        this.role="Customer";
        this.password="";
        this.connected=0;
    }

    public UserInfo(UserInfo u){
        this.id=u.getId();
        this.role=u.getRole();
        this.name=u.getName();
        this.password=u.getPassword();
        this.connected=u.getConnected();
    }

    public void setId(String id){
        this.id=id;
    }
    public String getId(){
        return this.id;
    }

    public void setRole(String role){
        this.role=role;
    }
    public String getRole(){
        return this.role;
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public String getPassword(){return this.password;}
    public void setPassword(String p){this.password=p;}

    public int getConnected(){return this.connected;}
    public void setConnected(int c){this.connected=c;}

}
