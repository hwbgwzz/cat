package com.cat.sys.repository;

import com.cat.sys.entity.CatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CatUser,String> {
    Optional<CatUser> findUserByUserName(String userName);
}
