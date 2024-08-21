package ns.sos.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     * Error Code 400 : 잘못된 요청 401 : 권한 오류 403 : 서버가 허용하지 않은 웹페이지, 미디어 요청 404 : 존재하지 않는 정보에 대한 요청
     */

    //common
    NOT_FOUND_DATA(404, "C001", "해당하는 데이터를 찾을 수 없습니다."),
    BAD_REQUEST(400, "C002", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 에러입니다"),
    UNAUTHORIZED(403, "C004", "권한이 없습니다."),

    //jwt token
    INVALID_JWT_TOKEN(400, "J001", "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_JWT_TOKEN(400, "J002", "만료된 access 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(400, "J003", "지원되지 않는 JWT 토큰입니다."),
    ILLEGAL_JWT_TOKEN(400, "J004", "잘못된 JWT 토큰입니다."),
    NULL_JWT_TOKEN(400, "J005", "JWT 토큰 값이 넘어오지 않았습니다."),
    INVALID_BEARER_JWT_TOKEN(400, "J006", "토큰이 Bearer로 시작하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(400, "J007", "만료된 refresh 토큰입니다."),

    //member
    INVALID_TOKEN(400, "M001", "유효하지 않은 토큰입니다."),
    NOT_EXIST_MEMBER(400, "M002", "존재하지 않는 회원입니다."),
    DUPLICATED_MEMBER(400, "M003", "이미 존재하는 회원입니다."),
    NOT_MATCH_PASSWORD(400, "M004", "비밀번호가 일치하지 않습니다."),
    ALREADY_WITHDRAWN_MEMBER(400, "M005","이미 탈퇴 처리된 회원입니다."),

    //region
    NOT_EXIST_SIDO(400, "R001", "존재하지 않는 시도입니다."),
    NOT_EXIST_GUGUN(400, "R002", "존재하지 않는 구군입니다."),

    //board
    DUPLICATED_BOARDBUTTON(400, "B001", "이미 게시판버튼이 존재합니다."),
    BOARD_NOT_FOUND(404, "B002", "게시판을 찾을 수 없습니다."),
    DUPLICATED_BOARD(400, "B003", "이미 존재하는 게시판입니다."),
    INVALID_BOARD_REQUEST(400, "B004", "잘못된 게시판 요청입니다."),
    UNAUTHORIZED_BOARD_ACCESS(403, "B005", "게시판에 접근할 권한이 없습니다."),

    //comment
    COMMENT_NOT_FOUND(404, "CM001", "댓글을 찾을 수 없습니다."),
    DUPLICATED_COMMENT(400, "CM002", "이미 존재하는 댓글입니다."),
    INVALID_COMMENT_REQUEST(400, "CM003", "잘못된 댓글 요청입니다."),
    UNAUTHORIZED_COMMENT_ACCESS(403, "CM004", "댓글에 접근할 권한이 없습니다."),

    //reply
    REPLY_NOT_FOUND(404, "R001", "답글을 찾을 수 없습니다."),
    DUPLICATED_REPLY(400, "R002", "이미 존재하는 답글입니다."),
    INVALID_REPLY_REQUEST(400, "R003", "잘못된 답글 요청입니다."),
    UNAUTHORIZED_REPLY_ACCESS(403, "R004", "답글에 접근할 권한이 없습니다."),

    //notice
    NOTICE_NOT_FOUND(404, "N001", "공지사항을 찾을 수 없습니다."),
    INVALID_NOTICE_REQUEST(400, "N002", "잘못된 공지사항 요청입니다."),
    UNAUTHORIZED_NOTICE_ACCESS(403, "N003", "공지사항에 접근할 권한이 없습니다."),

    //image
    EMPTY_FILE_EXCEPTION(400, "I001", "이미지가 없습니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(500, "I1002", "파일업로드가 잘못되었습니다."),

    //follow
    FOLLOW_BAD_REQUEST(400, "F001", "자기자신은 팔로우할 수 없습니다."),
    FOLLOW_NOT_MUTUAL(400, "F002", "맞팔로우가 아닙니다."),
    FOLLOW_NOT_FOUND(404, "F003", "팔로우 정보를 찾을 수 없습니다."),
    FOLLOW_OVER_SIZE(400, "F004", "팔로우는 최대 10명만 가능합니다."),

    //sms
    INVALID_CERTIFICATION_NUMBER(400, "S001", "올바르지 못한 인증번호입니다."),
    INVALID_PHONE_NUMBER(400, "S002", "올바르지 못한 전화번호입니다."),
    PHONE_NOT_CERTIFIED(400, "S003", "전화번호 인증이 되지 않았습니다."),
    
    //favorite region
    FAVORITE_OVER_SIZE(400, "FR001" , "관심 지역은 1개만 추가 가능합니다."),

    //disaster
    NOT_EXIST_DISASTER(400, "D001", "존재하지 않는 재난문자입니다.");

    private final Integer status;
    private final String code;
    private final String message;
}
