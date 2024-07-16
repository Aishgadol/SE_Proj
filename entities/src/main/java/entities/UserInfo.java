package entities;

import java.io.Serializable;

public class UserInfo implements Serializable {
    String id;
    String role;
    String name;

    public UserInfo(String id, String role,String name){
        this.id=id;
        this.role=role;
        this.name=name;
    }

    public UserInfo(UserInfo u){
        this.id=u.getId();
        this.role=u.getRole();
        this.name=u.getName();
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
}
