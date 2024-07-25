package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DisplayTimeInfo implements Serializable {

    private String displayTime; //in the format of HH:MM, DD/MM/YYYY, Movie: <movieName>, Cinema: <cinemaName>
    private MovieInfo movieInfo;
    private CinemaInfo cinemaInfo;
    private List<TicketInfo> ticketInfoList=new ArrayList<>();

    public DisplayTimeInfo(String displayTime,MovieInfo m,CinemaInfo c){
        this.displayTime=displayTime+", Movie: "+m.getName()+", Cinema: "+c.getName();
        this.movieInfo=m;
        this.cinemaInfo=c;
    }
    public DisplayTimeInfo(String displayTime,MovieInfo m,CinemaInfo c ,List<TicketInfo> list){
        this.displayTime=displayTime+", Movie: "+m.getName()+", Cinema: "+c.getName();
        this.movieInfo=m;
        this.cinemaInfo=c;
        this.ticketInfoList=list;
    }
    public DisplayTimeInfo(DisplayTimeInfo d){
        this.displayTime=d.getDisplayTime();
        this.movieInfo=d.getMovieInfo();
        this.cinemaInfo=d.getCinemaInfo();
        this.ticketInfoList=d.getTicketInfoList();
    }

    public List<TicketInfo> getTicketInfoList() {
        return ticketInfoList;
    }

    public void setTicketInfoList(List<TicketInfo> ticketInfoList) {
        this.ticketInfoList = ticketInfoList;
    }

    public CinemaInfo getCinemaInfo() {
        return cinemaInfo;
    }

    public void setCinemaInfo(CinemaInfo cinemaInfo) {
        this.cinemaInfo = cinemaInfo;
    }

    public MovieInfo getMovieInfo() {
        return movieInfo;
    }

    public void setMovieInfo(MovieInfo movieInfo) {
        this.movieInfo = movieInfo;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }


    public void addTicketInfo(TicketInfo ticketInfo){
        for(TicketInfo t : this.ticketInfoList){
            if (t.toString().equals(ticketInfo.toString())) {
                return; //exists alrdy
            }
        }
        this.ticketInfoList.add(ticketInfo);
    }
    public void removeTicketInfo(TicketInfo ticket){
        boolean found=false;
        for(TicketInfo t:this.ticketInfoList){
            if(t.toString().equals(ticket.toString())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<TicketInfo> iterator = this.ticketInfoList.iterator();
            while (iterator.hasNext()) {
                TicketInfo obj = iterator.next();
                if (obj.toString().equals(ticket.toString())) {
                    iterator.remove();
                }
            }
        }
    }

}
