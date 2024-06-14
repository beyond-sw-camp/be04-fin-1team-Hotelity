package org.iot.hotelitybackend.filedownload.controller;

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
	public ResponseEntity<Resource> fileDownload(@PathVariable("filename") String filename) throws IOException {
		Resource resource = resourceLoader.getResource("classpath:" + filename);

		if (!resource.exists()) {
			return ResponseEntity.notFound().build();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

		return ResponseEntity.ok()
			.headers(headers)
			.body(resource);

	}
}
