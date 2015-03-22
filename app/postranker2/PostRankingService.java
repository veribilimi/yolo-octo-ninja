package postranker2;


import org.springframework.social.facebook.api.impl.FacebookTemplate;
import postranker.domain.AppUser;
import postranker.domain.Post;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import org.springframework.social.facebook.api.Comment;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.api.Reference;

public class PostRankingService {
    private Facebook facebook;
    private String groupId = "418686428146403";

    public PostRankingService() {
    }

    public void init(String token) {
        this.facebook = new FacebookTemplate(token);
    }

    public List<Post> getPosts() {
        return this.getPosts(7);
    }

    public List<Post> getPosts(int days) {
        LinkedList result = new LinkedList();
        Date till = new Date((new Date()).getTime() - (long) (86400000 * days));
        PagingParameters pp = null;

        while (true) {
            PagedList fbPosts = pp == null ? this.facebook.feedOperations().getFeed(this.groupId) : this.facebook.feedOperations().getFeed(this.groupId, pp);

            Post post;
            for (Iterator var6 = fbPosts.iterator(); var6.hasNext(); result.add(post)) {
                org.springframework.social.facebook.api.Post fbPost = (org.springframework.social.facebook.api.Post) var6.next();
                post = new Post();
                post.setSid(fbPost.getId());
                post.setMessage(fbPost.getMessage());
                if (fbPost.getMessage() == null) {
                    if (fbPost.getType() == org.springframework.social.facebook.api.Post.PostType.LINK) {
                        post.setMessage(fbPost.getLink());
                    } else {
                        post.setMessage("???NULL???");
                    }
                }

                post.setCreatedTime(fbPost.getCreatedTime());
                post.setUpdatedTime(fbPost.getUpdatedTime());
                post.setFrom(new AppUser(fbPost.getFrom().getId(), fbPost.getFrom().getName()));
                Iterator var9 = fbPost.getComments().iterator();

                while (var9.hasNext()) {
                    Comment fbReference = (Comment) var9.next();
                    postranker.domain.Comment comment = new postranker.domain.Comment();
                    comment.setSid(fbReference.getId());
                    comment.setFrom(new AppUser(fbReference.getFrom().getId(), fbReference.getFrom().getName()));
                    comment.setCreatedTime(fbReference.getCreatedTime());
                    comment.setLikeCount(Integer.valueOf(fbReference.getLikesCount()));
                    comment.setMessage(fbReference.getMessage());
                    post.getComments().add(comment);
                }

                if (fbPost.getLikes() != null) {
                    var9 = fbPost.getLikes().iterator();

                    while (var9.hasNext()) {
                        Reference fbReference1 = (Reference) var9.next();
                        post.getLikes().add(new AppUser(fbReference1.getId(), fbReference1.getName()));
                    }
                }
            }

            if (((Post) result.getLast()).getCreatedTime().before(till)) {
                return result;
            }

            pp = fbPosts.getNextPage();
        }
    }
}
