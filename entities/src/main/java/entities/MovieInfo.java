package entities;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class MovieInfo implements Serializable {


     String name;
     String releaseDate;
     byte[] imageData;
     List<String> displayTimes=new ArrayList<>();
     String genre;
     String producer;
     String summary;
     String actors;
     String status;

    public MovieInfo(String name, String releaseDate,String genre, String producer, String actors, String summary,String status){
        this.name=name;
        this.releaseDate=releaseDate;
        this.genre=genre;
        this.summary=summary;
        this.actors=actors;
        this.producer=producer;
        this.status=status;
    }

    public MovieInfo(MovieInfo movieInfo){
        this.name=movieInfo.getName();
        this.releaseDate=movieInfo.getReleaseDate();
        this.genre=movieInfo.getGenre();
        this.summary=movieInfo.getSummary();
        this.actors=movieInfo.getActors();
        this.producer=movieInfo.getProducer();
        this.status=movieInfo.getStatus();
        this.displayTimes=movieInfo.getDisplayTimes();
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
        this.displayTimes.remove(displayTime);
    }

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
        for (String dt : this.displayTimes) {
            sb.append("  - ").append(dt).append("\n");
        }

        return sb.toString();
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }


}
