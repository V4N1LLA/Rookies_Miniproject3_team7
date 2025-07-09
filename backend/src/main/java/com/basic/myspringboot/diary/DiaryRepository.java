package com.basic.myspringboot.diary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DiaryRepository extends CrudRepository<Diary, Long> {

    @Query("SELECT d FROM Diary d LEFT JOIN FETCH d.analysisResult")
    List<Diary> findAllWithAnalysis();
}
