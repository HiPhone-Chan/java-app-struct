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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.constants.AuthoritiesConstants;
import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.constants.SystemConstants;
import com.chf.app.domain.Authority;
import com.chf.app.domain.User;
import com.chf.app.exception.ServiceException;
import com.chf.app.repository.UserRepository;
import com.chf.app.service.UserService;
import com.chf.app.service.dto.AdminUserDTO;
import com.chf.app.service.dto.PasswordChangeDTO;
import com.chf.app.web.util.ResponseUtil;
import com.chf.app.web.vm.ManagedUserVM;

@RestController
@RequestMapping("/api/manager")
public class StaffResource {

    private static final Logger log = LoggerFactory.getLogger(StaffResource.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/staff")
    @ResponseStatus(HttpStatus.CREATED)
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
    public ResponseEntity<AdminUserDTO> updateStaff(@Valid @RequestBody AdminUserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        User existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).orElseThrow();

        Set<String> authorities = existingUser.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet());
        if (authorities.contains(AuthoritiesConstants.STAFF)) {
            userDTO.setAuthorities(
                    existingUser.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
            Optional<AdminUserDTO> updatedUser = userService.updateUser(userDTO);
            return ResponseUtil.wrapOrNotFound(updatedUser);
        }
        return ResponseUtil.wrapOrNotFound(Optional.empty());
    }

    @PutMapping("/staff/change-password/{login:" + SystemConstants.LOGIN_REGEX + "}")
    public void changePassword(@PathVariable String login, @RequestBody PasswordChangeDTO passwordChangeDTO) {
        if (!ManagedUserVM.checkPasswordLength(passwordChangeDTO.getNewPassword())) {
            throw new ServiceException(ErrorCodeContants.INVALID_PASSWORD, "Password is short.");
        }

        User user = userService.getUserWithAuthoritiesByLogin(login).orElseThrow();
        if (user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet())
                .contains(AuthoritiesConstants.STAFF)) {
            userService.changePasswordBySuperior(user, passwordChangeDTO.getCurrentPassword(),
                    passwordChangeDTO.getNewPassword());
        }
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStaff(@RequestParam String login) {
        log.debug("REST request to delete Staff: {}", login);
        User user = userService.getUserWithAuthoritiesByLogin(login).orElseThrow();
        Set<String> authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
        if (authorities.contains(AuthoritiesConstants.STAFF) && !authorities.contains(AuthoritiesConstants.ADMIN)) {
            userService.deleteUser(login);
        }
    }

}
