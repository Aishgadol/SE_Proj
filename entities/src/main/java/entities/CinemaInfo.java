package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CinemaInfo implements Serializable {

    int numHalls;
    String name;
    List<MovieInfo> movieInfoList;
    List<UserInfo> customerInfoList;
    List<TicketInfo> ticketInfoList;
    List<DisplayTimeInfo> displayTimeInfoList;

    public CinemaInfo(String name, int numHalls){
        this.name=name;
        this.numHalls=numHalls;
        this.movieInfoList=new ArrayList<>();
        this.customerInfoList=new ArrayList<>();
        this.ticketInfoList=new ArrayList<>();
        this.displayTimeInfoList=new ArrayList<>();
    }
    public CinemaInfo(String name,int numHalls,List<MovieInfo> milist,List<UserInfo> uilist,List<TicketInfo> tilist){
        this.name=name;
        this.numHalls=numHalls;
        this.movieInfoList=milist;
        this.customerInfoList=uilist;
        this.ticketInfoList=tilist;
    }


    public CinemaInfo(CinemaInfo c){
        this.numHalls=c.getNumHalls();
        this.name=c.getName();
        this.movieInfoList=c.getMovieInfoList();
        this.customerInfoList=c.getCustomerInfoList();
        this.ticketInfoList=c.getTicketInfoList();
        this.displayTimeInfoList=c.getDisplayTimeInfoList();
    }


    public String getName(){
        return this.name;
    }
    public void setName(String name){this.name=name;}
    public void setNumHalls(int numHalls){this.numHalls=numHalls;}
    public int getNumHalls(){return this.numHalls;}
    public List<MovieInfo> getMovieInfoList(){return this.movieInfoList;}
    public void setMovieInfoList(List<MovieInfo> list){this.movieInfoList=list;}
    public void setCustomerInfoList(List<UserInfo> list){this.customerInfoList=list;}
    public List<UserInfo> getCustomerInfoList(){return this.customerInfoList;}
    public List<TicketInfo> getTicketInfoList(){return this.ticketInfoList;}
    public void setTicketInfoList(List<TicketInfo> list){this.ticketInfoList=list;}
    public void addTicketInfo(TicketInfo ticketInfo){
        for(TicketInfo t:this.ticketInfoList){
            if(t.toString().equals(ticketInfo.toString())){
                return;
            }
        }
        this.ticketInfoList.add(ticketInfo);
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
    public void addMovieInfo(MovieInfo movieInfo) {
    for (MovieInfo m : this.movieInfoList) {
        if (m.getName().equals(movieInfo.getName())) {
            return;
        }
    }
    this.movieInfoList.add(movieInfo);
}
    public void removeMovieInfo(MovieInfo movieInfo) {
        boolean found = false;
        for (MovieInfo mi : this.movieInfoList) {
            if (mi.getName().equals(movieInfo.getName())) {
                found = true;
                break;
            }
        }
        if (found) {
            Iterator<MovieInfo> iterator = this.movieInfoList.iterator();
            while (iterator.hasNext()) {
                MovieInfo obj = iterator.next();
                if (obj.getName().equals(movieInfo.getName())) {
                    iterator.remove();
                }
            }
        }
    }
    public void addUserInfo(UserInfo userInfo) {
    for (UserInfo u : this.customerInfoList) {
        if (u.getName().equals(userInfo.getName())) {
            return;
        }
    }
    this.customerInfoList.add(userInfo);
}
    public void removeUserInfo(UserInfo userInfo) {
    boolean found = false;
    for (UserInfo ui : this.customerInfoList) {
        if (ui.getName().equals(userInfo.getName())) {
            found = true;
            break;
        }
    }
    if (found) {
        Iterator<UserInfo> iterator = this.customerInfoList.iterator();
        while (iterator.hasNext()) {
            UserInfo obj = iterator.next();
            if (obj.getName().equals(userInfo.getName())) {
                iterator.remove();
            }
        }
    }
}
    public List<DisplayTimeInfo> getDisplayTimeInfoList() {
        return displayTimeInfoList;
    }
    public void setDisplayTimeInfoList(List<DisplayTimeInfo> displayTimeInfoList) {
        this.displayTimeInfoList = displayTimeInfoList;
    }

}
