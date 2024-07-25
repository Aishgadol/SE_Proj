package entities;

import java.io.Serializable;
import java.util.*;
import java.util.List;

public class MovieInfo implements Serializable {


     String name;
     String releaseDate;
     byte[] imageData;
     List<DisplayTimeInfo> displayTimeInfoList=new ArrayList<>();
     List<CinemaInfo> cinemaInfoList=new ArrayList<>();
     List<TicketInfo> ticketInfoList=new ArrayList<>();
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
        this.displayTimeInfoList=movieInfo.getDisplayTimeInfoList();
        this.cinemaInfoList=movieInfo.getCinemaInfoList();
        this.ticketInfoList=movieInfo.getTicketInfoList();
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public List<TicketInfo> getTicketInfoList(){return  this.ticketInfoList;}
    public void setTicketInfoList(List<TicketInfo> l){this.ticketInfoList=l;}
    public void setReleaseDate(String releaseDate){
        this.releaseDate= this.releaseDate;
    }
    public String getReleaseDate(){
        return this.releaseDate;
    }
    public void setCinemaInfoList(List<CinemaInfo> names){this.cinemaInfoList=names;}
    public List<CinemaInfo> getCinemaInfoList(){return this.cinemaInfoList;}
    public void setDisplayTimeInfoList(List<DisplayTimeInfo> displayTimeInfoList){this.displayTimeInfoList=displayTimeInfoList;}
    public List<DisplayTimeInfo> getDisplayTimeInfoList(){return this.displayTimeInfoList;}




    public void addTicketInfo(TicketInfo ticket){
        for(TicketInfo t : this.ticketInfoList){
            if (t.toString().equals(ticket.toString())) {
                return;
            }
        }
        this.ticketInfoList.add(ticket);
    }
    public void removeTicketInfo(TicketInfo ticketInfo){
        boolean found=false;
        for(TicketInfo t:this.ticketInfoList){
            if(t.toString().equals(ticketInfo.toString())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<TicketInfo> iterator = this.ticketInfoList.iterator();
            while (iterator.hasNext()) {
                TicketInfo obj = iterator.next();
                if (obj.toString().equals(ticketInfo.toString())) {
                    iterator.remove();
                }
            }
        }
    }
    public void addCinemaInfo(CinemaInfo cinemaInfo){
        for(CinemaInfo c : this.cinemaInfoList){
            if (c.getName().equals(cinemaInfo.getName())) {
                return;
            }
        }
        this.cinemaInfoList.add(cinemaInfo);
    }
    public void removeCinemaInfo(CinemaInfo cinemaInfo){
        boolean found=false;
        for(CinemaInfo c : this.cinemaInfoList){
            if(c.getName().equals(cinemaInfo.getName())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<CinemaInfo> iterator = this.cinemaInfoList.iterator();
            while (iterator.hasNext()) {
                CinemaInfo obj = iterator.next();
                if (obj.getName().equals(cinemaInfo.getName())) {
                    iterator.remove();
                }
            }
        }
    }


    public void addDisplayTimeInfo(DisplayTimeInfo displayTimeInfo){
        for(DisplayTimeInfo d : this.displayTimeInfoList){
            if (d.getDisplayTime().equals(displayTimeInfo.getDisplayTime())) {
                return;
            }
        }
        this.displayTimeInfoList.add(displayTimeInfo);
    }
    public void removeDisplayTimeInfo(DisplayTimeInfo displayTimeInfo){
        boolean found=false;
        for(DisplayTimeInfo di:this.displayTimeInfoList){
            if(di.getDisplayTime().equals(displayTimeInfo.getDisplayTime())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<DisplayTimeInfo> iterator = this.displayTimeInfoList.iterator();
            while (iterator.hasNext()) {
                DisplayTimeInfo obj = iterator.next();
                if (obj.getDisplayTime().equals(displayTimeInfo.getDisplayTime())) {
                    iterator.remove();
                }
            }
        }
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
        sb.append("Name: \n").append(this.name).append("\n\n");
        sb.append("Release Date: \n").append(this.releaseDate).append("\n\n");
        sb.append("Genre: \n").append(this.genre).append("\n\n");
        sb.append("Producer: \n").append(this.producer).append("\n\n");
        sb.append("Actors: \n").append(this.actors).append("\n\n");
        sb.append("Summary: \n").append(this.summary).append("\n\n");

        sb.append("Display Times:\n\n");
        for (DisplayTimeInfo dti : this.displayTimeInfoList) {
            sb.append("  - ").append(dti.getDisplayTime()).append("\n");
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
