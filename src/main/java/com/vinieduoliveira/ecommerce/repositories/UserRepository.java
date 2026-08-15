package com.vinieduoliveira.ecommerce.repositories;

import com.vinieduoliveira.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
