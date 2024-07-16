package server;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Table(name="Customers")
public class Customer implements Serializable {
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

    public Customer(){
    }
    public Customer(String Id,String name){
        this.userID=Id;
        this.role="Customer";
        this.name=name;
    }
    public Customer(Customer c){
        this.userID=c.getId();
        this.role="Customer";
        this.name=c.getName();
    }

    public void setId(String id){
        this.userID=id;
    }
    public String getId(){
        return this.userID;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }

}
