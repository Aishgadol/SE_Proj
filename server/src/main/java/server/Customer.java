package server;

import entities.UserInfo;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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

    @ManyToMany
    @JoinTable(
        name = "customer_cinema",
        joinColumns = @JoinColumn(name = "customer_name"),
        inverseJoinColumns = @JoinColumn(name = "cinema_name")
    )
    List<Cinema> cinemaList=new ArrayList<>();

    //@OneToMany(mappedBy = "customer")
    //List<Ticket> ticketList=new ArrayList<>();

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
        //this.ticketList=c.getTicketList();
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
    public void setRole(String role){this.role=role;}
    public String getRole(){return this.role;}
    public int getConnected(){
        return this.connected;
    }
    public void setConnected(int c){this.connected=c;}
    public String getPassword(){return null;}

   /* public void setTicketList(List<Ticket> list){this.ticketList=list;}
    public List<Ticket> getTicketList(){return this.ticketList;}

    public void addTicket(Ticket ticket){
        for(Ticket t : this.ticketList){
            if (t.toString().equals(ticket.toString())) {
                return;
            }
        }
        this.ticketList.add(ticket);
    }
    public void removeTicket(Ticket ticket){
        boolean found=false;
        for(Ticket t:this.ticketList){
            if(t.toString().equals(ticket.toString())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<Ticket> iterator = this.ticketList.iterator();
            while (iterator.hasNext()) {
                Ticket obj = iterator.next();
                if (obj.toString().equals(ticket.toString())) {
                    iterator.remove();
                }
            }
        }
    }*/
    public void setCinemaList(List<Cinema> list){this.cinemaList=list;}
    public List<Cinema> getCinemaList(){return this.cinemaList;}

    public void addCinema(Cinema cinema){
        for(Cinema c : this.cinemaList){
            if(cinema.getName().equals(c.getName())){
                return;
            }
        }
        this.cinemaList.add(cinema);
    }

    public void removeCinema(Cinema cinema){
        boolean found=false;
        for(Cinema c : this.cinemaList){
            if(c.getName().equals(cinema.getName())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<Cinema> iterator = this.cinemaList.iterator();
            while (iterator.hasNext()) {
                Cinema obj = iterator.next();
                if (obj.getName().equals(cinema.getName())) {
                    iterator.remove();
                }
            }
        }
    }

}
