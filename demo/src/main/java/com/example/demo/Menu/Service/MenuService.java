package com.example.demo.Menu.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Menu.Entity.Menu1;
import com.example.demo.Menu.MenuRepository.MenuRepository1;

@Service
public class MenuService {

    @Autowired
    private MenuRepository1 menuRepository;

    public List<Menu1> getHierarchicalMenusForRoles(List<String> roles) {
        List<Menu1> flatMenus = menuRepository.findMenusByRoleNames(roles);
        return buildMenuTree(flatMenus);
    }

    private List<Menu1> buildMenuTree(List<Menu1> flatMenus) {
        Map<Long, Menu1> menuMap = new LinkedHashMap<>();
        List<Menu1> rootMenus = new ArrayList<>();

        for (Menu1 menu : flatMenus) {
            menuMap.put(menu.getId(), menu);
        }

        for (Menu1 menu : flatMenus) {
            if (menu.getParentId() == null) {
                rootMenus.add(menu);
            } else {
                Menu1 parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getSubmenus().add(menu);
                }
            }
        }
        return rootMenus;
    }
}