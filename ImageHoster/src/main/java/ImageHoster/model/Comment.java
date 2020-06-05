package ImageHoster.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "user")
    private User user;

    @Column(name = "image")
    private Image image;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "imageId")
    private Integer imageId;

    public Comment() {
    }

    public Comment(int id, String text, Date createdDate, User user, Image image) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.user = user;
        this.image = image;
    }


    public Integer getId(){ return id;}

    public void setId(Integer id){this.id = id;}

    public String getComment(){ return text;}

    public void setComment(String text){ this.text = text;}

    public Date getDate(){ return createdDate; }

    public void setDate(Date createdDate){ this.createdDate = createdDate; }

    public User getUser() { return user;}

    public void setUser(User user) { this.user = user;}

}
