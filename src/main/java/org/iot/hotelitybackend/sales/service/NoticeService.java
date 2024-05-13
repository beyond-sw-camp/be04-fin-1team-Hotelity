package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.NoticeDTO;
import org.iot.hotelitybackend.sales.vo.RequestNotice;

import java.util.Map;

public interface NoticeService {
    Map<String, Object> selectNoticesList(int pageNum);

    NoticeDTO selectNoticeByNoticeCodePk(int noticeCodePk);

    Map<String, Object> registNotice(RequestNotice requestNotice);
}
