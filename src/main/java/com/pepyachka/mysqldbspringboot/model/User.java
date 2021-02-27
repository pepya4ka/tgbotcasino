package com.pepyachka.mysqldbspringboot.model;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity // This tells Hibernate to make a table out of this class
@Table (name = "customer")
@Data
public class User {
    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "chatId")
    private Integer chatId;

    @Basic
    @Column(name = "coins")
    private Integer coins;

    @Basic
    @Column(name = "maxPrize")
    private Integer maxPrize;

    @Basic
    @Column(name = "date_prize")
    private LocalDateTime prizeDate;
}
