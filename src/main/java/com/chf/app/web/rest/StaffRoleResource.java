package com.chf.app.web.rest;

import java.util.List;

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

import com.chf.app.domain.StaffRole;
import com.chf.app.repository.StaffRoleRepository;
import com.chf.app.utils.RandomUtil;
import com.chf.app.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/staff")
public class StaffRoleResource {

    @Autowired
    private StaffRoleRepository staffRoleRepository;

    @PostMapping("/role")
    public void createStaffRole(@RequestBody StaffRole staffRoleVM) {
        StaffRole staffRole = new StaffRole();
        staffRole.setId(RandomUtil.uuid());
        staffRole.setName(staffRoleVM.getName());
        staffRole.setRemark(staffRoleVM.getRemark());
        staffRoleRepository.save(staffRole);
    }

    @PutMapping("/role")
    public void updateStaffRole(@RequestBody StaffRole staffRoleVM) {
        staffRoleRepository.findById(staffRoleVM.getId()).ifPresent(staffRole -> {
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
    public void deleteStaffRole(@RequestParam String id) {
        staffRoleRepository.deleteById(id);
    }

}
