package com.vinieduoliveira.ecommerce.controller;

import com.vinieduoliveira.ecommerce.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @GetMapping
    public ResponseEntity<User> findAll() {
        //Test
        User u = new User("Vinicius Oliveira", "vini@gmail.com", "1234", "12345");
        return ResponseEntity.ok().body(u);

    }
}
