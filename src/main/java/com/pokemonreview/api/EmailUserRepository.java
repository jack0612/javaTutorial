package com.pokemonreview.api;

import org.springframework.data.repository.CrudRepository;

import com.pokemonreview.api.EmailUser;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete


public interface EmailUserRepository extends CrudRepository<EmailUser, Integer> {

}
