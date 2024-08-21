package ns.sos.domain.follow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ns.sos.domain.follow.model.dto.request.FollowNickNameRequest;
import ns.sos.domain.follow.model.dto.response.FollowPhoneResponse;
import ns.sos.domain.follow.model.dto.response.FollowResponse;
import ns.sos.domain.follow.model.dto.response.FollowResponses;
import ns.sos.domain.follow.service.FollowService;
import ns.sos.global.config.guard.Login;
import ns.sos.global.config.swagger.SwaggerApiError;
import ns.sos.global.config.swagger.SwaggerApiSuccess;
import ns.sos.global.error.ErrorCode;
import ns.sos.global.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "팔로우 관련 API (팔로우, 팔로워 조회)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우 API", description = "followerId로 팔로우 진행")
    @SwaggerApiSuccess(description = "팔로우 성공")
    @SwaggerApiError({ErrorCode.BAD_REQUEST})
    @PostMapping
    public Response<?> follow(@Login final Integer memberId, @RequestParam final Integer followerId) {
        followService.follow(memberId, followerId);
        return Response.SUCCESS();
    }

    @Operation(summary = "팔로워 조회 API", description = "팔로워 조회 진행")
    @SwaggerApiSuccess(description = "팔로워 조회 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED})
    @GetMapping("/followers")
    public Response<FollowResponses> getFollowers(@Login final Integer memberId) {
        List<FollowResponse> followers = followService.getFollowers(memberId);
        return Response.SUCCESS(FollowResponses.from(followers));
    }

    @Operation(summary = "팔로워 검색 API", description = "닉네임으로 팔로워 검색")
    @SwaggerApiSuccess(description = "팔로워 검색 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED})
    @GetMapping("/followers/search")
    public Response<FollowResponses> searchFollowers(@Login final Integer memberId, @RequestParam String nickName) {
        List<FollowResponse> followers = followService.searchFollowersByNickName(memberId, nickName);
        return Response.SUCCESS(FollowResponses.from(followers));
    }

    @Operation(summary = "팔로잉 조회 API", description = "팔로잉 조회 진행")
    @SwaggerApiSuccess(description = "팔로잉 조회 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED})
    @GetMapping("/followings")
    public Response<FollowResponses> getFollowings(@Login final Integer memberId) {
        List<FollowResponse> followers = followService.getFollowings(memberId);
        return Response.SUCCESS(FollowResponses.from(followers));
    }

    @Operation(summary = "팔로잉 검색 API", description = "닉네임으로 팔로잉 검색")
    @SwaggerApiSuccess(description = "팔로잉 검색 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED})
    @GetMapping("/followings/search")
    public Response<FollowResponses> searchFollowings(@Login final Integer memberId, @RequestParam String nickName) {
        List<FollowResponse> followings = followService.searchFollowingsByNickName(memberId, nickName);
        return Response.SUCCESS(FollowResponses.from(followings));
    }

    @Operation(summary = "팔로잉 닉네임 수정 API", description = "팔로잉 닉네임 수정 진행")
    @SwaggerApiSuccess(description = "팔로잉 닉네임 수정 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED, ErrorCode.BAD_REQUEST})
    @PostMapping("/nickname")
    public Response<FollowResponse> updateFollowNickName(@Login final Integer memberId, @RequestBody FollowNickNameRequest followNickNameRequest) {
        FollowResponse followResponse = followService.updateFollowNickName(memberId, followNickNameRequest);
        return Response.SUCCESS(followResponse);
    }

    @Operation(summary = "팔로잉 번호 조회 API", description = "팔로잉 번호 조회 진행")
    @SwaggerApiSuccess(description = "팔로잉 번호 조회 성공")
    @SwaggerApiError({ErrorCode.UNAUTHORIZED, ErrorCode.BAD_REQUEST})
    @GetMapping("/phone")
    public Response<FollowPhoneResponse> getFollowNumber(@Login final Integer memberId, @RequestParam final Integer followerId) {
        FollowPhoneResponse followResponse = followService.getFollowNumber(memberId, followerId);
        return Response.SUCCESS(followResponse);
    }
}
