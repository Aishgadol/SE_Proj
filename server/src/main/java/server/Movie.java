package server;

import javax.persistence.*;

import entities.MovieInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Entity
@Table(name="Movies")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="name", unique=true,nullable = false)
    String name;

    @Column
    String releaseDate;

    @Column
    String genre;

    @Column(name="Availability_Status")
    String status;

    @Column
    String producer;

    @Column
    String summary;

    @Column
    String actors;

    @Lob
    byte[] imageData;

    @ManyToMany
    @JoinTable(
        name = "movie_display_times",
        joinColumns = @JoinColumn(name = "Movie_name", referencedColumnName = "name"),
        inverseJoinColumns = @JoinColumn(name = "Display_Time_And_Date", referencedColumnName = "Display_Time_And_Date")
    )
    private List<DisplayTime> displayTimes=new ArrayList<>();

    @ManyToMany(mappedBy = "movieList")
    private List<Cinema> cinemaList=new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<Ticket> ticketList=new ArrayList<>();



    public Movie(){
        /*dont use this*/
        // comment nosaf mitahat
    }

    public Movie(MovieInfo movieInfo){
        //use this, update paramteres
        this.name=movieInfo.getName();
        this.releaseDate=movieInfo.getReleaseDate();
        this.genre=movieInfo.getGenre();
        this.summary=movieInfo.getSummary();
        this.actors=movieInfo.getActors();
        this.producer=movieInfo.getProducer();
        this.status=movieInfo.getStatus();
        /*if(movieInfo.getDisplayTimes().size()!=0){
            for(String d : movieInfo.getDisplayTimes()){
                this.displayTimes.add(new DisplayTime(d));
            }
        }*/ //might delete later
    }

    public Movie(String name, String releaseDate,String genre, String producer, String actors, String summary,String status){
        this.name=name;
        this.releaseDate=releaseDate;
        this.genre=genre;
        this.summary=summary;
        this.actors=actors;
        this.producer=producer;
        this.status=status;
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate=releaseDate;
    }
    public String getReleaseDate(){
        return this.releaseDate;
    }

    public void setDisplayTimes(List<DisplayTime> displayTimes){this.displayTimes=displayTimes;}
    public List<DisplayTime> getDisplayTimes(){return this.displayTimes;}

    public void setImageData(byte[] data){this.imageData=data;}
    public byte[] getImageData(){return this.imageData;}

    public void setGenre(String genre){this.genre=genre;}
    public String getGenre(){return this.genre;}

    public void setActors(String actors){this.actors=actors;}
    public String getActors(){return this.actors;}

    public void setProducer(String producer){this.producer=producer;}
    public String getProducer(){return this.producer;}

    public void setSummary(String summary){this.summary=summary;}
    public String getSummary(){return this.summary;}

    public void setStatus(String status){
        if(status.equals("Available") || status.equals("Upcoming")){
            this.status=status;
        }
    }
    public String getStatus(){return this.status;}

    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Movie Information:\n");
        sb.append("Name: ").append(this.name).append("\n");
        sb.append("Release Date: ").append(this.releaseDate).append("\n");
        sb.append("Genre: ").append(this.genre).append("\n");
        sb.append("Producer: ").append(this.producer).append("\n");
        sb.append("Summary: ").append(this.summary).append("\n");
        sb.append("Actors: ").append(this.actors).append("\n");

        sb.append("Display Times:\n");
        for (DisplayTime dt : this.displayTimes) {
            sb.append("  - ").append(dt.getDisplayTime()).append("\n");
        }

        return sb.toString();
    }

    public void addDisplayTime(DisplayTime displayTime){
        for(DisplayTime d : this.displayTimes){
            if (d.getDisplayTime().equals(displayTime.getDisplayTime())) {
                return;
            }
        }
        this.displayTimes.add(displayTime);
    }


    public void removeDisplayTime(DisplayTime d) {
        boolean found=false;
        for(DisplayTime d1:this.displayTimes){
            if(d1.getDisplayTime().equals(d.getDisplayTime())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<DisplayTime> iterator = this.displayTimes.iterator();
            while (iterator.hasNext()) {
                DisplayTime obj = iterator.next();
                if (obj.getDisplayTime().equals(d.getDisplayTime())) {
                    iterator.remove();
                }
            }
        }
    }
    public void setCinemaList(List<Cinema> l){this.cinemaList=l;}
    public List<Cinema> getCinemaList(){return this.cinemaList;}

    public void addCinema(Cinema cinema){
        for(Cinema c:this.cinemaList){
            if (c.getName().equals(cinema.getName())){
                return;
            }
        }
        this.cinemaList.add(cinema);
    }


    public void removeCinema(Cinema cinema){
        boolean found=false;
        for(Cinema c:this.cinemaList){
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

    public void setTicketList(List<Ticket> list){this.ticketList=list;}
    public List<Ticket> getTicketList(){return this.ticketList;}

    public void addTicket(Ticket ticket){
        for(Ticket t:this.ticketList){
            if(t.toString().equals(ticket.toString())){
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
    }


}
