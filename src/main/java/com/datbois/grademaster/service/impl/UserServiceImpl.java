package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.User;
import com.datbois.grademaster.repository.UserRepository;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User save(User user) {
        if (user.getPassword() != null) {
            if (!Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}").matcher(user.getPassword()).matches()) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new com.datbois.grademaster.model.UserDetails(user);
    }
}
