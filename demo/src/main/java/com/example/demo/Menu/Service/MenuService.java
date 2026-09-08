package com.example.demo.Menu.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Menu.Entity.Menu;
import com.example.demo.Menu.MenuRepository.MenuRepository;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getHierarchicalMenusForRoles(List<String> roles) {
        List<Menu> flatMenus = menuRepository.findMenusByRoleNames(roles);
        return buildMenuTree(flatMenus);
    }

    private List<Menu> buildMenuTree(List<Menu> flatMenus) {
        Map<Long, Menu> menuMap = new LinkedHashMap<>();
        List<Menu> rootMenus = new ArrayList<>();

        for (Menu menu : flatMenus) {
            menuMap.put(menu.getId(), menu);
        }

        for (Menu menu : flatMenus) {
            if (menu.getParentId() == null) {
                rootMenus.add(menu);
            } else {
                Menu parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getSubmenus().add(menu);
                }
            }
        }
        return rootMenus;
    }
}