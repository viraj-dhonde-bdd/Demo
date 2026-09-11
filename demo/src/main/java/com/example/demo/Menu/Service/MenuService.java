package com.example.demo.Menu.Service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.Menu.Entity.MenuDto;
import com.example.demo.Menu.MenuRepository.MenuRepository;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Map<String, Object> insertRoleMenus(Map<String, List<MenuDto>> payload) {
        return menuRepository.insertRoleMenus(payload);
    }

    public List<MenuDto> getMenusForRole(String role) {
        return menuRepository.findMenusByRole(role);
    }
}
