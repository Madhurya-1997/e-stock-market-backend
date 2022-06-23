package com.heritage.stock.repository;

import com.heritage.stock.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {
    Page<Stock> findAllByCompanyCodeBetween(Pageable pageable, String companyCode, Date startDate, Date endDate);

//    @Query("{'companyCode': {$gte: ?0, $lte:?1 }}")
//    Page<Stock> findAllByCompanyCodeBetween(Date startDate, Date endDate);
}
