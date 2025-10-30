package com.back.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.back.app.model.Advertisement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AdvertisementRepo extends JpaRepository<Advertisement, Integer> {
    
    //query for parameters sent by frontend
    @Query(value = "SELECT * FROM advertisement a WHERE " +
           "(CAST(:categoryId AS INTEGER) IS NULL OR a.itemtype_id = :categoryId) AND " +
           "(CAST(:beginDate AS DATE) IS NULL OR a.advertisement_start <= CAST(:endDate AS DATE)) AND " +
           "(CAST(:endDate AS DATE) IS NULL OR a.advertisement_end >= CAST(:beginDate AS DATE)) AND " +
           "(CAST(:minPrice AS DECIMAL) IS NULL OR a.advertisement_price >= CAST(:minPrice AS DECIMAL)) AND " +
           "(CAST(:maxPrice AS DECIMAL) IS NULL OR a.advertisement_price <= CAST(:maxPrice AS DECIMAL)) AND " +
           "a.reservation_id IS NULL " , //send jsons of advertisments that arent taken
           nativeQuery = true)
    List<Advertisement> findFilteredAdvertisements(
            @Param("categoryId") Integer categoryId,
            @Param("beginDate") LocalDate beginDate,
            @Param("endDate") LocalDate endDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);

}