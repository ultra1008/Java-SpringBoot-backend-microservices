package com.harera.hayat.framework.model.user;

import com.harera.hayat.framework.model.BaseEntity;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_")
public class BaseUser extends BaseEntity {

    @Basic
    @Column(name = "phone_number")
    private String mobile;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "uid")
    private String uid;

    @Column(name = "device_token")
    private String deviceToken;
}