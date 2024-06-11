package org.iot.hotelitybackend.sales.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class NoticeSpecification {

    // 공지코드
    public static Specification<NoticeEntity> equalsNoticeCodePk(Integer noticeCodePk) {
        return  (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("noticeCodePk"), noticeCodePk);
    }

    // 제목
    public static Specification<NoticeEntity> likeNoticeTitle(String noticeTitle) {
        String pattern = "%" + noticeTitle + "%";

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("noticeTitle"), pattern);
    }

    // 내용
    public static Specification<NoticeEntity> likeNoticeContent(String noticeContent) {
        String pattern = "%" + noticeContent + "%";

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("noticeContent"), pattern);
    }

    // 직원코드
    public static Specification<NoticeEntity> equalsEmployeeCodeFk(Integer employeeCodeFk) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("employeeCodeFk"), employeeCodeFk);
    }

    // 직원 이름
    public static Specification<NoticeEntity> likeEmployeeName(String employeeName) {
        String pattern = "%" + employeeName + "%";

        return  (root, query, criteriaBuilder) -> {
            Join<NoticeEntity, EmployeeEntity> employeeJoin = root.join("employee");

            return criteriaBuilder.like(employeeJoin.get("employeeName"), pattern);
        };
    }

    // 지점코드
    public static Specification<NoticeEntity> equalsBranchCodeFk(String branchCodeFk) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("branchCodeFk"), branchCodeFk);
    }

    // 등록일
    public static Specification<NoticeEntity> equalsNoticePostedDate(LocalDateTime noticePostedDate) {
        LocalDate date = noticePostedDate.toLocalDate();

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("noticePostedDate").as(LocalDate.class), date);
    }
    // 수정일
    public static Specification<NoticeEntity> equalsNoticeLastUpdatedDate(LocalDateTime noticeLastUpdatedDate) {
        LocalDate date = noticeLastUpdatedDate.toLocalDate();

        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("noticeLastUpdatedDate").as(LocalDate.class), date);
    }
}
