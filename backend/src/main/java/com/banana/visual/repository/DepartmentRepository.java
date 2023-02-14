package com.banana.visual.repository;

import com.banana.visual.entity.mysql.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query(nativeQuery = true, value = "select count(*) from department")
    Long countDeptNum();

    @Query(nativeQuery = true, value = "SELECT a.dep_name " +
            "from department a " +
            "left join job b on b.dep_id = a.dep_id " +
            "where b.jid = ?1")
    String getDeptNameByJobId(Long jid);
}
