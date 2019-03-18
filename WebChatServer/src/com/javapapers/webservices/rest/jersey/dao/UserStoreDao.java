package com.javapapers.webservices.rest.jersey.dao;

import java.util.List;
import java.util.Optional;

import com.javapapers.webservices.rest.jersey.models.User;

public interface UserStoreDao {
	Optional<User> get(long id);

	List<User> getAll();

	boolean save(User t);

	boolean update(User t, String[] params);

	boolean delete(User t);
}