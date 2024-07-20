package entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
public class Message implements Serializable {
    int id;
    LocalDateTime timeStamp;
    String message;
    String data;
    MovieInfo movieInfo;
    List<MovieInfo> movieInfoList;
    byte[] imageData;
    UserInfo userInfo;
    List<UserInfo> userInfoList;
    TicketInfo ticketInfo;
    List<TicketInfo> ticketInfoList;


    public Message(int id, LocalDateTime timeStamp, String message) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public Message(int id, String message) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.data = null;
    }

    public Message(int id, String message,String data) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MovieInfo getMovieInfo() {
        return movieInfo;
    }
    public void setMovieInfo(MovieInfo m){
        this.movieInfo=m;
    }

    public List<MovieInfo> getMovieInfoList(){
        return this.movieInfoList;
    }
    public void setMovieInfoList(List<MovieInfo> movieInfoList){
        this.movieInfoList = movieInfoList;
    }

    public byte[] getImageData(){
        return this.imageData;
    }
    public void setImageData(byte[] imageData){this.imageData=imageData;}

    public UserInfo getUserInfo(){return this.userInfo;}
    public void setUserInfo(UserInfo u){this.userInfo=u;}

    public List<UserInfo> getUserInfoList(){return this.userInfoList;}
    public void setUserInfoList(List<UserInfo> list){this.userInfoList=list;}

    public TicketInfo getTicketInfo(){return this.ticketInfo;}
    public void setTicketInfo(TicketInfo t){this.ticketInfo=t;}

    public List<TicketInfo> getTicketInfoList(){return this.ticketInfoList;}
    public void setTicketInfoList(List<TicketInfo> list){this.ticketInfoList=list;}

}
