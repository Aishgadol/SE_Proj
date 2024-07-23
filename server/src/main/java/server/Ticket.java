package server;



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
    Movie movie;

    @Column(name="hall_num")
    private int hallNum;

    @Column
    private int seatCol;
    @Column
    private int seatRow;

    @Column(name="is_ticket_active")
    private int active;

    @ManyToOne
    @JoinColumn(name="cinema",referencedColumnName = "name")
    Cinema cinema;

    @ManyToOne
    @JoinColumn(name="customer_id",referencedColumnName = "User_ID")
    Customer customer;

    @Column(name="Purchase_time")
    private String purchaseTime;

    @ManyToOne
    @JoinColumn(name = "display_time",referencedColumnName ="Display_Time_And_Date" )
    DisplayTime displayTime;


    public Ticket(){}

    public Ticket(Ticket ticket){
        this.movie=ticket.getMovie();
        this.seatCol=ticket.getSeatCol();
        this.hallNum=ticket.getHallNum();
        this.seatRow=ticket.getSeatRow();
        this.cinema=ticket.getCinema();
        this.customer=ticket.getCustomer();
        this.displayTime=ticket.getDisplayTime();
        this.purchaseTime=this.displayTime.getDisplayTime();
        this.active=ticket.getActive();
    }

    public Ticket(Movie movie,int seatCol, int seatRow,int hallNum, Cinema cinema, Customer customer,DisplayTime displayTime){
        this.movie=movie;
        this.seatCol=seatCol;
        this.seatRow=seatRow;
        this.hallNum=hallNum;
        this.cinema=cinema;
        this.purchaseTime=displayTime.getDisplayTime();
        this.displayTime=displayTime;
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

    public int getSeatCol() {
        return seatCol;
    }

    public void setSeatCol(int seatCol) {
        this.seatCol = seatCol;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
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
                "Seat located at: ("+seatCol+","+seatRow+")\n"+
                "Cinema: "+cinema.getName()+"\n"+
                "Owner of this ticket: "+customer.getName()+"\n"+
                "Is the ticket active: "+active+".";
    }

}
