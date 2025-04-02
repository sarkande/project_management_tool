package com.codesolution.projectmanagement.dtos;

import com.codesolution.projectmanagement.models.User;

public record UserDTO(Integer id, String email, String username) {

    public UserDTO(User user) {
        this(user.getId(),user.getEmail(), user.getUsername());
    }

}
