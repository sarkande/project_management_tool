package com.codesolution.projectmanagement.services;

import com.codesolution.projectmanagement.dtos.UserDTO;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.User;

import java.util.List;

public interface UserService {

    public List<UserDTO> findAll();
    public UserDTO findById(Integer id);
    public User findByEmail(String email);
    public User findUserById(Integer id);
    public int created(User user);
    public void update(Integer id, User user);
    public void updatePartial(Integer id, User oldUser, User newUser);
    public void delete(Integer id);

    public UserDTO login(String email, String password);
    public List<Project> findUserProjects(Integer id);

}
