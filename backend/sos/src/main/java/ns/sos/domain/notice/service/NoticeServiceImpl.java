package ns.sos.domain.notice.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.member.repository.MemberRepository;
import ns.sos.domain.notice.model.Notice;
import ns.sos.domain.notice.model.dto.request.NoticeRequest;
import ns.sos.domain.notice.model.dto.response.NoticeResponse;
import ns.sos.domain.notice.model.dto.response.NoticeResponses;
import ns.sos.domain.notice.repository.NoticeRepository;
import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Override
    public NoticeResponse createNotice(Integer memberId, NoticeRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_MEMBER));

        Notice notice = new Notice(
                member,
                request.getTitle(),
                request.getContent(),
                'Y'
        );
        Notice savedNotice = noticeRepository.save(notice);
        return NoticeResponse.from(savedNotice);
    }

    @Override
    public NoticeResponse updateNotice(Integer memberId, Integer noticeId, NoticeRequest request) {
        Notice notice = noticeRepository.findByIdAndMemberId(noticeId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA, "Notice not found"));

        notice.updateNotice(request.getTitle(), request.getContent());
        Notice updatedNotice = noticeRepository.save(notice);
        return NoticeResponse.from(updatedNotice);
    }

    @Override
    public void deleteNotice(Integer memberId, Integer noticeId) {
        Notice notice = noticeRepository.findByIdAndMemberId(noticeId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA, "Notice not found"));
        noticeRepository.delete(notice);
    }

    @Override
    public NoticeResponses getLatestNotices() {
        List<Notice> notices = noticeRepository.findTop10ByOrderByIdDesc();
        List<NoticeResponse> noticeResponses = notices.stream()
                .map(NoticeResponse::from)
                .collect(Collectors.toList());
        return NoticeResponses.from(noticeResponses);
    }

    @Override
    public NoticeResponses searchNotices(String keyword) {
        List<Notice> notices = noticeRepository.findByTitleContaining(keyword);
        List<NoticeResponse> noticeResponses = notices.stream()
                .map(NoticeResponse::from)
                .toList();
        return NoticeResponses.from(noticeResponses);
    }

    @Override
    public NoticeResponse getNoticeById(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA, "Notice not found"));
        return NoticeResponse.from(notice);
    }
}