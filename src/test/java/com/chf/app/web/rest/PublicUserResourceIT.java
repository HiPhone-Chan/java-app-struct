package com.chf.app.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.chf.app.IntegrationTest;
import com.chf.app.constants.AuthoritiesConstants;
import com.chf.app.domain.User;
import com.chf.app.repository.UserRepository;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class PublicUserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String DEFAULT_LANGKEY = "en";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    @BeforeEach
    public void setup() {
        cacheManager.getCache(UserRepository.USER_BY_LOGIN_CACHE).clear();
    }

    @BeforeEach
    public void initTest() {
        user = UserResourceIT.initTestUser(userRepository, em);
    }

    @Test
    @Transactional
    void getAllPublicUsers() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get all the users
        restUserMockMvc.perform(get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
                .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGEURL)))
                .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)));
    }

    @Test
    @Transactional
    void getAllAuthorities() throws Exception {
        restUserMockMvc
                .perform(get("/api/authorities").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasItems(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN)));
    }

    @Test
    @Transactional
    void getAllUsersSortedByParameters() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        restUserMockMvc.perform(get("/api/users?sort=resetKey,desc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        restUserMockMvc.perform(get("/api/users?sort=password,desc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        restUserMockMvc.perform(get("/api/users?sort=resetKey,id,desc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        restUserMockMvc.perform(get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
