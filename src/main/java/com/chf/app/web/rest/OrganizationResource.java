package com.chf.app.web.rest;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.domain.Organization;
import com.chf.app.repository.OrganizationRepository;
import com.chf.app.utils.RandomUtil;
import com.chf.app.web.util.ResponseUtil;
import com.chf.app.web.vm.OrganizationVM;

@RestController
@RequestMapping("/api/staff")
public class OrganizationResource {

    @Autowired
    private OrganizationRepository organizationRepository;

    @PostMapping("/organization")
    public void createOrganization(@RequestBody OrganizationVM organizationVM) {
        Organization organization = new Organization();
        organization.setId(RandomUtil.uuid());
        organization.setName(organizationVM.getName());
        String parentId = organizationVM.getParentId();
        if (StringUtils.isNotEmpty(parentId)) {
            organizationRepository.findById(parentId).ifPresent(parent -> {
                organization.setParent(parent);
            });
        }
        organizationRepository.save(organization);
    }

    @PutMapping("/organization")
    public void updateOrganization(@RequestBody OrganizationVM organizationVM) {
        organizationRepository.findById(organizationVM.getId()).ifPresent(organization -> {
            organization.setName(organizationVM.getName());
            String parentId = organizationVM.getParentId();
            if (StringUtils.isNotEmpty(parentId)) {
                organizationRepository.findById(parentId).ifPresent(parent -> {
                    organization.setParent(parent);
                });
            }
            organizationRepository.save(organization);
        });
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationVM>> getOrganizations(Pageable pageable,
            @RequestParam(name = "parentId", required = false) String parentId) {
        Organization parent = Optional.ofNullable(parentId).flatMap(organizationRepository::findById).orElse(null);
        Page<OrganizationVM> page = organizationRepository.findByParent(pageable, parent).map(OrganizationVM::new);
        return ResponseUtil.wrapPage(page);
    }

    @DeleteMapping("/organization")
    public void deleteOrganization(@RequestParam String id) {
        organizationRepository.deleteById(id);
    }

}
