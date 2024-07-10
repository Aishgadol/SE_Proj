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
    List<MovieInfo> list;
    byte[] imageData;

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

    public List<MovieInfo> getList(){
        return this.list;
    }
    public void setList(List<MovieInfo> list){
        this.list=list;
    }

    public byte[] getImageData(){
        return this.imageData;
    }
    public void setImageData(byte[] imageData){this.imageData=imageData;}
}
