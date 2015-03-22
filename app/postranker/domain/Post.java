/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postranker.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Each post
 *
 * @author Fatih
 */
public class Post {
    private String sid;
    private String message;
    private AppUser from;
    private Date createdTime;
    private Date updatedTime;
    private List<AppUser> likes;
    private List<Comment> comments;

    public Post() {
        comments = new LinkedList();
        likes = new LinkedList();
    }

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the from
     */
    public AppUser getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(AppUser from) {
        this.from = from;
    }

    /**
     * @return the createdTime
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime the createdTime to set
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the updatedTime
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime the updatedTime to set
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * @return the likes
     */
    public List<AppUser> getLikes() {
        return likes;
    }

    /**
     * @param likes the likes to set
     */
    public void setLikes(List<AppUser> likes) {
        this.likes = likes;
    }

    /**
     * @return the comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
