package com.javapapers.webservices.rest.jersey.dao;

import java.util.List;
import java.util.Optional;

import com.javapapers.webservices.rest.jersey.models.User;

public interface UserStoreDao {
	Optional<User> get(long id);

	public Optional<User> getByUserName(String userName);

	public Optional<User> getByUserObject(User user);

	List<User> getAll();

	boolean save(User t);

	boolean add(User t);

	boolean update(User t, String[] params);

	public boolean updateByUsername(String userName, String oldPassword, String[] params);

	boolean delete(User t);
}