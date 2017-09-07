package com.jennbowers.library.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book_request")
public class BookRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "touser")
    private User touser;
    @ManyToOne
    @JoinColumn(name = "fromuser")
    private User fromuser;
    @ManyToOne
    @JoinColumn(name = "bookid")
    private Book bookid;
    private String message;
    private Date borrowed;
    private Date due;
    private boolean active;
    private boolean pending;

    public BookRequest() {
    }

    public BookRequest(User touser, User fromuser, Book bookid, String message, Date borrowed, Date due, boolean active, boolean pending) {
        this.touser = touser;
        this.fromuser = fromuser;
        this.bookid = bookid;
        this.message = message;
        this.borrowed = borrowed;
        this.due = due;
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

    public Book getBookid() {
        return bookid;
    }

    public void setBookid(Book bookid) {
        this.bookid = bookid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Date borrowed) {
        this.borrowed = borrowed;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
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
