package org.iot.hotelitybackend.sales.service;

import java.util.Map;

public interface VocService {
    Map<String, Object> selectVocsList(int pageNum);
}
