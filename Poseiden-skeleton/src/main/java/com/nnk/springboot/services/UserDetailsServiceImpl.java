package com.nnk.springboot.services;


import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class  UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * Load a user by username with specific authorities depending on its role
     * @Override of method loadUserByUserName from UserDetailsService
     * @param username the username equals the user email address
     * @return UserDetails model including username (e-mail address), password and authority)
     * @throws UsernameNotFoundException user cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        com.nnk.springboot.domain.User personToLogin = userRepository.findByUsername(username);
        if(personToLogin.getRole().equals("ADMIN")) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        else {
            grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        }
        return new User(personToLogin.getUsername(), personToLogin.getPassword(), grantedAuthorities);
    }
}