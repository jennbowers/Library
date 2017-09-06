package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.FriendRequest;
import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {
    Iterable<FriendRequest> findAllByTouserOrFromuserAndActive(User toUser, User fromUser,Boolean booleanParam);
    Iterable<FriendRequest> findAllByTouserAndActive(User user, Boolean booleanParam);
    Iterable<FriendRequest> findAllByFromuserAndActive(User user, Boolean booleanParam);
    Iterable<FriendRequest> findAllByActive(Boolean booleanParam);
    Iterable<FriendRequest> findAllByPending(Boolean booleanParam);
}
