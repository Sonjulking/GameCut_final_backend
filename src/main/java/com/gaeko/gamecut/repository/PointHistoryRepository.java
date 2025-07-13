package com.gaeko.gamecut.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gaeko.gamecut.entity.PointHistory;
import com.gaeko.gamecut.entity.User;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {

    @Query(value = """
        SELECT ph.USER_NO, SUM(ph.POINT_AMOUNT) AS total_points
        FROM POINT_HISTORY ph
        WHERE ph.POINT_DATE >= DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 1 MONTH), '%Y-%m-01')
          AND ph.POINT_DATE < DATE_FORMAT(NOW(), '%Y-%m-01')
        GROUP BY ph.USER_NO
        ORDER BY total_points DESC
        """, nativeQuery = true)
    List<Object[]> findMonthlyRanking();
    
    
    
    
    @Query(value = """
    	    SELECT ph.USER_NO, SUM(ph.POINT_AMOUNT) AS total_points
    	    FROM POINT_HISTORY ph
    	    GROUP BY ph.USER_NO
    	    ORDER BY total_points DESC
    	    """, nativeQuery = true)
    	List<Object[]> findTotalRanking();
    	
    	
    	
    	List<PointHistory> findByUser(User user);

    	

}