package com.loose.bankingserver.web;

import com.loose.bankingserver.service.AccountService;
import com.loose.bankingserver.service.FriendService;
import com.loose.bankingserver.web.dto.AccountDto;
import com.loose.bankingserver.web.dto.FriendDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("내 계좌 조회 - 성공")
    void getMyAccountsTest() throws Exception {
        MockHttpSession httpSession = new MockHttpSession();
        String name = "testuser";
        List<AccountDto> accountDtoList = new ArrayList<>();
        when(accountService.getMyAccounts(name)).thenReturn(accountDtoList);

        mockMvc.perform(get("/api/v1/accounts")
                        .session(httpSession))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("송금 - 성공")
    void transferTest() throws Exception {
        String senderName = "sender";
        String receiverName = "receiver";
        long amount = 1000;

        mockMvc.perform(post("/api/v1/" + senderName + "/transfer/" + receiverName)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(content().string("이체가 완료되었습니다."));

        verify(accountService, times(1)).transfer(senderName, receiverName, amount);
    }
}