package com.chf.app.repository.support;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

@NoRepositoryBean
public interface JpaExtRepository<T, ID> extends JpaRepositoryImplementation<T, ID> {

    long sum(@Nullable Specification<T> spec);

}
