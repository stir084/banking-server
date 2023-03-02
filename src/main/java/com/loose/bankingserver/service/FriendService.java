package com.loose.bankingserver.service;

import com.loose.bankingserver.exception.FriendAlreadyExistsException;
import com.loose.bankingserver.exception.MemberNotFoundException;
import com.loose.bankingserver.model.Friend;
import com.loose.bankingserver.model.Member;
import com.loose.bankingserver.repository.FriendRepository;
import com.loose.bankingserver.repository.MemberRepository;
import com.loose.bankingserver.web.dto.FriendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FriendService {
    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;

    @Transactional
    public void addFriend(String username, String friendName) throws MemberNotFoundException {
        Member me = memberRepository.findByName(username)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
        Member you = memberRepository.findByName(friendName)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));


        if (me.getFriends().stream().anyMatch(friend -> friend.getFriend().equals(you))) {
            throw new FriendAlreadyExistsException("이미 친구입니다.");
        }

        Friend newFriend = Friend.createFriend(me, you);
        friendRepository.save(newFriend);

    }
    @Transactional
    public List<FriendDto> getFriends(String name) throws MemberNotFoundException {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));

        List<Friend> friends = member.getFriends();

        return  FriendDto.toDto(friends);
    }
}