package server;

import javax.persistence.*;

@Entity
@Table(name="Movie_Images")
public class MovieImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="Movie_Name", unique=true,nullable = false)
    private String name;

    @Lob
    private byte[] imageData;

    public MovieImage() {
    }

    public MovieImage(String name, byte[] imageData) {
        this.name = name;
        this.imageData = imageData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
