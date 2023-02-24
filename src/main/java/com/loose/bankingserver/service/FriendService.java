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
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional
    public void sendFriendRequest(FriendRequestDto friendRequestDto) {
        Member requester = memberRepository.findByName(friendRequestDto.getFromName())
                .orElseThrow(() -> new MemberNotFoundException(friendRequestDto.getFromName()));
        Member requested = memberRepository.findByName(friendRequestDto.getToName())
                .orElseThrow(() -> new MemberNotFoundException(friendRequestDto.getToName()));

        if (requested.hasFriend(requester)) {
            throw new FriendAlreadyExistsException("이미 친구");
        }

        /*if (friendRequestRepository.findByRequesterAndRequested(requester, requested).isPresent()) {
            throw new FriendRequestAlreadyExistsException(friendRequestDto.getFromName(), friendRequestDto.getToName());
        }*/

        FriendRequest friendRequest = new FriendRequest(requester, requested, FriendRequestStatus.PENDING);
        friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void acceptFriendRequest(String fromName, String toName) {


        Member from = memberRepository.findByName(fromName).orElseThrow(() -> new MemberNotFoundException("멤버찾기불가"));
        Member to = memberRepository.findByName(toName).orElseThrow(() -> new MemberNotFoundException("멤버찾기불가"));

        FriendRequest request = (FriendRequest) friendRequestRepository.findByRequesterAndRequested(from, to)
                .orElseThrow(() -> new FriendRequestNotFoundException("요청된거 없음"));

        if (request.getStatus() == FriendRequestStatus.ACCEPTED) {
            throw new FriendAlreadyExistsException("이미 친구");
        }

        request.setStatus(FriendRequestStatus.ACCEPTED);
        Friend friend = new Friend(from, to);
        from.addFriend(friend);
        to.addFriend(friend);
        friendRequestRepository.delete(request);
    }
   /* public List<MemberDto> getFriends(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id " + memberId));

        List<Friend> friends = friendRepository.findAllByFromOrToAndStatus(member, member, FriendStatus.ACCEPTED);
        List<MemberDto> friendDtos = friends.stream()
                .map(friend -> friend.getFrom().equals(member) ? friend.getTo() : friend.getFrom())
                .map(memberMapper::toDto)
                .collect(Collectors.toList());

        return friendDtos;
    }*/
}