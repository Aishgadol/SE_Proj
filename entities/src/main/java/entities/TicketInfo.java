package entities;

import java.io.Serializable;

public class TicketInfo implements Serializable {
    private UserInfo userInfo;
    private MovieInfo movieInfo;
    private String displayTime;
    private CinemaInfo cinemaInfo;
    private int seatRow;
    private int seatCol;
    private int hallNum;
    private String purchaseTime;

    public TicketInfo(UserInfo userInfo, MovieInfo movieInfo, String displayTime, CinemaInfo cinemaInfo,int hallNum,int seatRow, int seatCol,String purchaseTime) {
        this.userInfo = userInfo;
        this.movieInfo = movieInfo;
        this.displayTime = displayTime;
        this.cinemaInfo = cinemaInfo;
        this.seatRow=seatRow;
        this.seatCol=seatCol;
        this.purchaseTime=purchaseTime;
        this.hallNum = hallNum;
    }
    public TicketInfo(TicketInfo other) {
        this.userInfo = other.getUserInfo();
        this.movieInfo = other.getMovieInfo();
        this.displayTime = other.getDisplayTime();
        this.cinemaInfo = other.getCinemaInfo();
        this.seatRow=other.getSeatRow();
        this.seatCol=other.getSeatCol();
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

     public int getSeatCol() {
        return seatCol;
    }

    public void setSeatCol(int seatCol) {
        this.seatCol = seatCol;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
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

    @Override
    public String toString(){
        return "Ticket: \n" +
                "Movie: "+movieInfo.getName()+"\n"+
                "Hall Num: "+hallNum+"\n"+
                "Seat located at: ("+seatCol+","+seatRow+")\n"+
                "Cinema: "+cinemaInfo.getName()+"\n"+
                "Owner of this ticket: "+userInfo.getName()+".";
    }
}
