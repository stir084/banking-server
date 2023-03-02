package com.loose.bankingserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loose.bankingserver.service.MemberService;
import com.loose.bankingserver.web.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class BankingServerApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MemberService memberService;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("로그인 API - 성공적인 로그인 테스트")
	void testLogin() throws Exception {
		String name = "testuser";
		String password = "password";

		MemberDto memberDto = new MemberDto();
		memberDto.setName(name);
		memberDto.setPassword(password);

		ObjectMapper mapper = new ObjectMapper();

		// 회원 가입
		mockMvc.perform(post("/api/v1/members/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsBytes(memberDto)))
				.andExpect(status().isOk());

		// 로그인
		MockHttpSession mockHttpSession = (MockHttpSession) mockMvc.perform(post("/api/v1/members/login")
						.param("name", name)
						.param("password", password))
				.andExpect(status().isOk())
				.andExpect(request().sessionAttribute("name", name))
				.andReturn()
				.getRequest()
				.getSession();

		String sessionName = (String) mockHttpSession.getAttribute("name");
		assertNotNull(sessionName);
		assertEquals(name, sessionName);
	}
}
