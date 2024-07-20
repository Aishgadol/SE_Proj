package entities;

import java.io.Serializable;

public class TicketInfo implements Serializable {
    private UserInfo userInfo;
    private MovieInfo movieInfo;
    private String displayTime;
    private CinemaInfo cinemaInfo;
    private int row;
    private int col;
    private int hallNum;
    private String purchaseTime;

    public TicketInfo(UserInfo userInfo, MovieInfo movieInfo, String displayTime, CinemaInfo cinemaInfo,int hallNum,int row, int col,String purchaseTime) {
        this.userInfo = userInfo;
        this.movieInfo = movieInfo;
        this.displayTime = displayTime;
        this.cinemaInfo = cinemaInfo;
        this.row=row;
        this.col=col;
        this.purchaseTime=purchaseTime;
        this.hallNum = hallNum;
    }
    public TicketInfo(TicketInfo other) {
        this.userInfo = other.getUserInfo();
        this.movieInfo = other.getMovieInfo();
        this.displayTime = other.getDisplayTime();
        this.cinemaInfo = other.getCinemaInfo();
        this.row=other.getRow();
        this.col=other.getCol();
        this.purchaseTime=other.getPurchaseTime();
        this.hallNum = other.getHallNum();
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

     public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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

    public int getHallNum() {
        return hallNum;
    }

    public void setHallNum(int hallNum) {
        this.hallNum=hallNum;
    }
}
