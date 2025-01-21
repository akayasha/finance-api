package com.example.repository;

import com.example.model.FinanceData;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class FinanceDataRepository implements PanacheRepository<FinanceData> {


}
