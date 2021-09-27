package com.chf.app.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import javax.validation.Valid;

import org.apache.commons.collections4.SetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.chf.app.constants.AuthoritiesConstants;
import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.domain.Authority;
import com.chf.app.domain.User;
import com.chf.app.exception.ServiceException;
import com.chf.app.repository.UserRepository;
import com.chf.app.service.UserService;
import com.chf.app.service.dto.AdminUserDTO;
import com.chf.app.service.dto.UserDTO;
import com.chf.app.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/manager")
public class StaffResource {

    private static final Logger log = LoggerFactory.getLogger(StaffResource.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/staff")
    public User createStaff(@Valid @RequestBody AdminUserDTO userDTO) {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new ServiceException(ErrorCodeContants.BAD_PARAMETERS, "A new user cannot already have an ID");
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new ServiceException(ErrorCodeContants.LOGIN_ALREADY_USED);
        }

        userDTO.setAuthorities(SetUtils.hashSet(AuthoritiesConstants.STAFF));
        User newUser = userService.createUser(userDTO);
        return newUser;
    }

    @PutMapping("/staff")
    public ResponseEntity<UserDTO> updateStaff(@Valid @RequestBody AdminUserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        User existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).orElseThrow();

        Set<String> authorities = existingUser.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet());
        if (authorities.contains(AuthoritiesConstants.STAFF)) {
            userDTO.setAuthorities(
                    existingUser.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
            Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
            return ResponseUtil.wrapOrNotFound(updatedUser);
        }
        return ResponseUtil.wrapOrNotFound(Optional.empty());
    }

    @GetMapping("/staffs")
    public ResponseEntity<List<AdminUserDTO>> getStaffs(Pageable pageable) {
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

    @DeleteMapping("/staff")
    public void deleteStaff(@RequestParam String login) {
        log.debug("REST request to delete Staff: {}", login);
        User user = userService.getUserWithAuthoritiesByLogin(login).orElseThrow();
        Set<String> authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
        if (authorities.contains(AuthoritiesConstants.STAFF) && !authorities.contains(AuthoritiesConstants.ADMIN)) {
            userService.deleteUser(login);
        }
    }

}
