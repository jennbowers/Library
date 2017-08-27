package com.jennbowers.library.models;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;
    private String img;
    private String summary;
    @Column(name = "datepublished")
    private String datePublished;
    private int copies;
    private int rating;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "ownerid")
    private User user;
    @Column(name = "googleid")
    private String googleId;

    public Book() { }

    public Book(String title, String author, String img, String summary, String datePublished, int copies, int rating, boolean active, User user, String googleId) {
        this.title = title;
        this.author = author;
        this.img = img;
        this.summary = summary;
        this.datePublished = datePublished;
        this.copies = copies;
        this.rating = rating;
        this.active = active;
        this.user = user;
        this.googleId = googleId;
    }

    public long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
