package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.NoticeDTO;

import java.util.Map;

public interface NoticeService {
    Map<String, Object> selectNoticesList(int pageNum);

    NoticeDTO selectNoticeByNoticeCodePk(int noticeCodePk);
}
