package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.FriendRequest;
import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {
    Iterable<FriendRequest> findAllByTouserAndActive(User user, Boolean booleanParam);
    Iterable<FriendRequest> findAllByFromuserAndActive(User user, Boolean booleanParam);
    Iterable<FriendRequest> findAllByTouserAndPending(User user, Boolean booleanParam);
}
