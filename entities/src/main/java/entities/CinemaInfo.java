package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CinemaInfo implements Serializable {

    String location;
    String name;
    List<HallInfo> hallInfoList;
    List<MovieInfo> displayedMovieInfos;

    public CinemaInfo(String name, String location){
        this.name=name;
        this.location=location;
        this.hallInfoList=new ArrayList<>();
        this.displayedMovieInfos=new ArrayList<>()
    }

    public CinemaInfo(CinemaInfo c){
        this.location=c.getLocation();
        this.name=c.getName();
        this.hallInfoList=c.getHallInfos();
        this.displayedMovieInfos=c.getMovieInfos();
    }


    public String getName(){
        return this.name;
    }
    public void setName(String name){this.name=name;}
    public void setLocation(String location){this.location=location;}
    public String getLocation(){return this.location;}
    public List<HallInfo> getHallInfos(){return this.hallInfoList;}
    public void setHallInfos(List<HallInfo> list){this.hallInfoList=list;}
    public List<MovieInfo> getMovieInfos(){return this.displayedMovieInfos;}
    public void setMovieInfos(List<MovieInfo> list){this.displayedMovieInfos=list;}

}
