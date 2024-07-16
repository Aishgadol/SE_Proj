package server;

import entities.UserInfo;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="Customers")
public class Customer implements Serializable {
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

    @Column(name="is_user_connected")
    int connected;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    List<Ticket> ticketList;

    public Customer(){}

    public Customer(String Id,String name){
        this.userID=Id;
        this.role="Customer";
        this.name=name;
        this.connected=0;
    }

    public Customer(Customer c){
        this.userID=c.getId();
        this.role="Customer";
        this.name=c.getName();
        this.connected=c.getConnected();
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
    public int getConnected(){
        return this.connected;
    }
    public void setConnected(int c){this.connected=c;}
    public String getPassword(){return null;}
}
