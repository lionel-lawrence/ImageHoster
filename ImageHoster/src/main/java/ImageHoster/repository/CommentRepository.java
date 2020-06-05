package ImageHoster.repository;

import ImageHoster.model.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Repository
public class CommentRepository {
    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    public Comment createComment(){
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        // yet to complete

    }
}
