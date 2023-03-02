package com.loose.bankingserver.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.service.MemberService;
import com.loose.bankingserver.web.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 API 테스트")
    void testSignUp() throws Exception {
        String name = "testuser";
        String password = "password";
        MemberDto memberDto = new MemberDto();
        memberDto.setName(name);
        memberDto.setPassword(password);

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(post("/api/v1/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(memberDto)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 계정 정보")
    void loginFailed_invalidAccount() throws Exception {
        // given
        doThrow(new MemberNotFoundException("회원을 찾을 수 없습니다.")).when(memberService).login("invalid", "invalid", null);

        // when
        MvcResult result = mockMvc.perform(post("/api/v1/members/login")
                        .param("name", "invalid")
                        .param("password", "invalid"))
                .andReturn();

        // then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
        assertEquals("로그인에 실패하였습니다.", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("로그아웃 API 테스트")
    void testLogout() throws Exception {
        // 로그아웃
        MvcResult result = mockMvc.perform(post("/api/v1/members/logout"))
                .andReturn();
        assertEquals(204, result.getResponse().getStatus());

        // 세션 검증
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertEquals(null, session.getAttribute("name"));
    }
}