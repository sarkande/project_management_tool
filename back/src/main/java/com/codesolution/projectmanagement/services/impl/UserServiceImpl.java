package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.dao.UserRepository;
import com.codesolution.projectmanagement.dtos.UserDTO;
import com.codesolution.projectmanagement.exceptions.BadLoginException;
import com.codesolution.projectmanagement.exceptions.EntityAlreadyExistException;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.ProjectUser;
import com.codesolution.projectmanagement.models.User;
import com.codesolution.projectmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO login(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user == null)
            throw new BadLoginException();
        return new UserDTO(user.getId(), user.getEmail(), user.getUsername());
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = new ArrayList<User>();
        userRepository.findAll().forEach(users::add);

        List<UserDTO> usersDTO = new ArrayList<UserDTO>();
        for(User user : users){
            usersDTO.add(new UserDTO(user.getId(), user.getEmail(), user.getUsername()));
        }
        return usersDTO;
    }

    @Override
    public UserDTO findById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            throw new EntityDontExistException();

        User userObj = user.get();
        return new UserDTO(userObj.getId(), userObj.getEmail(), userObj.getUsername());
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(EntityDontExistException::new);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(EntityDontExistException::new);
    }

    @Override
    public int created(User user) {
        if(userRepository.existsByEmail(user.getEmail()))
            throw new EntityAlreadyExistException();

        return userRepository.save(user).getId();
    }

    @Override
    public void update(Integer id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    @Override
    public void updatePartial(Integer id, User oldUser, User newUser) {
        this.findById(id);
        //username, email, password
        if(newUser.getUsername() != null){
            oldUser.setUsername(newUser.getUsername());
        }
        if(newUser.getEmail() != null){
            oldUser.setEmail(newUser.getEmail());
        }
        if(newUser.getPassword() != null){
            oldUser.setPassword(newUser.getPassword());
        }

        userRepository.save(oldUser);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Project> findUserProjects(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("User not found with id: " + id));

        return  user.getProjectUsers().stream()
                .map(ProjectUser::getProject)
                .collect(Collectors.toList());

    }
}
