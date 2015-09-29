package com.pirate.account;

import com.pirate.commons.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by simjisung on 15. 9. 15..
 */

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create createAccount, BindingResult result) {

        ResponseEntity errorResponse = hasErrorsFromAccountValues(result);
        if (errorResponse != null) return errorResponse;

        Account newAccount = accountService.createAccount(createAccount);
        //입력이 잘 되었는가 안되었는가 판단 해야한다. 1. 리턴 타입 2. 파라미터 이용 3. 서비스에서 예외를 던진다. 3번째 방법이 강추
        //3번째 방법을 사용한 상태이다. (핸들러 사용했음 아래 하단 로직 화인 요망)

        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    //accounts?page=1&size=10&sort=username,desc&sort=joined,desc
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity getAccounts(Pageable pageable) {

        Page<Account> page = accountRepository.findAll(pageable);

       /* List<AccountDto.Response> accountCollectList = page.getContent().stream()
                .map(account -> modelMapper.map(account, AccountDto.Response.class))
                .collect(Collectors.toList());*/

        List<AccountDto.Response> accountCollectList = page.getContent().stream()
                .map(account -> modelMapper.map(account, AccountDto.Response.class))
                .collect(Collectors.toList());

        PageImpl<AccountDto.Response> result = new PageImpl<>(accountCollectList, pageable, page.getTotalElements());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public ResponseEntity getAccount(@PathVariable Long id) {
        /*Account one = accountRepository.findOne(id);
        if(one == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }*/

        Account account = accountService.getAccount(id);

        AccountDto.Response result = modelMapper.map(account, AccountDto.Response.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //전체 업데이트 : PUT (username:"simjisung" , password : "123455" , fullname : "simjisung") vs 부분 업데이트 : PATCH (username : "simjisung")
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateAccount(@PathVariable Long id
            , @RequestBody @Valid AccountDto.Update updateDto
            , BindingResult result) {

        ResponseEntity errorResponse = hasErrorsFromAccountValues(result);
        if (errorResponse != null) return errorResponse;

        //TODO update
        /*Account account = accountRepository.findOne(id);
        if(account == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }*/

        Account updateAccount = accountService.updateAccount(id, updateDto);
        return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDto.Response.class), HttpStatus.OK);

    }

    @RequestMapping(value = "/accounts/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@ControllerAdvice 를 사용하면 이 핸들러 혹은 메소드를 전역으로 설정 할 수 있다. (클래스를 새로 생성해서 만들면 된다.)
    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity handleUserDuplicatedException(UserDuplicatedException e) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage("[ " + e.getUserName() + " ] 중복 username 입니다. ");
        errorResponse.setCode("duplicated.username.exception");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //@ControllerAdvice 를 사용하면 이 핸들러 혹은 메소드를 전역으로 설정 할 수 있다. (클래스를 새로 생성해서 만들면 된다.)
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity handleAccountNotFoundException(AccountNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage("[" + e.getId() + "] 해당하는 계정이 없습니다.");
        errorResponse.setCode("account.not.fount.exception");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity hasErrorsFromAccountValues(BindingResult result) {
        if (result.hasErrors()) {
            //error가 발생하면 본문 응답 추가 하기
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("잘못된 요청 입니다.");
            errorResponse.setCode("bad.request");

            //만약에 필드 에러가 발생하게 되면 어떤 필드에 에러가 났고, 그 값은 무엇이며, 원인이 무엇인지 띄워주는 로직
            List<ErrorResponse.FieldError> errorList = getFieldErrors(result);
            errorResponse.setErrors(errorList);

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private List<ErrorResponse.FieldError> getFieldErrors(BindingResult result) {
        List<ErrorResponse.FieldError> errorList = new ArrayList<>();

        for (FieldError fieldError : result.getFieldErrors()) {
            ErrorResponse.FieldError fieldErrors = new ErrorResponse.FieldError();

            fieldErrors.setField(fieldError.getField());
            fieldErrors.setValue(fieldError.getRejectedValue().toString());
            fieldErrors.setReason(fieldError.getDefaultMessage());

            errorList.add(fieldErrors);
        }
        return errorList;
    }
}