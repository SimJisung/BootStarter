package com.pirate.account;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by simjisung on 15. 9. 15..
 */

/*
* DTO영역이다.
* 각각 상황에 따라서 Data를 출력하거나 입력하는 컬럼들은 각기 다를것이다.
* 그 상황에 맞춰서 DTO를 짜주는것이 깔끔하다.
*
* */
public class AccountDto {

    /*
    * Account 를 생성 할때, 사용하는 DTO
    * */
    @Data
    public static class Create {

        @NotBlank
        @Size(min = 5)
        private String username;

        @NotBlank
        @Size(min = 5)
        private String fullname;

        @NotBlank
        @Size(min = 5)
        private String password;
    }

   /*
   * Account 내용을 출력 할때 사용하는 DTO
   * */

    @Data
    public static class Response {

        private Long id;
        private String username;
        private String fullname;
        private Date joined;
        private Date updated;

    }

    //Account Update 전용 DTO
    @Data
    public static class Update {

        @NotBlank
        @Size(min = 5)
        private String password;

        @NotBlank
        @Size(min = 5)
        private String fullname;
    }
}

