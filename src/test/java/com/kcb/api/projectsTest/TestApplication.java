package com.kcb.api.projectsTest;

import org.springframework.boot.SpringApplication;

class TestApplication {

	public static void main(String[] args) {
        SpringApplication
                //note that we are starting our actual Application from within our TestApplication
                .from(ProjectsTestApplication::main) 
                .with(ContainersConfig.class)
                .run(args);
    }

}
