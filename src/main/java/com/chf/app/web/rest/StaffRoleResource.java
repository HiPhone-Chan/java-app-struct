package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.chf.app.domain.StaffRole;
import com.chf.app.repository.StaffRoleRepository;
import com.chf.app.utils.RandomUtil;
import com.chf.app.web.util.ResponseUtil;
import com.chf.app.web.vm.ImportDataVM;

@RestController
@RequestMapping("/api/manager")
public class StaffRoleResource {

    @Autowired
    private StaffRoleRepository staffRoleRepository;

    @PostMapping("/role")
    @ResponseStatus(HttpStatus.CREATED)
    public void createStaffRole(@RequestBody StaffRole staffRoleVM) {
        StaffRole staffRole = newStaffRole(staffRoleVM);
        staffRoleRepository.save(staffRole);
    }

    @PostMapping("/role/import")
    public void importStaffRole(@RequestBody ImportDataVM<StaffRole> importDataVM) {
        List<StaffRole> staffRoleList = new ArrayList<>();
        for (StaffRole staffRoleVM : importDataVM.getDataList()) {

            StaffRole staffRole = newStaffRole(staffRoleVM);
            staffRoleList.add(staffRole);
        }

        if (importDataVM.isAdded()) {
        } else {
            staffRoleRepository.deleteAll();
        }

        staffRoleRepository.saveAll(staffRoleList);
    }

    @PutMapping("/role")
    public void updateStaffRole(@RequestBody StaffRole staffRoleVM) {
        staffRoleRepository.findById(staffRoleVM.getId()).ifPresent(staffRole -> {
            String code = staffRoleVM.getCode();
            if (StringUtils.isEmpty(code)) {
                code = staffRole.getId();
            }
            staffRole.setCode(code);

            staffRole.setName(staffRoleVM.getName());
            staffRole.setRemark(staffRoleVM.getRemark());
            staffRoleRepository.save(staffRole);
        });
    }

    @GetMapping("/roles")
    public ResponseEntity<List<StaffRole>> getStaffRoles(Pageable pageable) {
        Page<StaffRole> page = staffRoleRepository.findAll(pageable);
        return ResponseUtil.wrapPage(page);
    }

    @DeleteMapping("/role")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStaffRole(@RequestParam String id) {
        staffRoleRepository.deleteById(id);
    }

    private StaffRole newStaffRole(StaffRole staffRoleVM) {
        StaffRole staffRole = new StaffRole();
        staffRole.setId(RandomUtil.uuid());

        String code = staffRoleVM.getCode();
        if (StringUtils.isEmpty(code)) {
            code = RandomStringUtils.randomNumeric(12);
        }
        staffRole.setCode(code);
        staffRole.setName(staffRoleVM.getName());
        staffRole.setRemark(staffRoleVM.getRemark());
        return staffRole;
    }

}
