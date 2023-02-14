package com.banana.visual.repository;

import com.banana.visual.entity.mysql.Chart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartRepository extends JpaRepository<Chart, Long> {
    @Query(nativeQuery = true, value = "select count(*) from chart")
    Long countChartNum();

    /**
     * 根据图表访问权限
     *
     * @param viewStatus 0-仅自己可见 1-部门内可见 2-公开
     */
    List<Chart> findAllByViewStatusOrderByCreateTimeDesc(Long viewStatus);

    /**
     * 根据图表创建者
     *
     * @param uid
     * @return
     */
    List<Chart> findAllByCreatorOrderByCreateTimeDesc(Long uid);

    @Query(nativeQuery = true, value = "SELECT a.cid,a.name,a.chart_des,a.creator,a.detail," +
            "a.power_bi_key,a.sql_text,a.view_status,a.update_time,a.create_time " +
            "from chart a " +
            "left join user b on b.uid = a.creator " +
            "left join job c on  c.jid = b.jid " +
            "left join department d on d.dep_id = c.dep_id " +
            "where d.dep_id =?1 and a.view_status != 0 order by create_time desc")
    List<Chart> findAllByDepId(Long depId);

    @Query(nativeQuery = true, value = "SELECT a.cid,a.name,a.chart_des,a.creator,a.detail,a.power_bi_key,a.sql_text,a.view_status,a.update_time,a.create_time " +
            "from chart a " +
            "left join user b on b.uid = a.creator " +
            "left join job c on  c.jid = b.jid " +
            "left join department d on d.dep_id = c.dep_id " +
            "where (d.dep_id = ?1 and a.view_status=1) or (a.view_status=2) order by rand() LIMIT 2")
    List<Chart> findPublicAndDepChartByRandom(Long depId);
}
