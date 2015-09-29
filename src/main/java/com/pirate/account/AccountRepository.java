package com.pirate.account;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by simjisung on 15. 9. 15..
 */
/*
* 이부분은 Spring Data JPA 부분이다.
* 기본 CRUD는 Spring Data 가 제공해주기때문에, JpaRepository를 상속 받아주면, 기본적인 CRUD가 제공된다.
* 자세한 내용은 직접 JpaRepository 를 타고 들어가 보도록!
* */
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

}
