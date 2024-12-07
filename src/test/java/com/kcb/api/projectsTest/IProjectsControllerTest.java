package com.kcb.api.projectsTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.kcb.api.projectsTest.controllers.ProjectController;
import com.kcb.api.projectsTest.dtos.ProjectDto;
import com.kcb.api.projectsTest.exceptions.RestExceptionHandler;
import com.kcb.api.projectsTest.exceptions.SystemException;
import com.kcb.api.projectsTest.validations.RecordValidationInterceptor;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Method;


//@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
@TestMethodOrder(OrderAnnotation.class)
public class IProjectsControllerTest extends BaseIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private ProjectController projectController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectController)
                .addInterceptors(new RecordValidationInterceptor())
                .setHandlerExceptionResolvers(createExceptionResolver())
                .build();
    }

    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception, ServletWebRequest webRequest) {
                Method method = new ExceptionHandlerMethodResolver(RestExceptionHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new RestExceptionHandler(), method);
            }
        };
        exceptionResolver.afterPropertiesSet();
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }


    @Test
    @Order(1)
    void getProjectsFailed() throws Exception {
        this.mockMvc.perform(get("/projects"))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andExpect(result -> assertEquals("Projects Not Found", result.getResolvedException().getMessage()));

    }

    @Test
    void createProjectsValidationFailed() throws Exception {
    String payload = """
            {
                "name": null,
                "description": "This is the first projects"
            }
            """;
    this.mockMvc.perform(post("/projects")
        .content(payload)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Data Validation Error : Project Name Must not be empty or null"));
}

    @Test
    @Order(2)
    void createProjectsSuccess() throws Exception {
        this.mockMvc.perform(post("/projects")
            .content(asJsonString(new ProjectDto("project 1", "This is the first projects")))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

    }

}
