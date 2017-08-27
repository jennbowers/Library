package com.jennbowers.library.interfaces;

import com.jennbowers.library.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
   Role findByName (String name);
}
