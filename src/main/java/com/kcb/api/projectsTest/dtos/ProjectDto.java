package com.kcb.api.projectsTest.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author martin
 */
public record ProjectDto(@NotBlank(message = "Project Name Must not be empty") String name,String description) {

}
