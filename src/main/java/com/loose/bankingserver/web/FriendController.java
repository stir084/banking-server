package com.loose.bankingserver.web;

import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.service.FriendService;
import com.loose.bankingserver.web.dto.FriendDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @ApiOperation(value = "친구를 추가한다.")
    @PostMapping("/api/v1/friends")
    public ResponseEntity<Void> addFriend(HttpSession session, @RequestParam String friendName) throws MemberNotFoundException {
        String name = (String) session.getAttribute("name");
        friendService.addFriend(name, friendName);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "나의 친구 목록을 조회한다.")
    @GetMapping("/api/v1/friends")
    public ResponseEntity<List<FriendDto>> getFriends(HttpSession session) throws MemberNotFoundException {
        String name = (String) session.getAttribute("name");
        List<FriendDto> friends = friendService.getFriends(name);
        return ResponseEntity.ok().body(friends);
    }

}