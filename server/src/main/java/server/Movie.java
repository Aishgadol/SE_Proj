package server;

import javax.persistence.*;
import entities.MovieInfo;


@Entity
@Table(name="Movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String releaseDate;

    private MovieInfo movieInfo;


    public Movie(){
        /*dont use this*/
    }

    public Movie(MovieInfo movieInfo){
        //use this, update paramteres
        this.movieInfo=movieInfo;

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

}
