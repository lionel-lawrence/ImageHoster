package ImageHoster.service;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment uploadComment(Comment comment){
//        comment.setDate(new Date());
        return commentRepository.updateImageComment(comment);
    }

}
