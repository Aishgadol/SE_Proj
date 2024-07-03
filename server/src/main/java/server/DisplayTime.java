package server;

import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="DisplayTimes")
public class DisplayTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column
    String displayTime; //in the format of HH:MM, DD/MM/YYYY

    @ManyToMany
    @JoinTable(name="movie_display_time",
            joinColumns = @JoinColumn(name="displayTime"),
            inverseJoinColumns = @JoinColumn(name="name"))
    private List<Movie> movies=new ArrayList<>();

    public String getDisplayTime(){
        return this.displayTime;
    }
    public void setDisplayTime(String displayTime){
        this.displayTime=displayTime;
    }
    public List<Movie> getMovies(){return this.movies;}
    public void setMovies(List<Movie> movies){this.movies=movies;}


}
