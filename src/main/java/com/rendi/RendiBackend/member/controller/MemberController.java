package com.rendi.RendiBackend.member.controller;

import com.rendi.RendiBackend.auth.domain.AuthInfo;
import com.rendi.RendiBackend.auth.dto.TokenDto;
import com.rendi.RendiBackend.auth.dto.TokenDtoCookie;
import com.rendi.RendiBackend.auth.service.AuthService;
import com.rendi.RendiBackend.common.dto.StringResponse;
import com.rendi.RendiBackend.member.domain.Provider;
import com.rendi.RendiBackend.member.dto.*;
import com.rendi.RendiBackend.member.exception.MemberErrorCode;
import com.rendi.RendiBackend.member.exception.MemberException;
import com.rendi.RendiBackend.member.service.EmailService;
import com.rendi.RendiBackend.member.service.MemberService;
//import com.rendi.RendiBackend.product.CrawlingService;
import com.rendi.RendiBackend.member.service.ProfileService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    private final EmailService emailService;
    private final ProfileService profileService;

//    private final CrawlingService crawlingService;


    private final AuthenticationManager authenticationManager;

    @PostMapping("/email")
    public StringResponse sendMail(@Valid @RequestBody EmailAuthRequest emailDto) throws MessagingException {
        String name = emailDto.getName();
        String email = emailDto.getEmail();
        String str = emailService.sendEmail(name, email);

        return new StringResponse("인증코드 메일을 전송했습니다. 인증코드: " + str);
    }


    @PostMapping("/find-id")
    public IdResponse findIdAndDate(@Valid @RequestBody EmailAuthRequest codeDto) {

        String[] result = emailService.findIdAndDate(codeDto.getEmail());
        String username = result[0];
        String createdAt = result[1];

        if(username != null){
            return new IdResponse(username, createdAt);
        }
        throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);

    }

    @PostMapping("/find-pw")
    public StringResponse findPw(@Valid @RequestBody NewLoginRequest dto) {
        emailService.updatePw(dto);
        return new StringResponse("비밀번호 재설정이 완료되었습니다.");

    }

    @PostMapping("/local")
    public TokenDto joinByLocalAccount(@Valid @RequestBody LocalJoinRequest request) throws IOException {
        AuthInfo info = memberService.join(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/id-check")
    public StringResponse checkUsername(@Valid @RequestBody IdDto id) {
        memberService.verifyDuplicatedId(id.getId());
        return new StringResponse("사용 가능한 아이디 입니다.");
    }
    @PostMapping("/email-check")
    public StringResponse checkEmail(@Valid @RequestBody EmailDto email){
        emailService.verifyDuplicatedEmail(email.getEmail());
        return new StringResponse("사용 가능한 이메일입니다");
    }

    @PostMapping("/login")
    public TokenDto signInByLocalAccount(@Valid @RequestBody LoginRequest loginRequest) {
        AuthInfo info = memberService.signIn(loginRequest);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/social-access")
    public StringResponse accessBySocialAccount(@Valid @RequestBody SocialLoginRequest request) {
        Provider.toProvider(request.getProvider());
        if (memberService.verifyMember(request))
            return new StringResponse("이미 가입된 회원입니다. 로그인을 위해 토큰 요청 필요합니다.");
        return new StringResponse("신규 회원입니다. 프로필 입력 진행해주세요.");
    }

    // 소셜로그인 접근 시 이미 가입된 회원일 때 토큰 반환
    @PostMapping("/social-token")
    public TokenDto signInBySocialAccount(@Valid @RequestBody SocialLoginRequest request) {
        Provider.toProvider(request.getProvider());
        AuthInfo info = memberService.signIn(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    // 소셜로그인 접근 시 신규 회원일 때 프로필 입력 후 토큰 반환
    @PostMapping("/social-profile")
    public TokenDto joinBySocialAccount(@Valid @RequestBody SocialJoinRequest request ) throws IOException {
        Provider.toProvider(request.getProvider());
        AuthInfo info = memberService.join(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }


//    @PostMapping("/reissue")
//    public TokenDto reissueToken(@Valid @RequestBody TokenDto tokenDto) {
//        AuthInfo info = authService.reissueTokenfromCookie(tokenDto);
//        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
//    }
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/reissue")
    public TokenDto reissueToken(@Valid @RequestBody TokenDtoCookie tokenDto, HttpServletRequest request) {
        AuthInfo info = authService.reissueTokenfromCookie(tokenDto, request);
        log.info("**tokenresponse** : {}",info.getAccessToken().getToken());
        log.info("**refresh** = {}", info.getMemberRefreshToken().getRefreshToken());
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @GetMapping("/information")
    public InfoResponse readProfile(){
        return profileService.showProfile();
    }
    @PutMapping("/information")
    public StringResponse updateProfile(@Valid @RequestBody ProfileUpdateRequest requestDto) {
        profileService.updateProfile(requestDto);
        return new StringResponse("회원정보 수정 완료되었습니다");
    }



}
