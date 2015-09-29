package com.pirate.account;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by simjisung on 15. 9. 15..
 */
/*
* 서비스 영역
* 일단 Repository 를  DI 받고 여기서 ModelMapper도 받았다.
* 일반적인 서비스 단의 로직이다.
* ModelMapper를 사용해서,createAccount로 받은 Data정보를 Account클래스에 매핑하여, 그 데이터를 그대로 save하는 로직이다.
*
* */
@Service
@Transactional
@Slf4j
public class AccountService {

    //private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Account createAccount(AccountDto.Create createAccount) {
        Account account = modelMapper.map(createAccount, Account.class);

        //TODO:유효한 userName인지 판단
        String username = createAccount.getUsername();
        if (accountRepository.findByUsername(username) != null) {
            log.error("user duplicated exception. {}", username);
            throw new UserDuplicatedException(username);
        }
        //TODO: password hasing

        Date todayDate = new Date();
        account.setJoined(todayDate);
        account.setUpdated(todayDate);

        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, AccountDto.Update updateDto) {
        Account account = getAccount(id);
        account.setPassword(updateDto.getPassword());
        account.setFullName(updateDto.getFullname());

        //Spring Data JPA 에서는 save 의 기능은 기존의 데이터가 없으면 persist , 있으면 merge 를 동작한다.
        //내가 알고 있는 jpa는 그냥 set만 해줘도 바로 update가 되는걸로 알고 있지만 혹시 모르니 save를 추가 하도록 한다.
        Account updateAccount = accountRepository.save(account);
        return updateAccount;

        //return account;
    }

    public Account getAccount(Long id) {
        Account account = accountRepository.findOne(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }

    public void deleteAccount(Long id) {
        accountRepository.delete(getAccount(id));

    }

}


