package com.lucaschen.common.jpa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface Service<T, I> {
    <S extends T> S save(final S entity);

    @Transactional(rollbackFor = Exception.class)
    <S extends T> Iterable<S> saveAll(final Iterable<S> entities);

    <S extends T> S saveAndFlush(final S entity);

    @Transactional(rollbackFor = Exception.class)
    <S extends T> List<S> saveAllAndFlush(final Iterable<S> entities);

    boolean existsById(final I id);

    void deleteById(final I id);

    @Transactional(rollbackFor = Exception.class)
    void deleteAllByIdInBatch(final Iterable<I> ids);

    Optional<T> findById(final I id);

    Iterable<T> findAllById(final Iterable<I> ids);

    Optional<T> findOne(@Nullable final Specification<T> spec);

    List<T> findAll(@Nullable final Specification<T> spec);

    Page<T> findAll(@Nullable final Specification<T> spec, final Pageable pageable);

    List<T> findAll(@Nullable final Specification<T> spec, final Sort sort);

    long count(@Nullable final Specification<T> spec);
}
