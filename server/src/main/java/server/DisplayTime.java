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

    @Column(name="Time_Date_Movie_Cinema", unique=true,nullable = false)
    String displayTime; //in the format of HH:MM, DD/MM/YYYY, Movie: <movieName>, Cinema: <cinemaName>

    @ManyToOne
    @JoinColumn(name="movie_name",referencedColumnName = "name")
    private Movie movie;

    @OneToMany(mappedBy = "displayTime")
    private List<Ticket> ticketList=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="Cinema_name",referencedColumnName = "name")
    private Cinema cinema;


    public DisplayTime(){
    }

    public DisplayTime(String displayTime){
        this.displayTime=displayTime;
    }
    public DisplayTime(String displayTime,Movie movie,Cinema cinema,List<Ticket> list){
        this.displayTime=displayTime+", Movie: "+movie.getName()+", Cinema: "+cinema.getName();
        this.movie=movie;
        this.cinema=cinema;
        this.ticketList=list;
    }
    public DisplayTime(String displayTime,Movie movie,Cinema cinema){
        this.displayTime=displayTime+", Movie: "+movie.getName()+", Cinema: "+cinema.getName();
        this.movie=movie;
        this.cinema=cinema;
    }
    public DisplayTime(DisplayTime displayTime){
        this.displayTime=displayTime.getDisplayTime();
        this.movie=displayTime.getMovie();
        this.cinema=displayTime.getCinema();
        this.ticketList=displayTime.getTicketList();
    }


    public  Cinema getCinema(){return this.cinema;}
    public void setCinema(Cinema list){this.cinema=list;}
    public String getDisplayTime(){
        return this.displayTime;
    }
    public void setDisplayTime(String displayTime){
        this.displayTime=displayTime;
    }
    public Movie getMovie(){return this.movie;}
    public void setMovie(Movie movies){this.movie=movies;}

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
