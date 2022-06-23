package com.heritage.company.repositories;

import com.heritage.company.models.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

    Optional<Company> findByCode(String code);
    void deleteByCode(String code);
}
