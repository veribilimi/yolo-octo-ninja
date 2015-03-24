/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookapi.domain;

import java.util.Date;

/**
 * Each Comment
 *
 * @author Fatih
 */
public class Comment {
    private String sid;
    private String message;
    private AppUser from;
    private Date createdTime;
    private Integer likeCount;

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
     * @return the likeCount
     */
    public Integer getLikeCount() {
        return likeCount;
    }

    /**
     * @param likeCount the likeCount to set
     */
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
