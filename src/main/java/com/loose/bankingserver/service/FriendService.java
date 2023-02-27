package com.loose.bankingserver.service;

import com.loose.bankingserver.exception.FriendAlreadyExistsException;
import com.loose.bankingserver.exception.FriendRequestNotFoundException;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Friend;
import com.loose.bankingserver.model.FriendRequest;
import com.loose.bankingserver.model.FriendRequestStatus;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.FriendRepository;
import com.loose.bankingserver.repository.FriendRequestRepository;
import com.loose.bankingserver.repository.MemberRepository;
import com.loose.bankingserver.web.dto.FriendDto;
import com.loose.bankingserver.web.dto.FriendRequestDto;
import com.loose.bankingserver.web.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    @Transactional
    public void addFriend(String username, String friendName) throws MemberNotFoundException {
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));
        Member friend = memberRepository.findByName(friendName)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));

        Friend newFriend = new Friend();
        newFriend.setMember(member);
        newFriend.setFriend(friend);
        member.getFriends().add(newFriend);
        memberRepository.save(member);
    }
    @Transactional
    public List<FriendDto> getFriends(String name) throws MemberNotFoundException {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException("Member not found."));
        List<FriendDto> friendDtos = new ArrayList<>();
        List<Friend> friends = member.getFriends();
        for (Friend friend : friends) {
            FriendDto friendDto = new FriendDto();
            friendDto.setUsername(friend.getFriend().getName());
            friendDtos.add(friendDto);
        }
        return friendDtos;
    }
}