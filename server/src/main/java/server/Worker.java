package server;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Table(name="Workers")
public class Worker implements Serializable {
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int index;

    @Column(name="User_ID", unique=true, nullable = false)
    String userID;

    @Column(name="Role")
    String role;

    @Column(name="Full_Name")
    String name;

    public Worker(){
    }

    public Worker(String id, String role,String name){
        this.userID=id;
        this.role=role;
        this.name=name;
    }

    public Worker(Worker w){
        this.userID=w.getId();
        this.role=w.getRole();
        this.name=w.getName();
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
}
