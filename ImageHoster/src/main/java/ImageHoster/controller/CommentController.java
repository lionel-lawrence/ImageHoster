package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/image/{imageId}/{imageTitle}/comments", method = RequestMethod.POST)
    public String newComment(@PathVariable("imageId") Integer imageId, @PathVariable("imageTitle") String title, @RequestParam(name = "comment") String text, HttpSession session) {
        Image image = imageService.getImage(imageId);
        User user = (User) session.getAttribute("loggeduser");
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setComments(text);
        comment.setDate(new Date());
        comment.setImage(image);
        commentService.uploadComment(comment);
        return "redirect:/images/" + imageId + "/" + title;
    }
}
