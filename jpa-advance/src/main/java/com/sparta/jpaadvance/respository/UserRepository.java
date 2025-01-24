package com.sparta.jpaadvance.respository;

import com.sparta.jpaadvance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
