package com.chf.app.service;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(StaffService.class);

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserService userService;

    public Staff createStaff(AdminStaffDTO staffDTO) {
        staffDTO.setAuthorities(SetUtils.hashSet(AuthoritiesConstants.STAFF));
        User user = userService.createUser(staffDTO);
        Staff staff = new Staff();
        staff.setId(new UserId(user));
        String staffId = staffDTO.getStaffId();
        if (StringUtils.isEmpty(staffId)) {
            staffId = generateStaffId();
        }
        staff.setStaffId(staffId);
        return staffRepository.save(staff);
    }

    public Page<AdminStaffDTO> getStaffs(Specification<Staff> spec, Pageable pageable) {
        return staffRepository.findAll(spec, pageable).map(staff -> {
            AdminStaffDTO adminStaffDTO = new AdminStaffDTO(staff.getId().getUser());
            adminStaffDTO.setStaffId(staff.getStaffId());
            return adminStaffDTO;
        });
    }

    public String generateStaffId() {
        String id = RandomStringUtils.randomNumeric(10);
        int count = 0;
        while (staffRepository.existsByStaffId(id)) {
            id = RandomStringUtils.randomNumeric(10);
            if (count++ > 30) {
                log.warn("warning generate Teacher Id failed too many times.");
            }
        }
        return id;
    }

    public void deleteStaff(String login) {
        User user = userService.getUserWithAuthoritiesByLogin(login).orElseThrow();

        staffRepository.deleteById(new UserId(user));
        userService.deleteUser(login);
    }
}
