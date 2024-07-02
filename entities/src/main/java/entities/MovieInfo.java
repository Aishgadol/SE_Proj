package entities;

import java.io.Serializable;
import java.util.*;

public class MovieInfo implements Serializable {


     String name;
     String releaseDate;
     List<DisplayTime> displayTimes;

    public MovieInfo(String name, String releaseDate){
        this.name=name;
        this.releaseDate=releaseDate;
    }

    public MovieInfo(MovieInfo m){
        this.name=m.getName();
        this.releaseDate=m.getReleasedate();
        this.displayTimes=m.getDisplayTimes();
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

    public void setDisplayTimes(List<DisplayTime> displayTimes){this.displayTimes=displayTimes;}
    public List<DisplayTime> getDisplayTimes(){return this.displayTimes;}

}
