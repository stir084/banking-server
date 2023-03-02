package com.loose.bankingserver.web;

import com.loose.bankingserver.service.FriendService;
import com.loose.bankingserver.web.dto.FriendDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FriendController.class)
class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService;

    @Test
    @DisplayName("친구 목록 조회 API Test")
    void getFriendsTest() throws Exception {
        MockHttpSession httpSession = new MockHttpSession();
        String name = "junho";
        List<FriendDto> friendDtoList = new ArrayList<>();
        when(friendService.getFriends(name)).thenReturn(friendDtoList);
        mockMvc.perform(get("/api/v1/friends")
                        .session(httpSession))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("친구 추가 Api Test")
    void addFriendTest() throws Exception {
        MockHttpSession httpSession = new MockHttpSession();
        String name = "junho";
        String friendName = "loose";
        doNothing().when(friendService).addFriend(name, friendName);
        mockMvc.perform(post("/api/v1/friends")
                        .session(httpSession)
                        .param("friendName", friendName))
                .andExpect(status().isOk());
    }
}