package com.cat.sys.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Attempts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "user_name")
    private String userName;

    private int attempts;
}
