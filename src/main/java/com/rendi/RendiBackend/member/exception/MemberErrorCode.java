package com.rendi.RendiBackend.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MemberErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    PROFILE_NOT_FOUND("프로필을 찾을 수 없습니다."),
    MEMBER_CODE_NOT_FOUND("인증코드를 찾을 수 없습니다."),
    MEMBER_DUPLICATED("이미 가입된 회원입니다."),
    EMAIL_DUPLICATED("이미 가입된 email 입니다"),
    MEMBER_PROFILE_BAD_REQUEST("img가 null일 수 없습니다."),

    ACCOUNT_IN_SOCIAL("일반 회원가입으로 가입한 계정이 아닙니다."),
    INTEREST_NOT_FOUND("INTEREST를 profile id로 찾을수 없습니다"),
    MEMBER_PW_UPDATE_FAILED("비밀번호 재설정에 실패하였습니다.");


    private String defaultMessage;
}
