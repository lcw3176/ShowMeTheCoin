package com.joebrooks.showmethecoin.repository.pricestore;

import com.joebrooks.showmethecoin.exchange.CommonCoinType;
import com.joebrooks.showmethecoin.exchange.CompanyType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceStoreRepository extends JpaRepository<PriceStoreEntity, Long> {

    Optional<PriceStoreEntity> findByCoinTypeAndCompanyType(CommonCoinType coinType, CompanyType companyType);

    List<PriceStoreEntity> findAllByCompanyType(CompanyType companyType);

}
