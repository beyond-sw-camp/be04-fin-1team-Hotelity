package org.iot.hotelitybackend.filedownload.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file/download")
public class FileDownloadController {

	@Autowired
	private ResourceLoader resourceLoader;

	// src/main/resources/ 폴더에 있는 파일을 다운로드
	@GetMapping("/{filename}")
	public ResponseEntity fileDownload(@PathVariable("filename") String filename) throws IOException {
		Resource resource = resourceLoader.getResource("classpath:" + filename);
		File file = resource.getFile();
		String fileLen = file.length() + "";

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
			.header(HttpHeaders.CONTENT_LENGTH, fileLen)
			.body(resource);
	}
}
