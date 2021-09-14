package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.domain.Navigation;
import com.chf.app.repository.NavigationRepository;
import com.chf.app.utils.RandomUtil;
import com.chf.app.web.util.ResponseUtil;
import com.chf.app.web.vm.NavigationVM;

@RestController
@RequestMapping("/api/staff")
public class NavigationResource {

    @Autowired
    private NavigationRepository navigationRepository;

    @PostMapping("/navigation")
    public void createNavigation(@RequestBody NavigationVM navigationVM) {
        Navigation navigation = new Navigation();
        navigation.setId(RandomUtil.uuid());
        navigation.setTitle(navigationVM.getTitle());
        navigation.setIcon(navigationVM.getIcon());
        navigation.setPath(navigationVM.getPath());
        navigation.setRegion(navigationVM.getRegion());
        String parentId = navigationVM.getParentId();
        if (StringUtils.isNotEmpty(parentId)) {
            navigationRepository.findById(parentId).ifPresent(parent -> {
                navigation.setParent(parent);
            });
        }
        navigationRepository.save(navigation);
    }

    @PutMapping("/navigation")
    public void updateNavigation(@RequestBody NavigationVM navigationVM) {
        navigationRepository.findById(navigationVM.getId()).ifPresent(navigation -> {
            navigation.setTitle(navigationVM.getTitle());
            navigation.setIcon(navigationVM.getIcon());
            navigation.setPath(navigationVM.getPath());
            navigation.setRegion(navigationVM.getRegion());
            String parentId = navigationVM.getParentId();
            if (StringUtils.isNotEmpty(parentId)) {
                navigationRepository.findById(parentId).ifPresent(parent -> {
                    navigation.setParent(parent);
                });
            }
            navigationRepository.save(navigation);
        });
    }

    @GetMapping("/navigations")
    public ResponseEntity<List<NavigationVM>> getNavigations(Pageable pageable,
            @RequestParam(name = "parentId", required = false) String parentId) {
        Specification<Navigation> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> andList = new ArrayList<>();

            Navigation parent = Optional.ofNullable(parentId).flatMap(navigationRepository::findById).orElse(null);
            criteriaBuilder.equal(root.get("parent"), parent);

            query.where(criteriaBuilder.and(andList.toArray(new Predicate[andList.size()])));
            return query.getRestriction();
        };
        Page<NavigationVM> page = navigationRepository.findAll(spec, pageable).map(NavigationVM::new);
        return ResponseUtil.wrapPage(page);
    }

    @DeleteMapping("/navigation")
    public void deleteNavigation(@RequestParam String id) {
        navigationRepository.deleteById(id);
    }

}
