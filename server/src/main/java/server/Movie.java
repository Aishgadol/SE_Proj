package server;

import javax.persistence.*;

import entities.MovieInfo;

import java.io.Serializable;
import java.util.ArrayList;
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

    @ManyToMany
    @JoinTable(
        name = "movie_display_times",
        joinColumns = @JoinColumn(name = "Movie_name", referencedColumnName = "name"),
        inverseJoinColumns = @JoinColumn(name = "Display_Time_And_Date", referencedColumnName = "Display_Time_And_Date")
    )
    private List<DisplayTime> displayTimes=new ArrayList<>();

    @Transient
    private MovieInfo movieInfo;


    public Movie(){
        /*dont use this*/
    }

    public Movie(MovieInfo movieInfo){
        //use this, update paramteres
        this.movieInfo=movieInfo;
        this.name=movieInfo.getName();
        this.releaseDate=movieInfo.getReleaseDate();
    }

    public Movie(String name, String releaseDate){
        this.name=name;
        this.releaseDate=releaseDate;
        movieInfo=new MovieInfo(name,releaseDate);
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate= this.releaseDate;
    }
    public String getReleasedate(){
        return this.releaseDate;
    }

    public void setMovieInfo(MovieInfo movieInfo){this.movieInfo=movieInfo;}
    public MovieInfo getMovieInfo(){return this.movieInfo;}

    public void setDisplayTimes(List<DisplayTime> displayTimes){this.displayTimes=displayTimes;}
    public List<DisplayTime> getDisplayTimes(){return this.displayTimes;}


    public void addDisplayTime(DisplayTime displayTime){
        for(DisplayTime d : this.displayTimes){
            if (d.getDisplayTime().equals(displayTime.getDisplayTime())) {
                return;
            }
        }
        this.displayTimes.add(displayTime);
        this.movieInfo.addDisplayTime(displayTime.getDisplayTime());
    }
    public void removeDisplayTime(DisplayTime displayTime){
        boolean found=false;
        for(DisplayTime d : this.displayTimes){
            if (d.getDisplayTime().equals(displayTime.getDisplayTime())) {
                found=true;
                break;
            }
        }
        if(found) {
            this.displayTimes.remove(displayTime);
            List<String> stfu=this.movieInfo.getDisplayTimes();
            stfu.remove(displayTime.getDisplayTime());
            this.movieInfo.setDisplayTimes(stfu);
        }
    }
}
