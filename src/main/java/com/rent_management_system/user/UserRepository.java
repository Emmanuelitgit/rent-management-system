package com.rent_management_system.user;

import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.firstName = :firstname AND u.lastName = :lastname")
    public List<User> getAllUsers(@Param("firstname") String firstname, @Param("lastname") String lastname);
}
