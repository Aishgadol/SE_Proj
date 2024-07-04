package entities;

import java.io.Serializable;
import java.util.*;

public class MovieInfo implements Serializable {


     String name;
     String releaseDate;
     List<String> displayTimes=new ArrayList<>();

    public MovieInfo(String name, String releaseDate){
        this.name=name;
        this.releaseDate=releaseDate;
    }

    public MovieInfo(MovieInfo m){
        this.name=m.getName();
        this.releaseDate=m.getReleaseDate();
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
    public String getReleaseDate(){
        return this.releaseDate;
    }

    public void setDisplayTimes(List<String> displayTimes){this.displayTimes=displayTimes;}
    public List<String> getDisplayTimes(){return this.displayTimes;}
    public void addDisplayTime(String displayTime){
        for(String d : this.displayTimes){
            if (d.equals(displayTime)) {
                return;
            }
        }
        this.displayTimes.add(displayTime);
    }
    public void removeDisplayTime(String displayTime){
        boolean found=false;
        for(String d: this.displayTimes){
            if(d.equals(displayTime)){
                found=true;
                break;
            }
        }
        if(found){
            this.displayTimes.remove(displayTime);
        }
    }

}
