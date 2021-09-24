package com.chf.app.config;

import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import com.chf.app.config.properties.ConfigProperties;
import com.chf.app.constants.AuthoritiesConstants;
import com.chf.app.security.bpm.BpmConfigurer;
import com.chf.app.security.jwt.JWTConfigurer;
import com.chf.app.security.jwt.TokenProvider;
import com.chf.app.security.rbac.StaffVoter;
import com.chf.app.service.RoleService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ConfigProperties configProperties;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    private final SecurityProblemSupport problemSupport;

    private final RoleService roleService;
    
    private final IdentityService identityService;

    public SecurityConfiguration(ConfigProperties configProperties, TokenProvider tokenProvider, CorsFilter corsFilter,
            SecurityProblemSupport problemSupport, RoleService roleService, IdentityService identityService) {
        super();
        this.configProperties = configProperties;
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.roleService = roleService;
        this.identityService = identityService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/h2-console/**").antMatchers("/test/**")
                .antMatchers("/pages/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable().addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(problemSupport).accessDeniedHandler(problemSupport)
                .and()
                .headers()
                .contentSecurityPolicy(configProperties.getSecurity().getContentSecurityPolicy())
                .and()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                .and()
                .featurePolicy(
                        "geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
                .and()
                .frameOptions().deny().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .accessDecisionManager(accessDecisionManager())
                .antMatchers("/resource/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/engine-rest/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/manager/**").hasAuthority(AuthoritiesConstants.MANAGER)
                .antMatchers("/api/staff/**").hasAuthority(AuthoritiesConstants.STAFF)
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/health").permitAll().antMatchers("/management/info").permitAll()
                .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN).and()
                .httpBasic().and()
                .apply(new JWTConfigurer(tokenProvider)).and()
                .apply(new BpmConfigurer(identityService))
                ;
        // @formatter:on
    }

    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(new WebExpressionVoter(),
                getRoleHierarchyVoter(), new StaffVoter(roleService));
        return new UnanimousBased(decisionVoters);
    }

    private RoleVoter getRoleHierarchyVoter() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(AuthoritiesConstants.ADMIN + ">" + AuthoritiesConstants.MANAGER);
        roleHierarchy.setHierarchy(AuthoritiesConstants.MANAGER + ">" + AuthoritiesConstants.STAFF);
        roleHierarchy.setHierarchy(AuthoritiesConstants.STAFF + ">" + AuthoritiesConstants.USER);
        roleHierarchy.setHierarchy(AuthoritiesConstants.USER + ">" + AuthoritiesConstants.ANONYMOUS);
        return new RoleHierarchyVoter(roleHierarchy);
    }

}
