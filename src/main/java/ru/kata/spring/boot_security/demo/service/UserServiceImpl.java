package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user ==null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found!", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapPolesToAuthority(user.getRoles()));
    }
    private Collection <? extends GrantedAuthority> mapPolesToAuthority (Collection <Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRolename())).collect(Collectors.toList());
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            return userById.get();
        } else {
            throw new UsernameNotFoundException(String.format("User with %s not found", id));
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void removeUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateUser(Long id, User user) {
        Optional<User> updateUser = userRepository.findById(id);
        if (updateUser.isPresent()) {
            User userUpd = updateUser.get();
            userUpd.setId(id);
            userUpd.setUsername(user.getUsername());
            userUpd.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userUpd.setEmail(user.getEmail());
            userUpd.setRoles(userRepository.findById(id).get().getRoles());
            userRepository.save(userUpd);
        } else {
            throw new UsernameNotFoundException(String.format("User %s with %s not found", user, id));
        }
    }
}
