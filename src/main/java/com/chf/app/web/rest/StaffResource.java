package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.constants.AuthoritiesConstants;
import com.chf.app.domain.Authority;
import com.chf.app.domain.User;
import com.chf.app.service.UserService;
import com.chf.app.service.dto.AdminUserDTO;
import com.chf.app.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/staff")
public class StaffResource {

    @Autowired
    private UserService userService;

    @GetMapping("/members")
    public ResponseEntity<List<AdminUserDTO>> getUsers(Pageable pageable) {
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> andList = new ArrayList<>();

            SetJoin<User, Authority> setJoin = root.joinSet("authorities", JoinType.LEFT);
            List<String> authorities = Arrays.asList(AuthoritiesConstants.STAFF);
            andList.add(setJoin.get("name").in(authorities));

            query.where(criteriaBuilder.and(andList.toArray(new Predicate[andList.size()])));
            return query.getRestriction();
        };
        final Page<AdminUserDTO> page = userService.getAllManagedUsers(spec, pageable);
        return ResponseUtil.wrapPage(page);
    }

}
