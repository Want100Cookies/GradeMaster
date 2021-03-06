package com.datbois.grademaster.service.impl;

import com.datbois.grademaster.model.*;
import com.datbois.grademaster.repository.GradeRepository;
import com.datbois.grademaster.repository.GroupRepository;
import com.datbois.grademaster.repository.UserRepository;
import com.datbois.grademaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GradeRepository gradeRepository;

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
    public List<User> findByRole(Role role) {
        return userRepository.findAllByRolesContaining(role);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email);
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
        User user = userRepository.findOne(id);
        Set<Group> groups = user.getGroups();
        for (Group group : groups) {
            Set<User> users = group.getUsers();
            users.remove(user);
            group.setUsers(users);
            groupRepository.save(group);
        }
        List<Grade> gradesReceived = user.getGradesReceived();
        for (Grade grade : gradesReceived) {
            gradeRepository.delete(grade.getId());
        }
        List<Grade> gradesDistributed = user.getGradeDistributed();
        for (Grade grade : gradesDistributed) {
            gradeRepository.delete(grade.getId());
        }
        userRepository.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmailContainingIgnoreCase(s);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new UserDetails(user);
    }
}
