package com.lucaschen.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Repository<T, I> extends JpaRepository<T, I>,
        JpaSpecificationExecutor<T> {
}
