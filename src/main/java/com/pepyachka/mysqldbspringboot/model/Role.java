package com.pepyachka.mysqldbspringboot.model;

import lombok.Data;

import javax.persistence.*;

/**
 * todo Document type Role
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "role")
@Data
public class Role {
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String username;
}
