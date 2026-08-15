package com.vinieduoliveira.ecommerce.model;

import com.vinieduoliveira.ecommerce.common.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_user")
public class User extends BaseModel {

    @NotBlank(message = "The name can't be empty ")
    @Size(min = 3, message = "The name needs at least 3 characters ")
    private String name;

    @NotBlank(message = "The email can't be empty ")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "The phone can't be empty ")
    @Pattern(regexp = "^\\d{2}9\\d{8}$", message = "Invalid phone (use area code + 9 + number, digits only)")
    private String phone;

    @NotBlank(message = "The password can't be empty ")
    private String password;

    public User() {
    }

    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}