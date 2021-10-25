package com.chf.app.service;

import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chf.app.constants.AuthoritiesConstants;
import com.chf.app.domain.Staff;
import com.chf.app.domain.User;
import com.chf.app.domain.id.UserId;
import com.chf.app.repository.StaffRepository;
import com.chf.app.service.dto.AdminStaffDTO;

@Service
@Transactional
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserService userService;

    public Staff createStaff(AdminStaffDTO staffDTO) {
        staffDTO.setAuthorities(SetUtils.hashSet(AuthoritiesConstants.STAFF));
        User user = userService.createUser(staffDTO);
        Staff staff = new Staff();
        staff.setId(new UserId(user));
        staff.setStaffId(staffDTO.getStaffId());
        return staffRepository.save(staff);
    }

    public Page<AdminStaffDTO> getStaffs(Specification<Staff> spec, Pageable pageable) {
        return staffRepository.findAll(spec, pageable).map(staff -> {
            AdminStaffDTO adminStaffDTO = new AdminStaffDTO(staff.getId().getUser());
            adminStaffDTO.setStaffId(staff.getStaffId());
            return adminStaffDTO;
        });
    }

    public void deleteStaff(String login) {
        User user = userService.getUserWithAuthoritiesByLogin(login).orElseThrow();

        staffRepository.deleteById(new UserId(user));
        userService.deleteUser(login);
    }
}
