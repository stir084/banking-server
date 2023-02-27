package com.loose.bankingserver.web;

import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.service.FriendService;
import com.loose.bankingserver.web.dto.FriendDto;
import com.loose.bankingserver.web.dto.FriendRequestDto;
import com.loose.bankingserver.web.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping
    public ResponseEntity<Void> addFriend(@RequestParam String username, @RequestParam String friendUsername) throws MemberNotFoundException {
        friendService.addFriend(username, friendUsername);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendDto>> getFriends(@RequestParam String username) throws MemberNotFoundException {
        List<FriendDto> friends = friendService.getFriends(username);
        return ResponseEntity.ok().body(friends);
    }

}