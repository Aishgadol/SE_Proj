package server;


import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Tickets")
public class Ticket implements Serializable {
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name="movie_name",referencedColumnName = "name")
    private Movie movie;

    @Column(name="hall_num")
    private int hallNum;

    @Column
    private int col;
    @Column
    private int row;

    @Column(name="is_ticket_active")
    private int active;

    @ManyToOne
    @JoinColumn(name="cinema_name",referencedColumnName = "cinema_name")
    private Cinema cinema;

    @ManyToOne
    @JoinColumn(name="customer_name",referencedColumnName = "Full_Name")
    private Customer customer;

    @Column(name="Purchase time")
    private String purchaseTime;

    @ManyToOne
    @JoinColumn(name = "display_time_id")
    private DisplayTime displayTime;


    public Ticket(){}

    public Ticket(Ticket ticket){
        this.movie=ticket.getMovie();
        this.col=ticket.getCol();
        this.row=ticket.getRow();
        this.cinema=ticket.getCinema();
        this.customer=ticket.getCustomer();
        this.active=ticket.getActive();
    }

    public Ticket(Movie movie,int col, int row, Cinema cinema, Customer customer){
        this.movie=movie;
        this.col=col;
        this.row=row;
        this.cinema=cinema;
        this.customer=customer;
        this.active=1;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getHallNum() {
        return hallNum;
    }

    public String getPurchaseTime(){return this.purchaseTime;}
    public void setPurchaseTime(String purchaseTime){this.purchaseTime=purchaseTime;}

    public void setHallNum(int hallNum) {
        this.hallNum = hallNum;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDisplayTime(DisplayTime displayTime){this.displayTime=displayTime;}
    public DisplayTime getDisplayTime(){return this.displayTime;}

    public void setActive(int active){this.active=active;}
    public int getActive(){return this.active;}

    @Override
    public String toString(){
        return "Ticket: \n" +
                "Movie: "+movie.getName()+"\n"+
                "Hall Num: "+hallNum+"\n"+
                "Seat located at: ("+col+","+row+")\n"+
                "Cinema: "+cinema.getName()+"\n"+
                "Owner of this ticket: "+customer.getName()+"\n"+
                "Is the ticket active: "+active+".";
    }

}