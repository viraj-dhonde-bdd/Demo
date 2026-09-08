package com.example.demo.Menu.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Menu.Entity.Menu;
import com.example.demo.Menu.Service.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/Home")
    public  List<Menu> home(Model model) {
        List<String> userRoles = List.of("ADMIN"); // Change to "USER" to test restriction
        
        List<Menu> menus = menuService.getHierarchicalMenusForRoles(userRoles);
        model.addAttribute("menus", menus);
        
        return menus;
    }
}