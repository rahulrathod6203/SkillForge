package com.sf.appUser.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name" , nullable = false)
    @NotBlank(message = "Name cannot be blank!")
    private String fullName;

    @Column(name = "user_email" , nullable = false , unique = true)
    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Enter a valid email!")
    private String email;

    @Column(name = "password" , nullable = false)
    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 8, message = "Password should have minimum 8 characters!")
    private String password;


    @Column(name = "user_phone" , nullable = false , unique = true)
    @NotBlank(message = "Phone cannot be blank!")
    @Size(min = 10, message = "Enter a valid phone number!")
    private String phone;

    @Column(name = "user_address" , nullable = false)
    @NotBlank(message = "Address cannot be blank!")
    private String address;

    @Column(name = "user_created_At" , nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_updated_At" , nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "user_active_status" , nullable = false)
    private Boolean active;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PostUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
