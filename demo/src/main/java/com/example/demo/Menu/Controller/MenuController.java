package com.example.demo.Menu.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Menu.Entity.MenuDto;
import com.example.demo.Menu.Entity.SubmenuDto;
import com.example.demo.Menu.Service.MenuService;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> insertMenus(@RequestBody Map<String, List<MenuDto>> payload) {
        Map<String, Object> report = menuService.insertRoleMenus(payload);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{role}")
    public ResponseEntity<List<MenuDto>> getMenusByRole(@PathVariable String role) {
        List<MenuDto> menus = menuService.getMenusForRole(role);
        return ResponseEntity.ok(menus);
    }
    
    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable long menuId) {
        boolean deleted = menuService.removeMenu(menuId);
        if (deleted) {
            return ResponseEntity.ok("Menu and its submenus deleted successfully.");
        }
        return ResponseEntity.status(404).body("Menu not found.");
    }

    @DeleteMapping("/submenus/{submenuId}")
    public ResponseEntity<String> deleteSubmenu(@PathVariable long submenuId) {
        boolean deleted = menuService.removeSubmenu(submenuId);
        if (deleted) {
            return ResponseEntity.ok("Submenu deleted successfully.");
        }
        return ResponseEntity.status(404).body("Submenu not found.");
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<String> updateMenu(@PathVariable long menuId, @RequestBody MenuDto menuDto) {
        boolean updated = menuService.modifyMenu(menuId, menuDto);
        if (updated) {
            return ResponseEntity.ok("Menu updated successfully.");
        }
        return ResponseEntity.status(404).body("Menu not found.");
    }

    @PutMapping("/submenus/{submenuId}")
    public ResponseEntity<String> updateSubmenu(@PathVariable long submenuId, @RequestBody SubmenuDto submenuDto) {
        boolean updated = menuService.modifySubmenu(submenuId, submenuDto);
        if (updated) {
            return ResponseEntity.ok("Submenu updated successfully.");
        }
        return ResponseEntity.status(404).body("Submenu not found.");
    }
}