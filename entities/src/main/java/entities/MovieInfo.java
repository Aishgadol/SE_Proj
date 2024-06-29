package entities;

import java.io.Serializable;

public class MovieInfo implements Serializable {


    private String name;
    private String releaseDate;


    public MovieInfo(String name, String releaseDate){
        this.name=name;
        this.releaseDate=releaseDate;

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
}
