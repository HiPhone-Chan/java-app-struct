package com.chf.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chf.app.domain.Navigation;
import com.chf.app.domain.NavigationRole;
import com.chf.app.domain.User;
import com.chf.app.domain.UserRole;
import com.chf.app.domain.id.NavigationRoleId;
import com.chf.app.repository.NavigationRepository;
import com.chf.app.repository.NavigationRoleRepository;
import com.chf.app.repository.UserRoleRepository;
import com.chf.app.service.dto.NavigationTreeDTO;

@Service
@Transactional
public class NavigationService {

    @Autowired
    private NavigationRepository navigationRepository;

    @Autowired
    private NavigationRoleRepository navigationRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<NavigationTreeDTO> getAllTrees() {
        List<Navigation> children = navigationRepository.findAllByParent(null);
        List<NavigationTreeDTO> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(children)) {
            for (Navigation child : children) {
                list.add(getNavTree(child));
            }
        }
        return list;
    }

    public NavigationTreeDTO getNavTree(Navigation node) {
        NavigationTreeDTO tree = new NavigationTreeDTO();
        tree.set(node);

        List<Navigation> children = navigationRepository.findAllByParent(node);
        List<NavigationTreeDTO> list = new ArrayList<>();
        if (!CollectionUtils.isNotEmpty(children)) {
            for (Navigation child : children) {
                list.add(getNavTree(child));
            }
        }
        tree.setChildren(list);
        return tree;
    }

    // 获取和角色相关的导航
    public List<NavigationTreeDTO> getAllRoleNavTree(User user) {
        List<UserRole> userRoleList = userRoleRepository.findAllByIdUser(user);

        Set<String> navigationIdSet = new HashSet<>();
        for (UserRole userRole : userRoleList) {
            List<NavigationRole> navigationRoleList = navigationRoleRepository
                    .findAllByIdRole(userRole.getId().getRole());
            navigationIdSet.addAll(navigationRoleList.stream().map(NavigationRole::getId)
                    .map(NavigationRoleId::getNavigation).map(Navigation::getId).collect(Collectors.toSet()));
        }
        return getRoleNavTree(getAllTrees(), navigationIdSet);
    }

    public List<NavigationTreeDTO> getRoleNavTree(List<NavigationTreeDTO> navigationTreeList,
            Set<String> navigationIdSet) {

        if (CollectionUtils.isEmpty(navigationTreeList)) {
            return navigationTreeList;
        }

        List<NavigationTreeDTO> newNavigationTreeList = new ArrayList<>();
        for (NavigationTreeDTO navigationTreeDTO : navigationTreeList) {
            if (navigationIdSet.contains(navigationTreeDTO.getId())) {
                navigationTreeDTO.setChildren(getRoleNavTree(navigationTreeDTO.getChildren(), navigationIdSet));
                newNavigationTreeList.add(navigationTreeDTO);
            }
        }
        return newNavigationTreeList;
    }

}
