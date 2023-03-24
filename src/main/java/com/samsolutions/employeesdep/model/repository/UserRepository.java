package com.samsolutions.employeesdep.model.repository;

import com.samsolutions.employeesdep.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("delete from User u where u.login <> :login")
    void deleteByLoginNot(@Param("login") String login);
}
