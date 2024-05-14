package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.VocDTO;
import org.iot.hotelitybackend.sales.vo.RequestReplyVoc;

import java.util.Map;

public interface VocService {
    Map<String, Object> selectVocsList(int pageNum);

    VocDTO selectVocByVocCodePk(int vocCodePk);

    Map<String, Object> replyVoc(RequestReplyVoc requestReplyVoc, int vocCodePk);
}
