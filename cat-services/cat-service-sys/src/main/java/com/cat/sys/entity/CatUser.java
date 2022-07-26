package com.cat.sys.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class CatUser{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", unique = true)
    @NonNull
    private String userId;

    @Column(name = "user_name")
    private String userName;

    private String password;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

}
