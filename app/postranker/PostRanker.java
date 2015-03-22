/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postranker;

import java.util.List;
import postranker.domain.Comment;
import postranker.domain.Post;

/**
 * Rank
 *
 * @author Fatih
 */
public class PostRanker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PostRankingService rankingService = new PostRankingService();
        rankingService.init("CAAE4r0cMaAoBAIatmzQNnZB45QyrbkvekDqmB5zfQfHz1Uj7RCEPLFqADk8ZBw5uTegWZBbgc3zqPC1NQ25S2ELE5TdW1Woq1oJ32PL0uJTkyuBDG0iPwEtaDDWU0iG4BavJ8lZCQ39wtA1CGNlO2j5I1qnrNZApJyZBasClgny7v88Om9biTZARxXbOMhSrEoXHePQsyc1GuIkMQVpZAZCQ0r7msjDsbnJgZD");
        List<Post> posts = rankingService.getPosts(7);
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
