package com.jennbowers.library.models;

import javax.persistence.*;

@Entity
@Table(name = "friend_request")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "touser")
    private User touser;
    @ManyToOne
    @JoinColumn(name = "fromuser")
    private User fromuser;
    private boolean active;
    private boolean pending;

    public FriendRequest() {
    }

    public FriendRequest(long id, User touser, User fromuser, boolean active, boolean pending) {
        this.id = id;
        this.touser = touser;
        this.fromuser = fromuser;
        this.active = active;
        this.pending = pending;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getTouser() {
        return touser;
    }

    public void setTouser(User touser) {
        this.touser = touser;
    }

    public User getFromuser() {
        return fromuser;
    }

    public void setFromuser(User fromuser) {
        this.fromuser = fromuser;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
