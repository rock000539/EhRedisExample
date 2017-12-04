package com.redixExample.demo.service;

import java.util.List;

import com.redixExample.demo.entity.User;
import com.redixExample.demo.entity.User.Sex;

public interface UserService {
	   User findById(long id);

	    List<User> findByPage(int startIndex, int limit);

	    List<User> findBySex(Sex sex);

	    List<User> findByAge(int lessAge);

	    List<User> findByUsers(List<User> users);

	    boolean update(User user);

	    boolean deleteById(long id);
}
