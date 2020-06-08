package ImageHoster.controller;

import ImageHoster.model.Image;
import ImageHoster.model.Tag;
import ImageHoster.model.User;
import ImageHoster.service.ImageService;
import ImageHoster.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private TagService tagService;


    //This method displays all the images in the user home page after successful login
    @RequestMapping("images")
    public String getUserImages(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "images";
    }

    // it reroutes to the image post after selecting a particular image or after the user writes a comment
    @RequestMapping("/images/{id}/{title}")
    public String showImage(@PathVariable("id") Integer imageId, @PathVariable("title") String title, Model model ) throws NullPointerException{
        //getImage method is invoked with the imageId as the argument
        Image image = imageService.getImage(imageId);
        model.addAttribute("image", image);
        model.addAttribute("comments",image.getComments());
        model.addAttribute("tags",image.getTags());
        System.out.println(image.getComments());
        return "images/image";
    }



    //This controller method is called when the request pattern is of type 'images/upload'
    //The method returns 'images/upload.html' file
    @RequestMapping("/images/upload")
    public String newImage() {
        return "images/upload";
    }

    //after the user clicks the upload image button, it routes to this mapping and the image is persisted after the user puts in all necessary information
    @RequestMapping(value = "/images/upload", method = RequestMethod.POST)
    public String createImage(@RequestParam("file") MultipartFile file, @RequestParam("tags") String tags, Image newImage, HttpSession session) throws IOException {

        User user = (User) session.getAttribute("loggeduser");
        newImage.setUser(user);
        String uploadedImageData = convertUploadedFileToBase64(file);
        newImage.setImageFile(uploadedImageData);

        List<Tag> imageTags = findOrCreateTags(tags);
        newImage.setTags(imageTags);
        newImage.setDate(new Date());
        imageService.uploadImage(newImage);
        return "redirect:/images";
    }

    //the user will be able to edit the post, only if the current_user is the same user as the one who created it
    @RequestMapping(value = "/editImage")
    public String editImage(@RequestParam("imageId") Integer imageId, Model model, HttpSession session) {
        Image image = imageService.getImage(imageId);
        User user = (User) session.getAttribute("loggeduser");
        //if condition is executed if the current user is not the user who created the post
        if(image.getUser().getId().intValue() != user.getId().intValue()) {
            //Error message to be displayed when non-user clicks on the edit hyperlink
            String error = "Only the owner of the image can edit the image";
            model.addAttribute("image", image);
            model.addAttribute("tags", image.getTags());
            model.addAttribute("comments",image.getComments());
            model.addAttribute("editError", error);
            return "images/image";
        }
        //else condition is executed if the current user is the user who created the post
        else{
        String tags = convertTagsToString(image.getTags());
        model.addAttribute("image", image);
        model.addAttribute("tags", tags);
        model.addAttribute("comments", image.getComments());
        return "images/edit";
        }
    }

    //after the submit button is pressed, the database values are changed and it is persisted
    @RequestMapping(value = "/editImage", method = RequestMethod.PUT)
    public String editImageSubmit(@RequestParam("file") MultipartFile file, @RequestParam("imageId") Integer imageId, @RequestParam("tags") String tags, Image updatedImage, HttpSession session) throws IOException {

        Image image = imageService.getImage(imageId);
        String updatedImageData = convertUploadedFileToBase64(file);
        List<Tag> imageTags = findOrCreateTags(tags);

        if (updatedImageData.isEmpty())
            updatedImage.setImageFile(image.getImageFile());
        else {
            updatedImage.setImageFile(updatedImageData);
        }

        updatedImage.setId(imageId);
        User user = (User) session.getAttribute("loggeduser");
        updatedImage.setUser(user);
        updatedImage.setTags(imageTags);
        updatedImage.setDate(new Date());

        imageService.updateImage(updatedImage);
        return "redirect:/images/" + updatedImage.getId() + "/" +updatedImage.getTitle();
    }

    //once the delete button is pressed, the post is deleted if the user is same as the one who created it
    @RequestMapping(value = "/deleteImage", method = RequestMethod.POST)
    public String deleteImageSubmit(@RequestParam(name = "imageId") Integer imageId, Model model, HttpSession session) {
        Image image = imageService.getImage(imageId);

        User user = (User) session.getAttribute("loggeduser");
        if(image.getUser().getId() != user.getId()){
            //Error message to be displayed when non-user clicks on the delete button
            String error = "Only the owner of the image can edit the image";

            model.addAttribute("image", image);
            model.addAttribute("tags", image.getTags());
            model.addAttribute("comments", image.getComments());
            model.addAttribute("deleteError", error);
            return "images/image";
        }else
        {
            imageService.deleteImage(imageId);
            return "redirect:/images";
        }
    }

    //This method converts the image to Base64 format
    private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    //it returns the list of tags when it is invoked wrt an image
    private List<Tag> findOrCreateTags(String tagNames) {
        StringTokenizer st = new StringTokenizer(tagNames, ",");
        List<Tag> tags = new ArrayList<Tag>();

        while (st.hasMoreTokens()) {
            String tagName = st.nextToken().trim();
            Tag tag = tagService.getTagByName(tagName);

            if (tag == null) {
                Tag newTag = new Tag(tagName);
                tag = tagService.createTag(newTag);
            }
            tags.add(tag);
        }
        return tags;
    }


    //The method receives the list of all tags
    //Converts the list of all tags to a single string containing all the tags separated by a comma
    //Returns the string
    private String convertTagsToString(List<Tag> tags) {
        StringBuilder tagString = new StringBuilder();

        for (int i = 0; i <= tags.size() - 2; i++) {
            tagString.append(tags.get(i).getName()).append(",");
        }

        Tag lastTag = tags.get(tags.size() - 1);
        tagString.append(lastTag.getName());

        return tagString.toString();
    }
}
