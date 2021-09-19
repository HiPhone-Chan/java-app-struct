package com.chf.app.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chf.app.domain.Navigation;
import com.chf.app.repository.NavigationRepository;
import com.chf.app.service.dto.NavigationTreeDTO;

@Service
@Transactional
public class NavigationService {

    @Autowired
    private NavigationRepository navigationRepository;

    public List<NavigationTreeDTO> getAllTrees() {
        List<Navigation> children = navigationRepository.findAllByParent(null);
        List<NavigationTreeDTO> list = new ArrayList<>();
        if (!CollectionUtils.isNotEmpty(children)) {
            for (Navigation child : children) {
                list.add(getTree(child));
            }
        }
        return list;
    }

    NavigationTreeDTO getTree(Navigation node) {
        NavigationTreeDTO tree = new NavigationTreeDTO();
        tree.set(node);

        List<Navigation> children = navigationRepository.findAllByParent(node);
        List<NavigationTreeDTO> list = new ArrayList<>();
        if (!CollectionUtils.isNotEmpty(children)) {
            for (Navigation child : children) {
                list.add(getTree(child));
            }
        }
        tree.setChildren(list);
        return tree;
    }

}
