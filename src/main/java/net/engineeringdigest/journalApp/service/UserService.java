package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepositry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepositry userRepositry;

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public void saveUser(User user){
        userRepositry.save(user);
    }

    public void saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepositry.save(user);
    }

    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("ADMIN"));
        userRepositry.save(user);
    }

    public List<User> getall(){
        return userRepositry.findAll();
    }

    public Optional<User> findById(ObjectId id){
        return userRepositry.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepositry.deleteById(id);
    }

    public User findByUserName(String userName){
        return userRepositry.findByUserName(userName);
    }
}
