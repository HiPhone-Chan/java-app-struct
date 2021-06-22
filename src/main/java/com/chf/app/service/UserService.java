package com.chf.app.service;

import static com.chf.app.constants.AuthoritiesConstants.ADMIN;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chf.app.constants.AuthoritiesConstants;
import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.constants.SystemConstants;
import com.chf.app.domain.Authority;
import com.chf.app.domain.User;
import com.chf.app.exception.ServiceException;
import com.chf.app.repository.AuthorityRepository;
import com.chf.app.repository.UserRepository;
import com.chf.app.security.SecurityUtils;
import com.chf.app.service.dto.UserDTO;
import com.chf.app.utils.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserDTO userDTO) {
        User user = new User();
        String login = userDTO.getLogin().toLowerCase(Locale.ENGLISH);
        user.setLogin(login);
        user.setNickName(userDTO.getNickName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        String langKey = userDTO.getLangKey();
        if (StringUtils.isEmpty(langKey)) {
            user.setLangKey(SystemConstants.LANG);
        } else {
            user.setLangKey(langKey);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream().map(authorityRepository::findById)
                    .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId())).filter(Optional::isPresent).map(Optional::get)
                .map(user -> {
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setNickName(userDTO.getNickName());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    user.setEmail(userDTO.getEmail());
                    user.setMobile(userDTO.getMobile());
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    user.setLangKey(userDTO.getLangKey());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO.getAuthorities().stream().map(authorityRepository::findById).filter(Optional::isPresent)
                            .map(Optional::get).forEach(managedAuthorities::add);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }).map(UserDTO::new);
    }

    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLangKey(langKey);
            user.setImageUrl(imageUrl);
            log.debug("Changed Information for User: {}", user);
        });
    }

    public void deleteUser(String login) {
        SecurityUtils.isCurrentUserInRole("");
        userRepository.findOneByLogin(login).ifPresent(user -> {
            if (isUserInRole(user, AuthoritiesConstants.ADMIN)) {
                log.warn("Cannot delete admin user: {}", user);
                return;
            }
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    @Secured(ADMIN)
    public void changePassword(String login, String currentClearTextPassword, String newPassword) {
        Optional.of(login).flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(admin -> {
                String currentEncryptedPassword = admin.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new ServiceException(ErrorCodeContants.BAD_PARAMETERS, "Old password not matched");
                }

                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                log.debug("Changed password for User: {}", user);
            });
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            String currentEncryptedPassword = user.getPassword();
            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                throw new ServiceException(ErrorCodeContants.BAD_PARAMETERS, "Old password not matched");
            }
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            log.debug("Changed password for User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Specification<User> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public static boolean isUserInRole(User user, String hasAuthority) {
        return user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals(hasAuthority));
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                });
    }
}
