/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postranker.domain;

/**
 * The sender of posts/comments
 *
 * @author Fatih
 */
public class AppUser {
    private String sid;
    private String name;

    public AppUser() {
    }

    public AppUser(String sid, String name) {
        this.sid = sid;
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return sid;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.sid = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
