package org.iot.hotelitybackend.sales.service;

import org.iot.hotelitybackend.sales.dto.VocDTO;
import org.iot.hotelitybackend.sales.vo.RequestReplyVoc;
import org.iot.hotelitybackend.sales.vo.VocSearchCriteria;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VocService {
    Map<String, Object> selectVocsList(
		// int pageNum, Integer vocCodePk, String vocTitle, String vocCategory,
		// Integer customerCodeFk, String customerName, LocalDateTime vocCreatedDate, LocalDateTime vocLastUpdatedDate, String branchCodeFk,
		// Integer employeeCodeFk, String PICEmployeeName, Integer vocProcessStatus, String orderBy, Integer sortBy
		VocSearchCriteria criteria
	);

    VocDTO selectVocByVocCodePk(int vocCodePk);

    Map<String, Object> replyVoc(RequestReplyVoc requestReplyVoc, int vocCodePk);

    // Map<String, Object> selectSearchedVocsList(int pageNum, String branchCodeFk, Integer vocProcessStatus, String vocCategory, Date vocCreatedDate, Integer customerCodeFk);

	List<VocDTO> selectVocsListForExcel();

	Map<String, Object> createVocsExcelFile(List<VocDTO> vocDTOList) throws
		IOException,
		NoSuchFieldException,
		IllegalAccessException;

	Map<String, Object> deleteVoc(int vocCodePk);

	Map<String, Object> selectLatestVocList();
}
