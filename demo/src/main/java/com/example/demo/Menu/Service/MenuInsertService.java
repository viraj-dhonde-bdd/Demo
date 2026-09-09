package com.example.demo.Menu.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Menu.Entity.Menu;
import com.example.demo.Menu.MenuRepository.MenuInsertRepository;

@Service
public class MenuInsertService {

	@Autowired
	private MenuInsertRepository menuInsertRepository;

	public Menu saveMenuTree(Menu menu) {
        // Check main menu title
        if (menuInsertRepository.existsByTitle(menu.getTitle())) {
            throw new IllegalArgumentException("A menu with the title '" + menu.getTitle() + "' already exists.");
        }
        // Also check submenus if needed...
        validateSubmenuTitles(menu.getSubmenus());

        linkSubmenus(menu, null);
        return menuInsertRepository.save(menu);
    }

    private void validateSubmenuTitles(List<Menu> submenus) {
        if (submenus == null) return;
        for (Menu sub : submenus) {
            if (menuInsertRepository.existsByTitle(sub.getTitle())) {
                throw new IllegalArgumentException("A menu with the title '" + sub.getTitle() + "' already exists.");
            }
            validateSubmenuTitles(sub.getSubmenus());
        }
    }

    private void linkSubmenus(Menu current, Menu parent) {
        current.setParent(parent);
        if (current.getSubmenus() != null) {
            for (Menu child : current.getSubmenus()) {
                linkSubmenus(child, current);
            }
        }
    }
}