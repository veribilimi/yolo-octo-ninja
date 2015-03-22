/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postranker;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.api.Post.PostType;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import postranker.domain.AppUser;
import postranker.domain.Comment;
import postranker.domain.Post;

/**
 * Retrieves posts and comments from FB Groups, and ranks them
 *
 * @author Fatih Mehmet Güler
 */
public class PostRankingService {
    private Facebook facebook;
    private String groupId = "418686428146403";

    public void init(String token) {
        facebook = new FacebookTemplate(token);
    }

    public List<Post> getPosts() {
        return getPosts(7);
    }

    public List<Post> getPosts(int days) {
        LinkedList<Post> result = new LinkedList();
        Date till = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * days));

        PagingParameters pp = null;
        while (true) {
            PagedList<org.springframework.social.facebook.api.Post> fbPosts = pp == null ? facebook.feedOperations().getFeed(groupId) : facebook.feedOperations().getFeed(groupId, pp);

            for (org.springframework.social.facebook.api.Post fbPost : fbPosts) {
                Post post = new Post();
                post.setSid(fbPost.getId());
                post.setMessage(fbPost.getMessage());
                if (fbPost.getMessage() == null){
                    if (fbPost.getType() == PostType.LINK) post.setMessage(fbPost.getLink());
                    else post.setMessage("???NULL???");
                }
                post.setCreatedTime(fbPost.getCreatedTime());
                post.setUpdatedTime(fbPost.getUpdatedTime());
                post.setFrom(new AppUser(fbPost.getFrom().getId(), fbPost.getFrom().getName()));

                for (org.springframework.social.facebook.api.Comment fbComment : fbPost.getComments()) {
                    Comment comment = new Comment();
                    comment.setSid(fbComment.getId());
                    comment.setFrom(new AppUser(fbComment.getFrom().getId(), fbComment.getFrom().getName()));
                    comment.setCreatedTime(fbComment.getCreatedTime());
                    comment.setLikeCount(fbComment.getLikesCount());
                    comment.setMessage(fbComment.getMessage());
                    post.getComments().add(comment);
                }

                if (fbPost.getLikes() != null) {
                    for (org.springframework.social.facebook.api.Reference fbReference : fbPost.getLikes()) {
                        post.getLikes().add(new AppUser(fbReference.getId(), fbReference.getName()));
                    }
                }

                result.add(post);
            }

            //check if the last post date is before the till date stop getting posts
            if (result.getLast().getCreatedTime().before(till)) break;

            //proceed to next page (earlier)
            pp = fbPosts.getNextPage();
        }

        return result;
    }

}
