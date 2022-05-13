package com.practice.repository;

import com.practice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT COUNT(T) FROM Task T WHERE T.project.projectCode = ?1 AND T.taskStatus <> 'Completed'")
    int totalNonCompletedTask(String projectCode);

    @Query(value = "SELECT COUNT(*)" +
            "FROM Task t join Project p on t.id =p.id " +
            "WHERE P.projectCode= ?1 AND t.taskStatus = 'COMPLETE'", nativeQuery = true)
    int totalCompletedTask(String projectCode);
}
