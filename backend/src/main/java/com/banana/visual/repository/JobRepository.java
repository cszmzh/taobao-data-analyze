package com.banana.visual.repository;

import com.banana.visual.entity.mysql.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query(nativeQuery = true, value = "select count(*) from job")
    Long countJobNum();

    List<Job> findAllByDepId(Long depId);

    // 更新主键
    @Modifying
    @Query(nativeQuery = true, value = "update job set jid=?1 where jid = ?2")
    void updateJid(Long newJid, Long oldJid);
}
