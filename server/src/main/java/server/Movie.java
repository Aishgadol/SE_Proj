package server;

import javax.persistence.*;

import entities.MovieInfo;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="Movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column
    String name;

    @Column
    String releaseDate;

    @ManyToMany(mappedBy="movies")
    private List<DisplayTime> displayTimes=new ArrayList<>();

    MovieInfo movieInfo;


    public Movie(){
        /*dont use this*/
    }

    public Movie(MovieInfo movieInfo){
        //use this, update paramteres
        this.movieInfo=movieInfo;
        this.name=movieInfo.getName();
        this.releaseDate=movieInfo.getReleasedate();
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
}
