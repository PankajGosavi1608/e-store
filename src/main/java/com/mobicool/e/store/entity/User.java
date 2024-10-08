package com.mobicool.e.store.entity;

import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.persistence.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Users")

public class User {
    @Id
    private String userId;

    @Column(name="User_Name")
    private String name;

    @Column(name="User_Email",unique = true)
    private String email;

    @Column(name="User_Password")
    private String password;

    private String gender;

    @Column(name="About")
    private String about;

    @Column(name = "user_image_name")
    private String imageName;


}
