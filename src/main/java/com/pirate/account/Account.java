package com.pirate.account;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by simjisung on 15. 9. 15..
 */
/*
    Account 엔티티이다.
    아래 특이점은 setter/getter hashcode/toString 이 없는걸 확인 할 수 있다.
    없는 이유는 lombok plugin 을 사용하였기 때문에, 생략이 가능하다.
    엔티티는 늘 하던 작업이라 생소하지는 않다.
*/
@Entity
@Data
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    private String fullName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

}