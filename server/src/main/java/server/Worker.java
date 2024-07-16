package server;

import entities.UserInfo;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Table(name="Workers")
public class Worker implements Serializable {
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="User_ID", unique=true, nullable = false)
    String userID;

    @Column(name="Role")
    String role;

    @Column(name="Full_Name")
    String name;

    @Column(name="password")
    String password;

    public Worker(){
    }

    public Worker(String id, String role,String name,String password){
        this.userID=id;
        this.role=role;
        this.name=name;
        this.password=password;
    }

    public Worker(Worker w){
        this.userID=w.getId();
        this.role=w.getRole();
        this.name=w.getName();
        this.password=w.getPassword();
    }

    public void setId(String id){
        this.userID=id;
    }
    public String getId(){
        return this.userID;
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
    public void setPassword(String p){
        this.password=p;
    }
    public String getPassword(){
        return this.password;
    }
}
