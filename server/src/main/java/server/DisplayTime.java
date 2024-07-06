package server;

import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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

    public void removeMovie(Movie movie){
        boolean found=false;
        for(Movie m:this.movies){
            if(m.getName().equals(movie.getName())){
                found=true;
                break;
            }
        }
        if(found){
            this.movies.remove(movie);
        }
    }

}
