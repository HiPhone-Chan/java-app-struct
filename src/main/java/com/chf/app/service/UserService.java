package com.chf.app.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.chf.app.service.dto.AdminUserDTO;
import com.chf.app.service.dto.UserDTO;
import com.chf.app.utils.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository,
            PasswordEncoder passwordEncoder, CacheManager cacheManager) {
        super();
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
    }

    public User createUser(AdminUserDTO userDTO) {
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
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId())).filter(Optional::isPresent).map(Optional::get)
                .map(user -> {
                    this.clearUserCaches(user);
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
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }).map(AdminUserDTO::new);
    }

    public void updateUser(String nickName, String mobile, String imageUrl) {
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            user.setNickName(nickName);
            user.setMobile(mobile);
            user.setImageUrl(imageUrl);

            this.clearUserCaches(user);
            log.debug("Changed Information for User: {}", user);
        });
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            if (isUserInRole(user, AuthoritiesConstants.ADMIN)) {
                log.warn("Cannot delete admin user: {}", user);
                return;
            }
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePasswordBySuperior(User subordinate, String superiorClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(superior -> {
            String currentEncryptedPassword = superior.getPassword();
            if (!passwordEncoder.matches(superiorClearTextPassword, currentEncryptedPassword)) {
                throw new ServiceException(ErrorCodeContants.BAD_PARAMETERS, "Old password not matched");
            }

            String encryptedPassword = passwordEncoder.encode(newPassword);
            subordinate.setPassword(encryptedPassword);
            userRepository.save(subordinate);
            this.clearUserCaches(subordinate);
            log.debug("Changed password for User: {}", subordinate.getLogin());
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

            this.clearUserCaches(user);
            log.debug("Changed password for User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Specification<User> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
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
                    this.clearUserCaches(user);
                });
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USER_BY_LOGIN_CACHE)).evict(user.getLogin());
    }
}
