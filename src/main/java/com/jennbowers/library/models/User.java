package com.jennbowers.library.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_data")
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    private String username;
    private String password;
    private boolean active;
    private String email;
    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> books;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Shelf> shelves;

    @OneToMany(mappedBy = "touser", cascade = CascadeType.ALL)
    private List<FriendRequest> toUsers;

    @OneToMany(mappedBy = "fromuser", cascade = CascadeType.ALL)
    private List<FriendRequest> fromUsers;



    public User() { }

    public User(String firstName, String lastName, String username, String password, boolean active, String email, Role role, List<Book> books, List<Shelf> shelves, List<FriendRequest> toUsers, List<FriendRequest> fromUsers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.active = active;
        this.email = email;
        this.role = role;
        this.books = books;
        this.shelves = shelves;
        this.toUsers = toUsers;
        this.fromUsers = fromUsers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }

    public List<FriendRequest> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<FriendRequest> toUsers) {
        this.toUsers = toUsers;
    }

    public List<FriendRequest> getFromUsers() {
        return fromUsers;
    }

    public void setFromUsers(List<FriendRequest> fromUsers) {
        this.fromUsers = fromUsers;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
