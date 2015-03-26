/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookapi;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import facebookapi.domain.AppUser;
import facebookapi.domain.Comment;
import facebookapi.domain.Post;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Retrieves posts and comments from FB Groups, and ranks them
 *
 * @author Fatih Mehmet Güler
 */
public class FacebookApiService {
    private Facebook facebook;

    public void init(String token) {
        facebook = new FacebookTemplate(token);
    }

    public List<Post> getPosts(String groupId) {
        return getPosts(groupId, 7);
    }

    public List<Post> getPosts(String groupId, int days) {
        LinkedList<Post> result = new LinkedList();
        Date till = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * days));

        PagingParameters pp = null;
        while (true) {
            PagedList<org.springframework.social.facebook.api.Post> fbPosts = pp == null ? facebook.feedOperations().getFeed(groupId) : facebook.feedOperations().getFeed(groupId, pp);
            if (fbPosts.isEmpty()) break;

            for (org.springframework.social.facebook.api.Post fbPost : fbPosts) {
                Post post = new Post();
                post.setSid(fbPost.getId());
                post.setMessage(fbPost.getMessage());
                if (fbPost.getMessage() == null) {
                    if (fbPost.getLink() != null) post.setMessage(fbPost.getLink());
                    else post.setMessage("???NULL???");
                }
                post.setCreatedTime(fbPost.getCreatedTime());
                post.setUpdatedTime(fbPost.getUpdatedTime());
                post.setFrom(new AppUser(fbPost.getFrom().getId(), fbPost.getFrom().getName()));
                //also add the link
                try {
                    post.setLink(new URL(fbPost.getLink()));
                } catch (MalformedURLException ex) {
                    String[] ids = fbPost.getId().split("_");
                    try {
                        post.setLink(new URL("https://facebook.com/" + ids[0] + "/posts/" + ids[1]));
                    } catch (MalformedURLException ex1) {                        
                        post.setLink(null);
                    }
                }

                //get the comments (may be paginated)
                PagingParameters ppc = null;
                while (true) {
                    PagedList<org.springframework.social.facebook.api.Comment> fbComments = ppc == null ? facebook.commentOperations().getComments(fbPost.getId()) : facebook.commentOperations().getComments(fbPost.getId(), ppc);
                    if (fbComments.isEmpty()) break;

                    for (org.springframework.social.facebook.api.Comment fbComment : fbComments) {
                        Comment comment = new Comment();
                        comment.setSid(fbComment.getId());
                        comment.setFrom(new AppUser(fbComment.getFrom().getId(), fbComment.getFrom().getName()));
                        comment.setCreatedTime(fbComment.getCreatedTime());
                        comment.setLikeCount(fbComment.getLikesCount());
                        comment.setMessage(fbComment.getMessage());
                        post.getComments().add(comment);
                    }
                    ppc = fbComments.getNextPage();
                    if (ppc == null) break;
                }

                if (fbPost.getLikes() != null) {
                    for (org.springframework.social.facebook.api.Reference fbReference : fbPost.getLikes()) {
                        post.getLikes().add(new AppUser(fbReference.getId(), fbReference.getName()));
                    }
                }

                result.add(post);
            }

            //check if the last post date is before the till date stop getting posts
            Date postDate = result.getLast().getUpdatedTime() == null ? result.getLast().getCreatedTime() : result.getLast().getUpdatedTime();
            if (postDate.before(till)) {
                System.out.println("found some post before till date, breaking");
                break;
            }

            //proceed to next page (earlier)
            pp = fbPosts.getNextPage();
        }

        return result;
    }

}
