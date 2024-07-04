package entities;

import java.io.Serializable;
import java.util.*;

public class MovieInfo implements Serializable {


     String name;
     String releaseDate;
     List<String> displayTimes;

    public MovieInfo(String name, String releaseDate){
        this.name=name;
        this.releaseDate=releaseDate;
        this.displayTimes=new ArrayList<>();
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

    public void setDisplayTimes(List<String> displayTimes){this.displayTimes=displayTimes;}
    public List<String> getDisplayTimes(){return this.displayTimes;}
    public void addDisplayTime(String displayTime){
        for(String d : displayTimes){
            if (d.equals(displayTime)) {
                return;
            }
        }
        displayTimes.add(displayTime);
    }
    public void removeDisplayTime(String displayTime){
        for(String d: displayTimes){
            if(d.equals(displayTime)){
                displayTimes.remove(displayTime);
            }
        }
    }

}
