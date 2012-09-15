package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.User;
import com.pikefin.exceptions.GenericException;
/**
 * User Dao contains the methods related to all user operations
 * 
 * @author Amar_Deep_Singh
 *
 */
public interface UserDao {
	public User saveUserInfo(User userEntity) throws GenericException;
	public User updateUserInfo(User userEntity) throws GenericException;
	public Boolean deleteUserInfo(User userEntity ) throws GenericException;
	public Boolean deleteUserInfoById(Integer userId ) throws GenericException;
	public User loadUserInfo(Integer userId) throws GenericException;
	public User loadUserInfoByUserName(String userName) throws GenericException;
	public List<User> loadAllUsers() throws GenericException;
	public List<User> loadAllUsersByAccountEmail(String accountEmail) throws GenericException;
} 
