package server;
import javax.persistence.*;

@Entity
@Table(name="Messages")
public class Msg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Item_Text")
    private String text;

    public Msg(){
    }

    public Msg(String text){
        this.text=text;
    }

    public void setText(String t){
        this.text=t;
    }

    public String getText(){
        return this.text;
    }

}
