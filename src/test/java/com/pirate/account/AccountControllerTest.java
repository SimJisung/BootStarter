package com.pirate.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pirate.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
* 테스트 로직
* 우선 MVC 테스틀 하기 위한 설정들이다. Boot 전용 임으로, Spring MVC framework 에서 사용하는 테스트 방법은 좀 다를것으로 판단된다.
*
* */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;


    MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createAccount() throws Exception {


        AccountDto.Create createDto = accountCreateFixture();

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));


        result.andDo(print());
        result.andExpect(status().isCreated());

        //TODO : JSON PATH
        result.andExpect(jsonPath("$.username", is("simjisung")));
    }

    @Test
    public void createAccount_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();

        createDto.setUsername("sim");
        createDto.setPassword("1234");
        createDto.setFullname("SimJi");

        ResultActions resultActions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());


    }

    @Test
    public void createAccount_DuplicatedException_BadRequest() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();


        ResultActions resultActions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));


        resultActions.andDo(print());
        resultActions.andExpect(status().isCreated());

        resultActions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
    }

    @Test
    public void getAccounts() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();

        accountService.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts"));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void getAccount() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();

        Account account = accountService.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts/" + account.getId()));

        result.andDo(print());
        result.andExpect(status().isOk());


    }

    @Test
    public void updateAccount() throws Exception {
        AccountDto.Create create = accountCreateFixture();
        Account account = accountService.createAccount(create);

        AccountDto.Update update = new AccountDto.Update();

        update.setFullname("braveDevloper");
        update.setPassword("password123");

        //Account updateAccount = accountService.updateAccount(account.getId(), update);


        ResultActions result = mockMvc.perform(put("/accounts/" + account.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));

        result.andDo(print());
        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.fullname", is("braveDevloper")));
        //result.andExpect(jsonPath("$.password", is("password123")));

    }

    @Test
    public void updateAccount_BadRequest() throws Exception {
        AccountDto.Create create = accountCreateFixture();
        Account account = accountService.createAccount(create);

        AccountDto.Update update = new AccountDto.Update();

        update.setFullname("sim");
        update.setPassword("pas");

        //Account updateAccount = accountService.updateAccount(account.getId(), update);


        ResultActions result = mockMvc.perform(put("/accounts/" + account.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));

        result.andDo(print());
        result.andExpect(status().isOk());

        //result.andExpect(jsonPath("$.fullname", is("braveDevloper")));
        //result.andExpect(jsonPath("$.password", is("password123")));

    }

    @Test
    public void deleteAccount() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/accounts/1"));
        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());

        AccountDto.Create create = accountCreateFixture();
        Account account = accountService.createAccount(create);

        resultActions = mockMvc.perform(delete("/accounts/"+account.getId()));
        resultActions.andDo(print());
        resultActions.andExpect(status().isNoContent());
    }

    private AccountDto.Create accountCreateFixture() {
        AccountDto.Create createDto = new AccountDto.Create();

        createDto.setUsername("simjisung");
        createDto.setFullname("SimJisung");
        createDto.setPassword("123456");

        return createDto;
    }

}