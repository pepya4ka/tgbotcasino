package com.pepyachka.mysqldbspringboot.repository;

import com.pepyachka.mysqldbspringboot.model.Role;
import com.pepyachka.mysqldbspringboot.model.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * todo Document type RoleRepository
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    @Query("SELECT new com.pepyachka.mysqldbspringboot.model.UserRole(ur.customer_id, ur.role_id) FROM UserRole AS ur")
    List<UserRole> getListUserRole();

}
