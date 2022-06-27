package com.heritage.stock.repository;

import com.heritage.stock.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {
//    Page<Stock> findAllByCompanyCodeBetween(Pageable pageable, String companyCode, Date startDate, Date endDate);

    List<Stock> findAllByCompanyCode(String companyCode);

//    @Query("{$and :[{companyCode: ?0},{endDate: {$gte: ?1, $lte: ?2}}]}")
//    Page<Stock> findAllByCompanyCodeAndDateBetween(Pageable pageable, String companyCode, Date startDate, Date endDate);
    Page<Stock> findAllByCompanyCodeAndCreatedAtBetween(Pageable pageable, String companyCode, Date startDate, Date endDate);

    void deleteAllByCompanyCode(String companyCode);
}
