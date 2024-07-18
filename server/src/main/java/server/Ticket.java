/*package server;


import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Tickets")
public class Ticket implements Serializable {
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="movie_name")
    private String movieName;

    @ManyToOne
    @JoinColumn(name="movie_name",referencedColumnName = "name")
    private Movie movie;

    @Column(name="hall_num")
    private int hallNum;

    @ManyToOne
    @JoinColumn(name="hall_number",referencedColumnName = "hall_num")
    Hall hall;

    @Column(name="cinema_name")
    private String cinemaName;
    @Column
    private int col;
    @Column
    private int row;

    @ManyToOne
    @JoinColumn(name="cinema_name",referencedColumnName = "cinema_name")
    private Cinema cinema;

    @ManyToOne
    @JoinColumn(name="customer_name",referencedColumnName = "Full_Name")
    Customer customer;


    public Ticket(){}

    public Ticket(){

    }

}
*/