package com.example.cryptoapp.user.repos;

import com.example.cryptoapp.user.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    Optional<UserRole> findUserRoleByRoleName(String roleName);
}
