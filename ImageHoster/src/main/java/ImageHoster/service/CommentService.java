package ImageHoster.service;

import ImageHoster.model.Comment;
import ImageHoster.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    //The method calls the updateImageComment() method in the CommentRepository and passes the comment to be persisted in the database
    public Comment uploadComment(Comment comment){
        return commentRepository.updateImageComment(comment);
    }

}
