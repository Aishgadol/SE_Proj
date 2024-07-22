package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CinemaInfo implements Serializable {

    int numHalls;
    String name;
    List<HallInfo> hallInfoList;
    List<MovieInfo> movieInfoList;
    List<UserInfo> customerInfoList;
    List<TicketInfo> ticketInfoList;

    public CinemaInfo(String name, int numHalls){
        this.name=name;
        this.numHalls=numHalls;
        this.hallInfoList=new ArrayList<>();
        this.movieInfoList=new ArrayList<>();
        this.customerInfoList=new ArrayList<>();
        this.ticketInfoList=new ArrayList<>();
    }

    public CinemaInfo(CinemaInfo c){
        this.numHalls=c.getNumHalls();
        this.name=c.getName();
        this.hallInfoList=c.getHallInfoList();
        this.movieInfoList=c.getMovieInfoList();
    }


    public String getName(){
        return this.name;
    }
    public void setName(String name){this.name=name;}
    public void setNumHalls(int numHalls){this.numHalls=numHalls;}
    public int getNumHalls(){return this.numHalls;}
    public List<HallInfo> getHallInfoList(){return this.hallInfoList;}
    public void setHallInfoList(List<HallInfo> list){this.hallInfoList=list;}
    public List<MovieInfo> getMovieInfoList(){return this.movieInfoList;}
    public void setMovieInfoList(List<MovieInfo> list){this.movieInfoList=list;}
    public void setCustomerInfoList(List<UserInfo> list){this.customerInfoList=list;}
    public List<UserInfo> getCustomerInfoList(){return this.customerInfoList;}
    public List<TicketInfo> getTicketInfoList(){return this.ticketInfoList;}
    public void setTicketInfoList(List<TicketInfo> list){this.ticketInfoList=list;}

}
