package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.VocDTO;

import java.util.Map;

public interface VocService {
    Map<String, Object> selectVocsList(int pageNum);

    VocDTO selectVocByVocCodePk(int vocCodePk);
}
