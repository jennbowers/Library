package com.jennbowers.library.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shelf")
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

//    @ManyToMany(mappedBy = "shelves", cascade = CascadeType.ALL)
//    private List<Book> books = new ArrayList<>();

    public Shelf() {
    }

    public Shelf(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
