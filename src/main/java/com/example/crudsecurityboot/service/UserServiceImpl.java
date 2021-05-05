package com.example.crudsecurityboot.service;

import com.example.crudsecurityboot.dao.RoleRepository;
import com.example.crudsecurityboot.dao.UserRepository;
import com.example.crudsecurityboot.model.Role;
import com.example.crudsecurityboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    ConversionService conversionService;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserId(Long id) {
        return userRepository.findById(id).get();
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getByName(String name) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.name=:name" ,User.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findAny().orElse(null);
    }

    @Override
    public Role getByRole(String name) {
        TypedQuery<Role> query = entityManager.createQuery("select r from Role r where r.name=:name", Role.class);
        query.setParameter("name", name);
        return query.getResultList().get(0);
    }


    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }


    @Override
    public void updateUserAndRoles(User user, String[] roleListInMethod) {
        Set<Role> update = new HashSet<>();


            for (int i =0; i<roleListInMethod.length; i++) {
                if (!user.getRoles().contains(roleListInMethod[i])){

                    update.add(getByRole(roleListInMethod[i]));
                }
                user.setRoles(update);
            }

        userRepository.save(user);
        }





    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = getByName(name);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Role role: user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
