/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookapi;

import java.util.List;
import facebookapi.domain.Comment;
import facebookapi.domain.Post;

/**
 * Rank
 *
 * @author Fatih
 */
public class FacebookApiServiceTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String groupId = "418686428146403";
        FacebookApiService rankingService = new FacebookApiService();
        rankingService.init("343800439138314|EfAO_J7NepZsopex7pTx83hlFU0");
        List<Post> posts = rankingService.getPosts(groupId, 7);
        System.out.println(posts.size());
        for (Post post : posts) {
            System.out.println("**POST**");
            System.out.println("Sender: " + post.getFrom().getName());
            System.out.println("Date: " + post.getCreatedTime());
            System.out.println("Likes: " + post.getLikes());
            System.out.println(post.getMessage().substring(0, post.getMessage().length() > 100 ? 100 : post.getMessage().length()));
            System.out.println("***COMMENTS***");
            for (Comment comment : post.getComments()) {
                System.out.println("\tSender: " + comment.getFrom());
                System.out.println("\tDate: " + comment.getCreatedTime());
                System.out.println("\tLike count: " + comment.getLikeCount());
                System.out.println("\tMessage: " + comment.getMessage().substring(0, comment.getMessage().length() > 100 ? 100 : comment.getMessage().length()));
            }
        }

    }

}
