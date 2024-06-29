package server;

import javax.persistence.*;
import entities.MovieInfo;


@Entity
@Table(name="Movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Movie(){
        /*dont use this*/
    }

    public Movie(String name, String picture_link ){
        //use this, update paramteres
    }

}
