package com.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.document.entity.DocumentManagementEO;

@Repository
public interface DocumentManagementRepository extends JpaRepository<DocumentManagementEO, Integer> {

}
