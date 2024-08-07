package entities;

import java.io.Serializable;

public class TicketInfo implements Serializable {
    private UserInfo userInfo;
    private MovieInfo movieInfo;
    private DisplayTimeInfo displayTimeInfo;
    private CinemaInfo cinemaInfo;
    private int seatRow;
    private int seatCol;
    private int hallNum;
    private String purchaseTime;
    private int active;

    public TicketInfo(UserInfo userInfo, MovieInfo movieInfo, DisplayTimeInfo displayTimeInfo, CinemaInfo cinemaInfo,int hallNum,int seatRow, int seatCol,String purchaseTime) {
        this.userInfo = userInfo;
        this.movieInfo = movieInfo;
        this.displayTimeInfo = displayTimeInfo;
        this.cinemaInfo = cinemaInfo;
        this.seatRow=seatRow;
        this.seatCol=seatCol;
        this.purchaseTime=purchaseTime;
        this.hallNum = hallNum;
        this.active=1;
    }

    public TicketInfo(TicketInfo other) {
        this.userInfo = other.getUserInfo();
        this.movieInfo = other.getMovieInfo();
        this.displayTimeInfo = other.getDisplayTimeInfo();
        this.cinemaInfo = other.getCinemaInfo();
        this.seatRow=other.getSeatRow();
        this.seatCol=other.getSeatCol();
        this.purchaseTime=other.getPurchaseTime();
        this.hallNum = other.getHallNum();
        this.active=other.getActive();
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
    public DisplayTimeInfo getDisplayTimeInfo() {
        return displayTimeInfo;
    }

    public void setDisplayTimeInfo(DisplayTimeInfo displayTimeInfo) {
        this.displayTimeInfo = displayTimeInfo;
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

    public int getActive() {return active;}
    public void setActive(int active) {this.active = active;}

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
