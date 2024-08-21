package ns.sos.domain.notice.service;

import ns.sos.domain.notice.model.dto.request.NoticeRequest;
import ns.sos.domain.notice.model.dto.response.NoticeResponse;
import ns.sos.domain.notice.model.dto.response.NoticeResponses;

public interface NoticeService {
    NoticeResponse createNotice(Integer memberId, NoticeRequest request);
    NoticeResponse updateNotice(Integer memberId, Integer noticeId, NoticeRequest request);
    void deleteNotice(Integer memberId, Integer noticeId);
    NoticeResponses getLatestNotices();
    NoticeResponses searchNotices(String keyword);
    NoticeResponse getNoticeById(Integer noticeId);
}