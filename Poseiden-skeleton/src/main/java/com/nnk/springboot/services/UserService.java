package com.nnk.springboot.services;


import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRep) {
		this.userRepository = userRep;
	}

	/**
	 * Get a list of all users
	 *
	 * @return list of UserModel containing all user models
	 */
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Check if a username (e-mail address) already exists
	 * @param username the e-mail address of the user
	 * @return true if ID already exists
	 */
	public boolean checkIfUserExistsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	/**
	 * Check if an Id already exists
	 * @param id the user ID
	 * @return true if ID already exists
	 */
	public boolean checkIfUserExistsById(int id) {
		return userRepository.existsById(id);
	}

	/**
	 * Get a user model by ID
	 *
	 * @param id the user ID
	 * @return UserModel found with the ID
	 */
	public Optional<User> getUserById(int id) {
		return userRepository.findById(id);
	}

	/**
	 * Get a user model by email address
	 * @param username the e-mail address
	 * @return UserModel found with the address
	 */
	public User getUserByEmail(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * Save a new user in the DB
	 * @param user the UserModel to save
	 */
	public void saveUser(User user) {
		userRepository.save(user);
	}

	/**
	 * Delete an existent user from the DB
	 * @param id the user ID
	 */
	public void deleteUserById(int id) {
		userRepository.deleteById(id);
	}

	/**
	 * Set an encoded password of the user password (non-encrypted)
	 *
	 * @param user is the user logged-in
	 * @return Encrypted password in the UserModel
	 */
	public User setUpUserModel(User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		return user;
	}

	/**
	 * Get the current logged in user
	 * @return
	 */
	public String getCurrentLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUsername(); // Cela vous donnera le nom d'utilisateur de l'utilisateur connecté
		} else {
			return "Utilisateur non connecté";
		}
	}
}