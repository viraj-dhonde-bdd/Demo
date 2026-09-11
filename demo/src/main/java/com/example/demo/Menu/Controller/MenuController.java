package com.example.demo.Menu.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Menu.Entity.MenuDto;
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
}