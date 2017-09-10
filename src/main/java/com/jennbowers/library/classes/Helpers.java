package com.jennbowers.library.classes;

import com.jennbowers.library.interfaces.FriendRequestRepository;
import com.jennbowers.library.interfaces.UserRepository;
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

    public List<User> findAllNotFriends (User user, FriendRequestRepository friendRequestRepo, UserRepository userRepo) {
        List<User> allNotFriends = new ArrayList<>();

        Helpers helpers = new Helpers();
        List<User> allActiveFriends = helpers.findAllActiveFriends(user, friendRequestRepo);
        List<User> allPendingFriends = helpers.findAllPendingFriends(user, friendRequestRepo);

        List<User> allFriends = new ArrayList<>();
        allFriends.addAll(allActiveFriends);
        allFriends.addAll(allPendingFriends);

        Iterable<User> allUsers = userRepo.findAll();
        for (User oneUser : allUsers){
            if(!allFriends.contains(oneUser)) {
                allNotFriends.add(oneUser);
            }
        }


        return allNotFriends;


//        List<User> allNotActive = new ArrayList<>();
//        List<User> allNotPending = new ArrayList<>();
//
//        Iterable<FriendRequest> toNotFriendsActive = friendRequestRepo.findAllByTouserAndActive(user, false);
//        for(FriendRequest friend : toNotFriendsActive) {
//            User otherFriend = friend.getFromuser();
//            allNotActive.add(otherFriend);
//            allNotFriends.add(otherFriend);
//        }
//
//        Iterable<FriendRequest> fromNotFriendsActive = friendRequestRepo.findAllByFromuserAndActive(user, false);
//        for(FriendRequest friend : fromNotFriendsActive) {
//            User otherFriend = friend.getTouser();
//            allNotActive.add(otherFriend);
//            allNotFriends.add(otherFriend);
//        }
//
//        Iterable<FriendRequest> toNotFriendsPending = friendRequestRepo.findAllByTouserAndPending(user, true);
//        for(FriendRequest friend : toNotFriendsPending) {
//            User otherFriend = friend.getFromuser();
//            allNotPending.add(otherFriend);
//        }
//
//        Iterable<FriendRequest> fromNotFriendsPending = friendRequestRepo.findAllByFromuserAndPending(user, false);
//        for(FriendRequest friend : fromNotFriendsPending) {
//            User otherFriend = friend.getTouser();
//            allNotPending.add(otherFriend);
//        }
//
//        for(User notActive: allNotActive) {
//            for(User notPending : allNotPending) {
//                if(notActive != notPending) {
//                    allNotFriends.add(notPending);
//                }
//            }
//        }

    }
    
}
