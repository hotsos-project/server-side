package ns.sos.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.member.model.dto.request.MemberChangeLocationRequest;
import ns.sos.domain.member.model.dto.request.MemberSearchRequest;
import ns.sos.domain.member.model.dto.request.MemberUpdateInfoRequest;
import ns.sos.domain.member.model.dto.request.MemberUpdatePasswordRequest;
import ns.sos.domain.member.model.dto.response.MemberInfoResponse;
import ns.sos.domain.member.model.dto.response.MemberSearchResponses;
import ns.sos.domain.member.service.MemberService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Member 관련 API (마이페이지 포함)")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "마이페이지 조회 API", description = "memberId로 member찾아옴")
    @SwaggerApiSuccess(description = "마이페이지 정보 조회")
    @GetMapping
    public Response<MemberInfoResponse> getMemberInfo(@Login Integer memberId){
        MemberInfoResponse memberInfoResponse = memberService.getMember(memberId);
        Response<MemberInfoResponse> success = Response.SUCCESS(memberInfoResponse);
        return success;
    }

    @Operation(summary = "비밀변경 변경 API", description = "")
    @SwaggerApiSuccess(description = "입력된 비밀번호와 현재 저장된 비밀번호 맞는지 확인 후 비밀번호 변경완료")
    @SwaggerApiError({ErrorCode.NOT_MATCH_PASSWORD})
    @PutMapping("/password")
    public Response<?> updateMemberPassword(@Login Integer memberId,
                                            @RequestBody final MemberUpdatePasswordRequest memberUpdatePasswordRequest){
        memberService.updateMemberPassword(memberUpdatePasswordRequest, memberId);
        return Response.SUCCESS("비밀번호 변경을 성공했습니다.");
    }

    @Operation(summary = "정보 수정 API", description = "")
    @SwaggerApiSuccess(description = "정보 수정")
    @PutMapping("/info")
    public Response<?> updateMemberInfo(@Login Integer memberId,
                                        @RequestBody MemberUpdateInfoRequest memberUpdateInfoRequest){
        memberService.updateMemberInfo(memberUpdateInfoRequest, memberId);
        return Response.SUCCESS();
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 처리, 실제 삭제가 아닌 status를 'N'으로 변경")
    @SwaggerApiSuccess(description = "회원 탈퇴 처리 완료")
    @SwaggerApiError(ErrorCode.ALREADY_WITHDRAWN_MEMBER)
    @DeleteMapping("/withdraw")
    public Response<?> withdrawMember(@Login Integer memberId) {
        memberService.withdrawMember(memberId);
        return Response.SUCCESS("회원 탈퇴가 완료되었습니다.");
    }

    @Operation(summary = "회원 조회 API", description = "검색 조건으로 회원을 검색합니다.")
    @SwaggerApiSuccess(description = "회원 조회 완료")
    @GetMapping("/search")
    public Response<MemberSearchResponses> searchMember(@RequestParam("searchKeyword") String searchKeyword) {
        MemberSearchResponses memberSearchResponses = memberService.searchMember(new MemberSearchRequest(searchKeyword));
        return Response.SUCCESS(memberSearchResponses,"회원 조회가 완료되었습니다.");
    }

    @Operation(summary = "회원 safe 설정 API", description = "회원이 안전한지 설정합니다.")
    @SwaggerApiSuccess(description = "회원 안전 설정 완료")
    @PostMapping("/safe")
    public Response<?> safeMember(@Login Integer memberId) {
        memberService.setSafe(memberId);
        return Response.SUCCESS("회원 안전 설정이 완료되었습니다.");
    }

    @Operation(summary = "회원 위치 변경 API", description = "회원의 실시간 위치를 변경합니다.")
    @SwaggerApiSuccess(description = "회원 위치 변경 완료")
    @PostMapping("/location")
    public Response<?> changeLocation(@RequestBody MemberChangeLocationRequest memberChangeLocationRequest,
                                      @Login Integer memberId) {
        memberService.changeLocation(memberChangeLocationRequest, memberId);
        return Response.SUCCESS("회원 위치 변경이 완료되었습니다.");
    }

}
