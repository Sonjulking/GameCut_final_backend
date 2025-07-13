package com.gaeko.gamecut.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gaeko.gamecut.entity.PointHistory;
import com.gaeko.gamecut.entity.User;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {

	@Query(value = """
        SELECT ph.user_no, SUM(ph.point_amount) AS total_points
        FROM point_history ph
        WHERE ph.point_date >= DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 1 MONTH), '%Y-%m-01')
          AND ph.point_date < DATE_FORMAT(NOW(), '%Y-%m-01')
        GROUP BY ph.user_no
        ORDER BY total_points DESC
        """, nativeQuery = true)
	List<Object[]> findMonthlyRanking();




	@Query(value = """
    	    SELECT ph.user_no, SUM(ph.point_amount) AS total_points
    	    FROM point_history ph
    	    GROUP BY ph.user_no
    	    ORDER BY total_points DESC
    	    """, nativeQuery = true)
	List<Object[]> findTotalRanking();



	List<PointHistory> findByUser(User user);



}