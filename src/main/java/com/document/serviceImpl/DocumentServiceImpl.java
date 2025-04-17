package com.document.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.document.entity.DocumentManagementEO;
import com.document.repository.DocumentManagementRepository;
import com.document.repository.DocumentManagementRepositoryImpl;
import com.document.request.dto.DocumentListRequestDto;
import com.document.request.dto.DocumentUploadRequestDto;
import com.document.response.dto.DocumentListResponseDto;
import com.document.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentManagementRepository documentManagementRepository;

	@Autowired
	private DocumentManagementRepositoryImpl documentManagementRepositoryImpl;

	@Value("${file.upload-document-dir}")
	public String docLocation;

	private static Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Override
	public String uploadDocument(DocumentUploadRequestDto reqDto) {
		String status = "Failure";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String formatDateTime = LocalDateTime.now().format(dtf);
		String localFile = docLocation;
		File dir = new File(localFile);
		MultipartFile file = reqDto.getFile();
		DocumentManagementEO docObj = null;
		if (reqDto.getDocId() != null)
			docObj = documentManagementRepository.findById(reqDto.getDocId()).get();
		if (docObj != null) {
			try {
				String oldFileLocation = docLocation + docObj.getDocName();
				File oldFile = new File(oldFileLocation);
				if (oldFile.exists())
					oldFile.delete();
				if (file != null && !file.isEmpty()) {
					try {
						if (dir.exists()) {
							FileCopyUtils.copy(file.getBytes(),
									new File(localFile + formatDateTime + "_" + file.getOriginalFilename()));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					docObj.setContentType(file.getContentType());
					docObj.setUpdatedOn(LocalDateTime.now());
					docObj.setUpdatedBy(reqDto.getUserId());
					docObj.setAuthor(reqDto.getAuthor());
					docObj.setDocName(formatDateTime + "_" + file.getOriginalFilename());
					docObj.setDocSize(file.getSize());
					documentManagementRepository.save(docObj);
				}
				status = "Success";
			} catch (Exception e) {
				logger.error("document updated method caused error at {}: {}", reqDto, e.getMessage());
			}
		} else {
			try {
				docObj = new DocumentManagementEO();
				if (file != null && !file.isEmpty()) {
					try {
						if (dir.exists()) {
							FileCopyUtils.copy(file.getBytes(),
									new File(localFile + formatDateTime + "_" + file.getOriginalFilename()));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					docObj.setContentType(file.getContentType());
					docObj.setCreatedOn(LocalDateTime.now());
					docObj.setCreatedBy(reqDto.getUserId());
					docObj.setAuthor(reqDto.getAuthor());
					docObj.setDocName(formatDateTime + "_" + file.getOriginalFilename());
					docObj.setDocSize(file.getSize());
					documentManagementRepository.save(docObj);
				}
				status = "Success";
			} catch (Exception e) {
				logger.error("document uploaded method caused error at {}: {}", reqDto, e.getMessage());
			}
		}
		return status;
	}

	@Override
	public Resource downloadDocument(Integer docId) {
		try {
			String docName = findFileNameById(docId);
			if (docName != null && !docName.isEmpty() && !docName.isBlank()) {
				String path = docLocation;
				File file = new File(path);
				Path dirPath = null;
				if (file.exists())
					dirPath = Paths.get(docLocation + docName);
				else
					logger.warn("Location not found!");
				return new UrlResource(dirPath.toUri());
			} else
				logger.info("Document not found");
		} catch (MalformedURLException e) {
			logger.error("error caused in download document", e.getMessage());
		}
		return null;
	}

	private String findFileNameById(int docId) {
		DocumentManagementEO docObj = documentManagementRepository.findById(docId).get();
		return docObj.getDocName();
	}

	@Override
	public Page<DocumentListResponseDto> getDocumentList(DocumentListRequestDto reqDto) {
		Page<DocumentListResponseDto> list = documentManagementRepositoryImpl.getDocumentList(reqDto);
		return list;
	}
}
