package com.kcb.api.projectsTest.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 *
 * @author martin
 */
public record TaskDto(@NotBlank String title,String description,TaskStatus status,@JsonFormat(pattern = "YYYY-MM-dd")LocalDateTime dueDate) {

}
