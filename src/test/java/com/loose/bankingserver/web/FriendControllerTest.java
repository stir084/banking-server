package com.loose.bankingserver.web;

import com.loose.bankingserver.service.FriendService;
import com.loose.bankingserver.web.dto.FriendDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

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
    void getFriendsTest() throws Exception {
        MockHttpSession httpSession = new MockHttpSession();
        String name = "testuser";
        List<FriendDto> friendDtoList = new ArrayList<>();
        when(friendService.getFriends(name)).thenReturn(friendDtoList);
        mockMvc.perform(get("/api/v1/friends")
                        .session(httpSession))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void addFriendTest() throws Exception {
        MockHttpSession httpSession = new MockHttpSession();
        String name = "testuser";
        String friendName = "friend";
        doNothing().when(friendService).addFriend(name, friendName);
        mockMvc.perform(post("/api/v1/friends")
                        .session(httpSession)
                        .param("friendName", friendName))
                .andExpect(status().isOk());
    }
}