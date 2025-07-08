package com.basic.myspringboot.analysis.repository;

import com.basic.myspringboot.analysis.entity.AnalysisVector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisVectorRepository extends JpaRepository<AnalysisVector, Long> {
}
