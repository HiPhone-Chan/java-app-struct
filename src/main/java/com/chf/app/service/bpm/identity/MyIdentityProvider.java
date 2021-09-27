package com.chf.app.service.bpm.identity;

import java.util.List;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.identity.NativeUserQuery;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.TenantQuery;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.impl.GroupQueryImpl;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.UserQueryImpl;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.chf.app.repository.OrganizationRepository;
import com.chf.app.repository.UserRepository;

public class MyIdentityProvider implements ReadOnlyIdentityProvider {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private OrganizationRepository organizationRepository;

    @Override
    public void flush() {
        userRepository.flush();
        organizationRepository.flush();
    }

    @Override
    public void close() {
    }

    @Override
    public User findUserById(String userId) {
        return userRepository.findOneByLogin(userId).map(BpmUser::new).orElse(null);
    }

    @Override
    public UserQuery createUserQuery() {
        return new UserQueryImpl() {

            private static final long serialVersionUID = 1L;

            @Override
            public long executeCount(CommandContext commandContext) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public List<User> executeList(CommandContext commandContext, Page page) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Override
    public UserQuery createUserQuery(CommandContext commandContext) {
        return null;
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        throw new BadUserRequestException("not supported");
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        return userRepository.findOneByLogin(userId).map(user -> {
            return passwordEncoder.matches(password, user.getPassword());
        }).orElse(false);
    }

    @Override
    public Group findGroupById(String groupId) {
        return organizationRepository.findById(groupId).map(BpmGroup::new).orElse(null);
    }

    @Override
    public GroupQuery createGroupQuery() {
        return new GroupQueryImpl() {

            private static final long serialVersionUID = 1L;

            @Override
            public long executeCount(CommandContext commandContext) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public List<Group> executeList(CommandContext commandContext, Page page) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Override
    public GroupQuery createGroupQuery(CommandContext commandContext) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Tenant findTenantById(String tenantId) {
        throw new BadUserRequestException("not supported");
    }

    @Override
    public TenantQuery createTenantQuery() {
        throw new BadUserRequestException("not supported");
    }

    @Override
    public TenantQuery createTenantQuery(CommandContext commandContext) {
        throw new BadUserRequestException("not supported");
    }

}
