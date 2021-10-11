package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.domain.ApiInfo;
import com.chf.app.repository.ApiInfoRepository;
import com.chf.app.utils.RandomUtil;
import com.chf.app.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/manager")
public class ApiInfoResource {

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @PostMapping("/api-info")
    @ResponseStatus(HttpStatus.CREATED)
    public void createApiInfo(@Valid @RequestBody ApiInfo apiInfoVM) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setId(RandomUtil.uuid());
        apiInfo.setMethod(apiInfoVM.getMethod().toLowerCase());
        apiInfo.setPath(apiInfoVM.getPath());
        apiInfo.setDescription(apiInfoVM.getDescription());
        apiInfoRepository.save(apiInfo);
    }

    @PutMapping("/api-info")
    public void updateApiInfo(@RequestBody ApiInfo apiInfoVM) {
        apiInfoRepository.findById(apiInfoVM.getId()).ifPresent(apiInfo -> {
            apiInfo.setMethod(apiInfoVM.getMethod().toLowerCase());
            apiInfo.setPath(apiInfoVM.getPath());
            apiInfo.setDescription(apiInfoVM.getDescription());
            apiInfoRepository.save(apiInfo);
        });
    }

    @GetMapping("/api-infos")
    public ResponseEntity<List<ApiInfo>> getApiInfos(Pageable pageable,
            @RequestParam(name = "method", required = false) String method,
            @RequestParam(name = "path", required = false) String path,
            @RequestParam(name = "description", required = false) String description) {
        Specification<ApiInfo> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> andList = new ArrayList<>();

            if (StringUtils.isNotEmpty(method)) {
                andList.add(criteriaBuilder.equal(root.get("method"), method));
            }
            if (StringUtils.isNotEmpty(path)) {
                andList.add(criteriaBuilder.like(root.get("method"), "%" + path + "%"));
            }
            if (StringUtils.isNotEmpty(description)) {
                andList.add(criteriaBuilder.like(root.get("method"), "%" + description + "%"));
            }

            query.where(criteriaBuilder.and(andList.toArray(new Predicate[andList.size()])));
            return query.getRestriction();
        };
        Page<ApiInfo> page = apiInfoRepository.findAll(spec, pageable);
        return ResponseUtil.wrapPage(page);
    }

    @DeleteMapping("/api-info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApiInfo(@RequestParam String id) {
        apiInfoRepository.deleteById(id);
    }

}
