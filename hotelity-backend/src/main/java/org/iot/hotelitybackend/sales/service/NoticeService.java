package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.NoticeDTO;
import org.iot.hotelitybackend.sales.vo.NoticeSearchCriteria;
import org.iot.hotelitybackend.sales.vo.RequestModifyNotice;
import org.iot.hotelitybackend.sales.vo.RequestNotice;

import java.time.LocalDateTime;
import java.util.Map;

public interface NoticeService {
    Map<String, Object> selectNoticesList(NoticeSearchCriteria criteria);

    NoticeDTO selectNoticeByNoticeCodePk(int noticeCodePk);

    Map<String, Object> registNotice(RequestNotice requestNotice);

    Map<String, Object> modifyNotice(RequestModifyNotice requestModifyNotice, int noticeCodePk);

    Map<String, Object> deleteNotice(int noticeCodePk);

	Map<String, Object> selectLatestNoticeList();
}
