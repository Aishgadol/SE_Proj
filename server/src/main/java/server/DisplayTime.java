package server;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Entity
@Table(name="DisplayTimes")
public class DisplayTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="Display_Time_And_Date", unique=true,nullable = false)
    String displayTime; //in the format of HH:MM, DD/MM/YYYY

    @ManyToMany(mappedBy = "displayTimes")
    private List<Movie> movies=new ArrayList<>();

     @OneToMany(mappedBy = "displayTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketList;


    public DisplayTime(){
    }

    public DisplayTime(String displayTime){
        this.displayTime=displayTime;
    }

    /*public DisplayTime(DisplayTime displayTime){
        this.displayTime=displayTime.getDisplayTime();
        this.movies=displayTime.getMovies();
    }*/

    public String getDisplayTime(){
        return this.displayTime;
    }

    public void setDisplayTime(String displayTime){
        this.displayTime=displayTime;
    }
    public List<Movie> getMovies(){return this.movies;}
    public void setMovies(List<Movie> movies){this.movies=movies;}

    public void addMovie(Movie movie){
        for(Movie m: this.movies){
            if(m.getName().equals(movie.getName())){
                return;
            }
        }
        this.movies.add(movie);
        movie.addDisplayTime(this);
    }

    public void removeMovie(Movie movie) {
        boolean found=false;
        for(Movie m:this.movies){
            if(m.getName().equals(movie.getName())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<Movie> iterator = this.movies.iterator();
            while (iterator.hasNext()) {
                Movie obj = iterator.next();
                if (obj.getName().equals(movie.getName())) {
                    iterator.remove();
                }
            }
        }
	}

    public void setTicketList(List<Ticket> ticketList){this.ticketList=ticketList;}
    public List<Ticket> getTicketList(){return this.ticketList;}

    public void addTicket(Ticket ticket){
        for(Ticket t: this.ticketList){
            if(ticket.toString().equals(t.toString())){
                return;
            }
        }
        this.ticketList.add(ticket);
        ticket.setDisplayTime(this);
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
}
