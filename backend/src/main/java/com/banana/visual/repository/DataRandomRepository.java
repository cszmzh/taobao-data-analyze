package com.banana.visual.repository;

import com.banana.visual.entity.mysql.DataRandom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRandomRepository extends JpaRepository<DataRandom, Long> {
    @Query(nativeQuery = true, value = "select count(*) from data_random")
    Long countDataRandomNum();
}
