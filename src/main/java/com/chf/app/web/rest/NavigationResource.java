package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.List;

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

import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.domain.Navigation;
import com.chf.app.exception.ServiceException;
import com.chf.app.repository.NavigationApiRepository;
import com.chf.app.repository.NavigationRepository;
import com.chf.app.service.NavigationService;
import com.chf.app.service.dto.NavigationTreeDTO;
import com.chf.app.utils.RandomUtil;
import com.chf.app.web.util.ResponseUtil;
import com.chf.app.web.vm.NavigationVM;

@RestController
@RequestMapping("/api/manager")
public class NavigationResource {

    @Autowired
    private NavigationRepository navigationRepository;

    @Autowired
    private NavigationApiRepository navigationApiRepository;

    @Autowired
    private NavigationService navigationService;

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
            Navigation parent = navigationRepository.findById(parentId)
                    .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));
            navigation.setParent(parent);
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
                Navigation parent = navigationRepository.findById(parentId)
                        .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA));
                navigation.setParent(parent);
            }
            navigationRepository.save(navigation);
        });
    }

    @GetMapping("/navigations")
    public ResponseEntity<List<NavigationVM>> getNavigations(Pageable pageable,
            @RequestParam(name = "parentId", required = false) String parentId) {
        Specification<Navigation> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> andList = new ArrayList<>();

            if (StringUtils.isEmpty(parentId)) {
                andList.add(root.get("parent").isNull());
            } else {
                Navigation parent = navigationRepository.findById(parentId).orElseThrow();
                andList.add(criteriaBuilder.equal(root.get("parent"), parent));
            }

            query.where(criteriaBuilder.and(andList.toArray(new Predicate[andList.size()])));
            return query.getRestriction();
        };
        Page<NavigationVM> page = navigationRepository.findAll(spec, pageable).map(navigation -> {
            NavigationVM navigationVM = new NavigationVM(navigation);
            navigationVM.setHasChildren(navigationRepository.existsByParent(navigation));
            navigationVM.setHasApis(navigationApiRepository.existsByIdNavigation(navigation));
            return navigationVM;
        });
        return ResponseUtil.wrapPage(page);
    }

    @GetMapping("/navigation/trees")
    public List<NavigationTreeDTO> getNavigationTrees() {
        return navigationService.getAllTrees();
    }

    @DeleteMapping("/navigation")
    public void deleteNavigation(@RequestParam String id) {
        navigationRepository.deleteById(id);
    }

}
