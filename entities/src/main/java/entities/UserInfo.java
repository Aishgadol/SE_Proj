package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserInfo implements Serializable {
    String id;
    String role;
    String name;
    String password;
    int connected;
    boolean hasTicketPass;
    List<TicketInfo> ticketInfoList=new ArrayList<>();
    List<CinemaInfo> cinemaInfoList=new ArrayList<>();

    

    public UserInfo(){}
    public UserInfo(String id, String role,String name,int connected){
        this.id=id;
        this.role=role;
        this.name=name;
        this.password="";
        this.hasTicketPass=false;
        this.connected=connected;
    }

    public UserInfo(String id, String role,String name,String password){
        this.id=id;
        this.role=role;
        this.name=name;
        this.hasTicketPass=false;
        this.password=password;
        this.connected=0;
    }
    public UserInfo(String id, String role,String name){
        this.id=id;
        this.role=role;
        this.name=name;
        this.password="";
        this.connected=0;
        this.hasTicketPass=false;
    }

    public UserInfo(String id, String name){
        this.id=id;
        this.name=name;
        this.role="Customer";
        this.password="";
        this.connected=0;
        this.hasTicketPass=false;
    }

    public UserInfo(UserInfo u){
        this.id=u.getId();
        this.role=u.getRole();
        this.hasTicketPass=u.hasTicketPass();
        this.name=u.getName();
        this.password=u.getPassword();
        this.connected=u.getConnected();
        this.ticketInfoList=u.getTicketInfoList();
    }

    public void setId(String id){
        this.id=id;
    }
    public String getId(){
        return this.id;
    }

    public void setRole(String role){
        this.role=role;
    }
    public String getRole(){
        return this.role;
    }
    public void setCinemaInfoList(List<CinemaInfo> list){this.cinemaInfoList=list;}
    public List<CinemaInfo> getCinemaInfoList(){return this.cinemaInfoList;}
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public String getPassword(){return this.password;}
    public void setPassword(String p){this.password=p;}
    public boolean hasTicketPass(){return this.hasTicketPass;}
    public void setHasTicketPass(boolean b){this.hasTicketPass=b;}
    public int getConnected(){return this.connected;}
    public void setConnected(int c){this.connected=c;}
    
    public void setTicketInfoList(List<TicketInfo> l){this.ticketInfoList=l;}
    public List<TicketInfo> getTicketInfoList(){return this.ticketInfoList;}

    public void addTicketInfo(TicketInfo ticket){
        for(TicketInfo t : this.ticketInfoList){
            if (t.toString().equals(ticket.toString())) {
                return;
            }
        }
        this.ticketInfoList.add(ticket);
    }
    public void removeTicketInfo(TicketInfo ticketInfo){
        boolean found=false;
        for(TicketInfo t:this.ticketInfoList){
            if(t.toString().equals(ticketInfo.toString())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<TicketInfo> iterator = this.ticketInfoList.iterator();
            while (iterator.hasNext()) {
                TicketInfo obj = iterator.next();
                if (obj.toString().equals(ticketInfo.toString())) {
                    iterator.remove();
                }
            }
        }
    }
    public void addCinemaInfo(CinemaInfo cinemaInfo){
        for(CinemaInfo c : this.cinemaInfoList){
            if (c.getName().equals(cinemaInfo.getName())) {
                return;
            }
        }
        this.cinemaInfoList.add(cinemaInfo);
    }
    public void removeCinemaInfo(CinemaInfo cinemaInfo){
        boolean found=false;
        for(CinemaInfo c : this.cinemaInfoList){
            if(c.getName().equals(cinemaInfo.getName())){
                found=true;
                break;
            }
        }
        if(found) {
            Iterator<CinemaInfo> iterator = this.cinemaInfoList.iterator();
            while (iterator.hasNext()) {
                CinemaInfo obj = iterator.next();
                if (obj.getName().equals(cinemaInfo.getName())) {
                    iterator.remove();
                }
            }
        }
    }

}
