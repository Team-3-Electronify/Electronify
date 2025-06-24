package com.femcoders.electronify.user;

import com.femcoders.electronify.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
