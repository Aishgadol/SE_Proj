package entities;

import java.io.Serializable;
import java.util.List;

public class HallInfo implements Serializable {
    private CinemaInfo cinemaInfo;
    private int hallNum;
    private List<SeatInfo> seatInfos;
    private int numRows;
    private int numCols;
    private List<MovieInfo> movieInfos;
    private List<String> displayTimes;
    private List<TicketInfo> ticketInfos;

     // Normal constructor
    public HallInfo(CinemaInfo cinemaInfo, List<SeatInfo> seatInfos, int numRows, int numCols,
                    List<MovieInfo> movieInfos, List<String> displayTimes, List<TicketInfo> ticketInfos,int hallNum) {
        this.cinemaInfo = cinemaInfo; this.seatInfos = seatInfos; this.numRows = numRows; this.numCols = numCols;
        this.movieInfos = movieInfos; this.displayTimes = displayTimes; this.ticketInfos = ticketInfos; this.hallNum=hallNum;
    }

    // Copy constructor
    public HallInfo(HallInfo other) {
        this.cinemaInfo = other.getCinemaInfo();
        this.seatInfos = other.getSeatInfoList();
        this.numRows = other.getNumRows();
        this.numCols = other.getNumCols();
        this.movieInfos = other.getMovieInfoList();
        this.displayTimes = other.getDisplayTimeList();
        this.ticketInfos = other.getTicketInfoList();
        this.hallNum=other.getHallNum();
    }

    // Getters and Setters
    public int getHallNum(){return this.hallNum;}
    public void setHallNum(int hallNum){this.hallNum=hallNum;}
    public CinemaInfo getCinemaInfo() { return cinemaInfo; }
    public void setCinemaInfo(CinemaInfo cinemaInfo) { this.cinemaInfo = cinemaInfo; }

    public List<SeatInfo> getSeatInfoList() { return seatInfos; }
    public void setSeatInfos(List<SeatInfo> seatInfos) { this.seatInfos = seatInfos; }

    public int getNumRows() { return numRows; }
    public void setNumRows(int numRows) { this.numRows = numRows; }

    public int getNumCols() { return numCols; }
    public void setNumCols(int numCols) { this.numCols = numCols; }

    public List<MovieInfo> getMovieInfoList() { return movieInfos; }
    public void setMovieInfoList(List<MovieInfo> movieInfos) { this.movieInfos = movieInfos; }

    public List<String> getDisplayTimeList() { return displayTimes; }
    public void setDisplayTimeList(List<String> displayTimes) { this.displayTimes = displayTimes; }

    public List<TicketInfo> getTicketInfoList() { return ticketInfos; }
    public void setTicketInfoList(List<TicketInfo> ticketInfos) { this.ticketInfos = ticketInfos; }

}
