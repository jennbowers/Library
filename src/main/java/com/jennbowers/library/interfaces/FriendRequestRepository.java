package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.FriendRequest;
import com.jennbowers.library.models.User;
import org.springframework.data.repository.CrudRepository;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {
    Iterable<FriendRequest> findAllByTouser(User user);
    Iterable<FriendRequest> findAllByFromuser(User user);
    Iterable<FriendRequest> findAllByActive(Boolean booleanParam);
    Iterable<FriendRequest> findAllByPending(Boolean booleanParam);
}
