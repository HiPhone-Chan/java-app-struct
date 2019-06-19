package com.chf.app.repository.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class JpaExtRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements JpaExtRepository<T, ID> {

    private final EntityManager em;

    public JpaExtRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        Assert.notNull(entityInformation, "JpaEntityInformation must not be null!");
        Assert.notNull(entityManager, "EntityManager must not be null!");

        this.em = entityManager;
    }

    public JpaExtRepositoryImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    @Override
    public long sum(Specification<T> spec) {
        return executeSumQuery(getSumQuery(spec, getDomainClass()));
    }

    protected <S extends T> TypedQuery<Number> getSumQuery(@Nullable Specification<S> spec, Class<S> domainClass) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Number> query = builder.createQuery(Number.class);

        Root<S> root = query.from(domainClass);
        Predicate predicate = spec.toPredicate(root, query, builder);
        // query.select(builder.sumAsLong(x)); 外部设置
        if (predicate != null) {
            query.where(predicate);
        }

        // Remove all Orders the Specifications might have applied
        query.orderBy(Collections.<Order>emptyList());

        return em.createQuery(query);
    }

    private static long executeSumQuery(TypedQuery<Number> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List<Number> totals = query.getResultList();
        long total = 0L;

        for (Number element : totals) {
            total += element == null ? 0 : element.longValue();
        }

        return total;
    }

}
