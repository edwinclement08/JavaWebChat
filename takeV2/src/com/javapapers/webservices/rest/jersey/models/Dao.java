package com.javapapers.webservices.rest.jersey.models;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    
    
	Optional<T> get(long id);
     
    List<T> getAll();
     
    void save(T t);
     
    boolean update(T t, String[] params);
     
    void delete(T t);
}