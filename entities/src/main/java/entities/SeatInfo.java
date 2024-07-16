package entities;

import java.io.Serializable;

public class SeatInfo implements Serializable {
    private HallInfo hallInfo;
    private CinemaInfo cinemaInfo;
    private MovieInfo movieInfo;
    private UserInfo userInfo;
    private int row;
    private int col;
    private boolean isTaken=false;

   public SeatInfo(CinemaInfo cinemaInfo, HallInfo hallInfo ,MovieInfo movieInfo, UserInfo userInfo,int col, int row) {
        this.hallInfo = hallInfo;
        this.cinemaInfo = cinemaInfo;
        this.movieInfo = movieInfo;
        this.userInfo = userInfo;
        this.row = row;
        this.col = col;

    }

    // Copy constructor using getter methods
    public SeatInfo(SeatInfo other) {
        this.hallInfo = other.getHallInfo();
        this.cinemaInfo = other.getCinemaInfo();
        this.movieInfo = other.getMovieInfo();
        this.userInfo = other.getUserInfo();
        this.row = other.getRow();
        this.col = other.getCol();
        this.isTaken = other.isTaken();
    }
    // Getter and Setter methods for hallInfo
    public HallInfo getHallInfo() {
        return hallInfo;
    }

    public void setHallInfo(HallInfo hallInfo) {
        this.hallInfo = hallInfo;
    }

    // Getter and Setter methods for cinemaInfo
    public CinemaInfo getCinemaInfo() {
        return cinemaInfo;
    }

    public void setCinemaInfo(CinemaInfo cinemaInfo) {
        this.cinemaInfo = cinemaInfo;
    }

    // Getter and Setter methods for movieInfo
    public MovieInfo getMovieInfo() {
        return movieInfo;
    }

    public void setMovieInfo(MovieInfo movieInfo) {
        this.movieInfo = movieInfo;
    }

    // Getter and Setter methods for userInfo
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    // Getter and Setter methods for row
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    // Getter and Setter methods for col
    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    // Getter and Setter methods for isTaken
    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }
}
