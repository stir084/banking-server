package com.loose.bankingserver.web;

import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.service.FriendService;
import com.loose.bankingserver.web.dto.FriendDto;
import com.loose.bankingserver.web.dto.FriendRequestDto;
import com.loose.bankingserver.web.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping
    public ResponseEntity<Void> addFriend(HttpSession session, @RequestParam String friendName) throws MemberNotFoundException {
        String name = (String) session.getAttribute("name");
        friendService.addFriend(name, friendName);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendDto>> getFriends(HttpSession session) throws MemberNotFoundException {
        String name = (String) session.getAttribute("name");
        List<FriendDto> friends = friendService.getFriends(name);
        return ResponseEntity.ok().body(friends);
    }

}