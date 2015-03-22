package postranker2;

import postranker.domain.Comment;
import postranker.domain.Post;

import java.util.Iterator;
import java.util.List;


public class PostRanker {
    public PostRanker() {
    }

    public static void main(String[] args) {
        PostRankingService rankingService = new PostRankingService();
        rankingService.init("CAAE4r0cMaAoBAIatmzQNnZB45QyrbkvekDqmB5zfQfHz1Uj7RCEPLFqADk8ZBw5uTegWZBbgc3zqPC1NQ25S2ELE5TdW1Woq1oJ32PL0uJTkyuBDG0iPwEtaDDWU0iG4BavJ8lZCQ39wtA1CGNlO2j5I1qnrNZApJyZBasClgny7v88Om9biTZARxXbOMhSrEoXHePQsyc1GuIkMQVpZAZCQ0r7msjDsbnJgZD");
        List posts = rankingService.getPosts(7);
        System.out.println(posts.size());
        Iterator var3 = posts.iterator();

        while(var3.hasNext()) {
            Post post = (Post)var3.next();
            System.out.println("**POST**");
            System.out.println("Sender: " + post.getFrom().getName());
            System.out.println("Date: " + post.getCreatedTime());
            System.out.println("Likes: " + post.getLikes());
            System.out.println(post.getMessage().substring(0, post.getMessage().length() > 100?100:post.getMessage().length()));
            System.out.println("***COMMENTS***");
            Iterator var5 = post.getComments().iterator();

            while(var5.hasNext()) {
                Comment comment = (Comment)var5.next();
                System.out.println("\tSender: " + comment.getFrom());
                System.out.println("\tDate: " + comment.getCreatedTime());
                System.out.println("\tLike count: " + comment.getLikeCount());
                System.out.println("\tMessage: " + comment.getMessage().substring(0, comment.getMessage().length() > 100?100:comment.getMessage().length()));
            }
        }

    }
}
