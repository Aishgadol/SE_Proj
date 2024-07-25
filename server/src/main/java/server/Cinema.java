package server;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name="Cinemas")
public class Cinema implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name", unique=true,nullable = false)
    private String name;

    @Column(name="num_of_halls")
    private int numHalls;

    @ManyToMany
    @JoinTable(
        name = "cinema_displaying_movies",
        joinColumns = @JoinColumn(name = "cinema_name", referencedColumnName = "name"),
        inverseJoinColumns = @JoinColumn(name = "Movie_name", referencedColumnName = "name")
    )
    private List<Movie> movieList=new ArrayList<>();

    @OneToMany(mappedBy = "cinema")
    private List<DisplayTime> displayTimeList=new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name="customer_Cinema",
            joinColumns = @JoinColumn(name="cinema_name",referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name="customer_id", referencedColumnName = "user_id")
    )
    private List<Customer> customerList=new ArrayList<>();

    @OneToMany(mappedBy = "cinema")
    private List<Ticket> ticketList=new ArrayList<>();

    // Empty constructor
    public Cinema() {}

    // Copy constructor
    public Cinema(Cinema cinema) {
        this.name = cinema.getName();
        this.numHalls=cinema.getNumHalls();
        this.movieList = cinema.getMovieList();
        this.ticketList=cinema.getTicketList();
        this.customerList=cinema.getCustomerList();
        this.displayTimeList=cinema.getDisplayTimeList();
    }

    // Normal constructor
    public Cinema(String name, int numHalls, List<Movie> movieList,List<Customer> customerList,List<DisplayTime> displayTimeList) {
        this.name = name;
        this.numHalls=numHalls;
        this.movieList = movieList;
        this.customerList=customerList;
        this.displayTimeList=displayTimeList;
    }
    /*
    // Normal constructor
    public Cinema(String name, int numHalls, List<Movie> movieList,List<Ticket> ticketList,List<Customer> customerList) {
        this.name = name;
        this.numHalls=numHalls;
        this.movieList = movieList;
        //this.ticketList=ticketList;
        this.customerList=customerList;
    }*/

    public Cinema(String name, int numHalls){
        this.name=name;
        this.numHalls=numHalls;
    }

     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumHalls(int numHalls){this.numHalls=numHalls;}
    public int getNumHalls(){return this.numHalls;}
    public void setDisplayTimeList(List<DisplayTime> l){this.displayTimeList=l;}
    public List<DisplayTime> getDisplayTimeList(){return this.displayTimeList;}
    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void addDisplayTime(DisplayTime d){
        for(DisplayTime dis:this.displayTimeList){
            if(dis.getDisplayTime().equals(d.getDisplayTime())){
                return;
            }
        }
        this.displayTimeList.add(d);
    }
    public void removeDisplayTime(DisplayTime d){
        boolean found=false;
        for(DisplayTime dis:this.displayTimeList){
            if(d.getDisplayTime().equals(dis.getDisplayTime())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<DisplayTime> iterator = this.displayTimeList.iterator();
            while (iterator.hasNext()) {
                DisplayTime obj = iterator.next();
                if (obj.getDisplayTime().equals(d.getDisplayTime())) {
                    iterator.remove();
                }
            }
        }
    }

    public void addMovie(Movie m){
        for(Movie movie:this.movieList){
            if(movie.getName().equals(m.getName())){
                return;
            }
        }
        this.movieList.add(m);
    }

    public void removeMovie(Movie movie) {
        boolean found=false;
        for(Movie m:this.movieList){
            if(m.getName().equals(movie.getName())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<Movie> iterator = this.movieList.iterator();
            while (iterator.hasNext()) {
                Movie obj = iterator.next();
                if (obj.getName().equals(movie.getName())) {
                    iterator.remove();
                }
            }
        }
	}

    public void setTicketList(List<Ticket> l){this.ticketList=l;}
    public List<Ticket> getTicketList(){return this.ticketList;}

    public void addTicket(Ticket t){
        for(Ticket ticket:this.ticketList){
            if (ticket.toString().equals(t.toString())) {
                return;
            }
        }
        this.ticketList.add(t);
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
    }

    public List<Customer> getCustomerList(){return this.customerList;}
    public void setCustomerList(List<Customer> list){this.customerList=list;}

    public void addCustomer(Customer customer){
        for(Customer c:this.customerList){
            if(c.getName().equals(customer.getName())){
                return;
            }
        }
        this.customerList.add(customer);
        return;
    }
    public void removeCustomer(Customer customer){
        boolean found=false;
        for(Customer c : this.customerList){
            if(customer.getName().equals(c.getName())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<Customer> iterator = this.customerList.iterator();
            while (iterator.hasNext()) {
                Customer obj = iterator.next();
                if (obj.toString().equals(customer.toString())) {
                    iterator.remove();
                }
            }
        }
    }

}
