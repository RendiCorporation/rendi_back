package com.rendi.RendiBackend.member.service;

import com.rendi.RendiBackend.auth.domain.MemberRefreshToken;
import com.rendi.RendiBackend.auth.dto.TokenRequestDto;
import com.rendi.RendiBackend.auth.exception.AuthErrorCode;
import com.rendi.RendiBackend.auth.exception.AuthException;
import com.rendi.RendiBackend.repositories.RefreshTokenRepository;
import com.rendi.RendiBackend.member.domain.Interest;
import com.rendi.RendiBackend.member.domain.Member;
import com.rendi.RendiBackend.member.domain.Profile;
import com.rendi.RendiBackend.member.dto.InfoResponse;
import com.rendi.RendiBackend.member.dto.ProfileSaveRequest;
import com.rendi.RendiBackend.member.dto.ProfileUpdateRequest;
import com.rendi.RendiBackend.member.exception.MemberErrorCode;
import com.rendi.RendiBackend.member.exception.MemberException;
import com.rendi.RendiBackend.repositories.InterestRepository;
import com.rendi.RendiBackend.repositories.MemberRepository;
import com.rendi.RendiBackend.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final InterestRepository interestRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Long saveProfile(ProfileSaveRequest request, Member member) {
        Profile profile = request.toProfile();
        String url = null;
        saveInterests(request.getInterests(), profile);
        profile.setMember(member);
        return profileRepository.save(profile).getId();
    }
    @Transactional(readOnly = true)
    public InfoResponse showProfile(){
        Member member = findCurrentMember();
        Profile profile = profileRepository.findById(member.getId())
                .orElseThrow(()-> new MemberException(MemberErrorCode.PROFILE_NOT_FOUND));
        List<String> interests = profile.getInterests().stream()
                .map(Interest::getField)
                .collect(Collectors.toList());
        return new InfoResponse(profile.getEmail(), profile.getNickname(), member.getUsername(),
                profile.getBirth(), profile.getPhonenum(), interests);

    }
    @Transactional
    public void updateProfile(ProfileUpdateRequest request){
        Member member = findCurrentMember();
        Profile profile = profileRepository.findById(member.getId())
                        .orElseThrow(()-> new MemberException(MemberErrorCode.PROFILE_NOT_FOUND));
        profile.updateProfile(request.getEmail(), request.getNickname(), request.getBirth(), request.getPhonenum());
    }


    private void saveInterests(List<String> requestList, Profile profile) {
        for (String interest : requestList) {
            Interest select = interestRepository.save(new Interest(profile, interest));
            profile.getInterests().add(select);
        }
    }

    private Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return user;
    }

    @Transactional
    public void deleteRefreshToken(TokenRequestDto tokenRequestDto) {
        String memberId = findCurrentMember().getUsername();
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsernameAndRefreshToken(memberId, tokenRequestDto.getRefreshToken());
        if (memberRefreshToken == null) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN, "가입되지 않은 회원이거나 유효하지 않은 리프레시 토큰입니다.");
        }
        refreshTokenRepository.deleteById(memberRefreshToken.getRefreshTokenId());
    }

}
