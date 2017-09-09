package com.jennbowers.library.classes;

import com.jennbowers.library.interfaces.FriendRequestRepository;
import com.jennbowers.library.models.FriendRequest;
import com.jennbowers.library.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public class Helpers {

    public List<User> findAllActiveFriends(User user, FriendRequestRepository friendRequestRepo){
        List<User> allFriends = new ArrayList<>();

        Iterable<FriendRequest> toFriends = friendRequestRepo.findAllByTouserAndActive(user, true);
        for(FriendRequest friend : toFriends) {
            User otherFriend = friend.getFromuser();
            allFriends.add(otherFriend);
        }

        Iterable<FriendRequest> fromFriends = friendRequestRepo.findAllByFromuserAndActive(user, true);
        for(FriendRequest friend : fromFriends) {
            User otherFriend = friend.getTouser();
            allFriends.add(otherFriend);
        }

        return allFriends;
    }

    public List<User> findAllPendingFriends(User user, FriendRequestRepository friendRequestRepo) {
        List<User> allPendingFriends = new ArrayList<>();

        Iterable<FriendRequest> toPendingFriends = friendRequestRepo.findAllByTouserAndPending(user, true);
        for(FriendRequest friend : toPendingFriends) {
            User otherFriend = friend.getFromuser();
            allPendingFriends.add(otherFriend);
        }

        Iterable<FriendRequest> fromPendingFriends = friendRequestRepo.findAllByFromuserAndPending(user, true);
        for(FriendRequest friend : fromPendingFriends) {
            User otherFriend = friend.getTouser();
            allPendingFriends.add(otherFriend);
        }

        return allPendingFriends;
    }

    
    

    
}
