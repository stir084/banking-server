package com.loose.bankingserver.web;

import com.loose.bankingserver.service.FriendService;
import com.loose.bankingserver.web.dto.FriendRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/friend-request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        friendService.sendFriendRequest(friendRequestDto);
        return ResponseEntity.ok("Friend request sent.");
    }

    @PostMapping("/friend-request/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        friendService.acceptFriendRequest(friendRequestDto.getFromName(), friendRequestDto.getToName());
        return ResponseEntity.ok("Friend request accepted.");
    }

   /* @PostMapping("/friend-request/reject")
    public ResponseEntity<String> rejectFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
       // friendService.rejectFriendRequest(friendRequestDto);
        return ResponseEntity.ok("Friend request rejected.");
    }*/

    /*@GetMapping("/friends/{username}")
    public ResponseEntity<List<String>> getFriends(@PathVariable String username) {
        //List<String> friends = friendService.getFriends(username);
      //  return ResponseEntity.ok(friends);
    }*/
}