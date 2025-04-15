package com.document.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.document.request.dto.DocumentListRequestDto;
import com.document.response.dto.DocumentListResponseDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class DocumentManagementRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	public Page<DocumentListResponseDto> getDocumentList(DocumentListRequestDto reqDto) {
		Pageable pageable = PageRequest.of(reqDto.getPageNumber(), reqDto.getPageSize());
		int startIndex = pageable.getPageNumber() * pageable.getPageSize();

		StringBuilder selectQuery = new StringBuilder(500);
		selectQuery.append("SELECT NEW com.document.response.dto.DocumentListResponseDto("
				+ "dm.docId,dm.docName,dm.docSize,dm.contentType,dm.createdOn,"
				+ "dm.updatedOn,dm.createdBy,dm.updatedBy,dm.author) "
				+ "From DocumentManagementEO dm ");

		selectQuery.trimToSize();
		String whereQuery = "where dm.createdBy is not null ";

		if (reqDto.getAuthor() != null && !reqDto.getAuthor().isEmpty())
			whereQuery += "and dm.author LIKE '%" + reqDto.getAuthor().trim() + "%' ";

		if (reqDto.getType() != null && !reqDto.getType().isEmpty())
			whereQuery += "and dm.contentType LIKE '%" + reqDto.getType().trim() + "%' ";

		if (reqDto.getUserId() != null && reqDto.getUserId() != 0)
			whereQuery += "and dm.createdBy =" + reqDto.getUserId() + " ";

		whereQuery += " and date(dm.createdOn) BETWEEN COALESCE(date(:fromDate),date(dm.createdOn)) "
				+ "and COALESCE(date(:toDate),date(dm.createdOn)) ";

		String mainQryStr = selectQuery + whereQuery + " order by dm.createdOn desc ";

		Query mainQry = entityManager.createQuery(mainQryStr, DocumentListResponseDto.class).setFirstResult(startIndex)
				.setMaxResults(pageable.getPageSize());
		String counSelectStr = "select COUNT(dm.docId) as count FROM DocumentManagementEO dm ";
		String countQryStr = counSelectStr + whereQuery;
		final Query countQry = entityManager.createQuery(countQryStr);

		mainQry.setParameter("fromDate", reqDto.getFromDate());
		countQry.setParameter("fromDate", reqDto.getFromDate());

		mainQry.setParameter("toDate", reqDto.getToDate());
		countQry.setParameter("toDate", reqDto.getToDate());

		@SuppressWarnings("unchecked")
		final List<DocumentListResponseDto> resultList = mainQry.getResultList();
		@SuppressWarnings("unchecked")
		final List<Long> countList = countQry.getResultList();
		int count = 0;
		if (!countList.isEmpty()) {
			count = countList.get(0).intValue();
		}
		return new PageImpl<DocumentListResponseDto>(resultList, pageable, count);
	}

}
