package com.example.gym_tracker.repository;

import com.example.gym_tracker.model.CrowdLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CrowdLogRepository extends JpaRepository<CrowdLog, Long> {
    List<CrowdLog> findByDayOfWeek(String dayOfWeek);

    @Query("SELECT c.timeSlot AS timeSlot, AVG(c.crowdLevel) AS avgCrowd " +
            "FROM CrowdLog c " +
            "WHERE LOWER(c.dayOfWeek) = LOWER(:dayOfWeek) " +
            "GROUP BY c.timeSlot " +
            "ORDER BY AVG(c.crowdLevel) ASC")
    List<Object[]> findOptimalTimeSlots(@Param("dayOfWeek") String dayOfWeek);
}