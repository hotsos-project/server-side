package ns.sos.domain.board.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ns.sos.domain.member.model.dto.Member;
import ns.sos.domain.region.gugun.model.Gugun;
import ns.sos.domain.region.sido.model.Sido;
import ns.sos.global.common.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Version
    private Integer version;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer commentNum;

    @Column(nullable = false)
    private char status = 'Y';

    @Column(nullable = false)
    private Integer factCnt;

    @Column(nullable = false)
    private Integer reportCnt;

    @Column(nullable = false)
    private Boolean isAlarm = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id", nullable = false)
    private Sido sido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gugun_id", nullable = false)
    private Gugun gugun;

    @Column(length = 300, nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer count;

    @PrePersist
    public void prePersist() {
        if (this.version == null) {
            this.version = 0;
        }
    }

    public Board(final Member member,
                 final String title,
                 final String content,
                 final Sido sido,
                 final Gugun gugun,
                 final String address) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.commentNum = 0;
        this.factCnt = 0;
        this.reportCnt = 0;
        this.isAlarm = false;
        this.sido = sido;
        this.gugun = gugun;
        this.address = address;
        this.count = 0;
    }

    @Builder
    public Board(final Member member,
                 final String title,
                 final String content,
                 final Integer commentNum,
                 final char status,
                 final Integer factCnt,
                 final Integer reportCnt,
                 final Boolean isAlarm,
                 final Sido sido,
                 final Gugun gugun,
                 final String address,
                 final Integer count) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.commentNum = commentNum;
        this.status = status;
        this.factCnt = factCnt;
        this.reportCnt = reportCnt;
        this.isAlarm = isAlarm;
        this.sido = sido;
        this.gugun = gugun;
        this.address = address;
        this.count = count;
    }

    public void updateBoard(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    public void setAlarm(final Boolean isAlarm) {
        this.isAlarm = true;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public void addFactCnt() {
        this.factCnt += 1;
    }

    public void addReportCnt() {
        this.reportCnt += 1;
    }

    public void minusFactCnt() {
        this.factCnt -= 1;
    }

    public void minusReportCnt() {
        this.reportCnt -= 1;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void incrementCount() {
        this.count += 1;
    }

    public void incrementCommentNum() {
        this.commentNum += 1;
    }

    public void decrementCommentNum() {
        this.commentNum -= 1;
        if (commentNum < 0) {
            commentNum = 0;
        }
    }
}
