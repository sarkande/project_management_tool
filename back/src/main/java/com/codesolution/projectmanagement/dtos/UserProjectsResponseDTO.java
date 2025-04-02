package com.codesolution.projectmanagement.dtos;


import java.util.List;

public record UserProjectsResponseDTO(UserDTO user, List<ProjectWithRoleDTO> projects) {}
