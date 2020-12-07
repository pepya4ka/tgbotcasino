package com.pepyachka.mysqldbspringboot;

import org.springframework.data.repository.CrudRepository;

import com.pepyachka.mysqldbspringboot.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

}
