package com.pepyachka.mysqldbspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * todo Document type UserRole
 */
@Entity // This tells Hibernate to make a table out of this class
@Table (name = "customer_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole implements Serializable {

    @Id
    Integer customer_id;
    @Id
    Integer role_id;

}
