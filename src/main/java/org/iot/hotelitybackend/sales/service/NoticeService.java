package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.NoticeDTO;
import org.iot.hotelitybackend.sales.vo.RequestModifyNotice;
import org.iot.hotelitybackend.sales.vo.RequestNotice;

import java.time.LocalDateTime;
import java.util.Map;

public interface NoticeService {
    Map<String, Object> selectNoticesList(
        int pageNum, Integer noticeCodePk, String noticeTitle, String noticeContent,
        Integer employeeCodeFk, String employeeName, String branchCodeFk,
        LocalDateTime noticePostedDate, LocalDateTime noticeLastUpdatedDate,
        String orderBy, Integer sortBy);

    NoticeDTO selectNoticeByNoticeCodePk(int noticeCodePk);

    Map<String, Object> registNotice(RequestNotice requestNotice);

    Map<String, Object> modifyNotice(RequestModifyNotice requestModifyNotice, int noticeCodePk);

    Map<String, Object> deleteNotice(int noticeCodePk);

	Map<String, Object> selectLatestNoticeList();
}
