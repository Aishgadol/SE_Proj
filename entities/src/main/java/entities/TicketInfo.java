package entities;

import java.io.Serializable;

public class TicketInfo implements Serializable {
    private UserInfo userInfo;
    private MovieInfo movieInfo;
    private String displayTime;
    private CinemaInfo cinemaInfo;
    private HallInfo hallInfo;
    private String purchaseTime;

    public TicketInfo(UserInfo userInfo, MovieInfo movieInfo, String displayTime, CinemaInfo cinemaInfo, HallInfo hallInfo,String purchaseTime) {
        this.userInfo = userInfo;
        this.movieInfo = movieInfo;
        this.displayTime = displayTime;
        this.cinemaInfo = cinemaInfo;
        this.purchaseTime=purchaseTime;
        this.hallInfo = hallInfo;
    }
    public TicketInfo(TicketInfo other) {
        this.userInfo = other.getUserInfo();
        this.movieInfo = other.getMovieInfo();
        this.displayTime = other.getDisplayTime();
        this.cinemaInfo = other.getCinemaInfo();
        this.purchaseTime=other.getPurchaseTime();
        this.hallInfo = other.getHallInfo();
    }

    public String getPurchaseTime(){return this.purchaseTime;}
    public void setPurchaseTime(String purchaseTime){this.purchaseTime=purchaseTime;}
    // Getters and Setters
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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

    public CinemaInfo getCinemaInfo() {
        return cinemaInfo;
    }

    public void setCinemaInfo(CinemaInfo cinemaInfo) {
        this.cinemaInfo = cinemaInfo;
    }

    public HallInfo getHallInfo() {
        return hallInfo;
    }

    public void setHallInfo(HallInfo hallInfo) {
        this.hallInfo = hallInfo;
    }
}
