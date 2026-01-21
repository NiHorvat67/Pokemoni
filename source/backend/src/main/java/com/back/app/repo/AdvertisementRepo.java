package com.back.app.repo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.back.app.model.Advertisement;

public interface AdvertisementRepo extends JpaRepository<Advertisement, Integer> {

       @Query("SELECT a FROM Advertisement a WHERE a.trader.id = :traderId")
       List<Advertisement> findAllForTraderId(@Param("traderId") Integer traderId);

       // query for parameters sent by frontend
       @Query(value = "SELECT * FROM advertisement a WHERE " +
                     "(CAST(:categoryId AS INTEGER) IS NULL OR a.itemtype_id = :categoryId) AND " +
                     "(CAST(:beginDate AS DATE) IS NULL OR a.advertisement_start <= CAST(:endDate AS DATE)) AND " +
                     "(CAST(:endDate AS DATE) IS NULL OR a.advertisement_end >= CAST(:beginDate AS DATE)) AND " +
                     "(CAST(:minPrice AS DECIMAL) IS NULL OR a.advertisement_price >= CAST(:minPrice AS DECIMAL)) AND "
                     +
                     "(CAST(:maxPrice AS DECIMAL) IS NULL OR a.advertisement_price <= CAST(:maxPrice AS DECIMAL)) AND "
                     +
                     "(:itemName IS NULL OR a.item_name ILIKE '%' || :itemName || '%') " 
                     , nativeQuery = true)
       List<Advertisement> findFilteredAdvertisements(
                     @Param("itemName") String itemName,
                     @Param("categoryId") Integer categoryId,
                     @Param("beginDate") LocalDate beginDate,
                     @Param("endDate") LocalDate endDate,
                     @Param("minPrice") BigDecimal minPrice,
                     @Param("maxPrice") BigDecimal maxPrice);


       List<Advertisement> findAllByAdvertisementIdIn(List<Integer> ids);
       Advertisement findByAdvertisementId(Integer id);

}