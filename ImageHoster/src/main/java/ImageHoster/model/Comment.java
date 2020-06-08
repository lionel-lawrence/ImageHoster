package ImageHoster.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "date")
    private Date createdDate;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "images_id")
    private Image image;

    public Comment() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getId() { return id; }

    public Image getImage(){ return image;}

    public void setImage(Image image) { this.image = image;}

    public void setComments(String text) { this.text = text; }

    public String getComments(){ return text;}

    public String getText() { return text; }

    public void setText(String text) { this.text = text;}

    public Date getDate(){ return createdDate; }

    public void setDate(Date createdDate){ this.createdDate = createdDate; }

    public User getUser() { return user;}

    public void setUser(User user) { this.user = user;}

}
