package com.pirate;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by simjisung on 15. 9. 15..
 */

/*
    @SpringBootApplication 으로 애노테이션을 설정을 하면, 기존에 Spring MVC framework 에서 설정 하는 대부분의 설정들을 대신 해줌.
    스프링 부트는 메인 메소드가 필요하다.

 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    /*
    * ModelMapper 는 Entity 와 DTO 간의 매핑을 담당한다. // Object Mapper 라고 함.
    */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}