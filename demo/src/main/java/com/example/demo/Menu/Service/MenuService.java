package com.example.demo.Menu.Service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.Menu.Entity.MenuDto;
import com.example.demo.Menu.Entity.SubmenuDto;
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
    
    public boolean removeMenu(long menuId) {
        return menuRepository.deleteMenu(menuId);
    }

    public boolean removeSubmenu(long submenuId) {
        return menuRepository.deleteSubmenu(submenuId);
    }

    public boolean modifyMenu(long menuId, MenuDto menuDto) {
        return menuRepository.updateMenu(menuId, menuDto);
    }

    public boolean modifySubmenu(long submenuId, SubmenuDto submenuDto) {
        return menuRepository.updateSubmenu(submenuId, submenuDto);
    }
}
