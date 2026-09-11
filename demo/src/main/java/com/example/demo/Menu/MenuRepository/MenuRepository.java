package com.example.demo.Menu.MenuRepository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Menu.Entity.MenuDto;
import com.example.demo.Menu.Entity.SubmenuDto;

@Repository
public class MenuRepository {

    private final JdbcTemplate jdbcTemplate;

    public MenuRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Map<String, Object> insertRoleMenus(Map<String, List<MenuDto>> payload) {
        String checkMenuSql = "SELECT menu_id FROM bo_menu WHERE role = ? AND title = ?";
        String insertMenuSql = "INSERT INTO bo_menu (role, title, url, icon) VALUES (?, ?, ?, ?)";
        
        String checkSubmenuSql = "SELECT COUNT(*) FROM bo_submenu WHERE menu_id = ? AND title = ?";
        String insertSubmenuSql = "INSERT INTO bo_submenu (menu_id, title, url, icon) VALUES (?, ?, ?, ?)";

        int menusInserted = 0;
        int menusSkipped = 0;
        int submenusInserted = 0;
        int submenusSkipped = 0;

        for (Map.Entry<String, List<MenuDto>> entry : payload.entrySet()) {
            String role = entry.getKey();
            for (MenuDto menu : entry.getValue()) {
                
                Long menuId = null;
                try {
                    menuId = jdbcTemplate.queryForObject(checkMenuSql, Long.class, role, menu.getTitle());
                } catch (org.springframework.dao.EmptyResultDataAccessException e) {
                    // Not found
                }

                if (menuId == null) {
                    KeyHolder keyHolder = new GeneratedKeyHolder();
                    jdbcTemplate.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(insertMenuSql, new String[]{"MENU_ID"});
                        ps.setString(1, role);
                        ps.setString(2, menu.getTitle());
                        ps.setString(3, menu.getUrl());
                        ps.setString(4, menu.getIcon());
                        return ps;
                    }, keyHolder);

                    Number generatedId = keyHolder.getKey();
                    if (generatedId == null) {
                        throw new RuntimeException("Failed to insert menu: " + menu.getTitle());
                    }
                    menuId = generatedId.longValue();
                    menusInserted++;
                } else {
                    menusSkipped++;
                }

                if (menu.getSubmenus() != null) {
                    for (SubmenuDto submenu : menu.getSubmenus()) {
                        Integer count = jdbcTemplate.queryForObject(
                            checkSubmenuSql, 
                            Integer.class, 
                            menuId, 
                            submenu.getTitle()
                        );

                        if (count == null || count == 0) {
                            jdbcTemplate.update(
                                insertSubmenuSql,
                                menuId,
                                submenu.getTitle(),
                                submenu.getUrl(),
                                submenu.getIcon()
                            );
                            submenusInserted++;
                        } else {
                            submenusSkipped++;
                        }
                    }
                }
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("message", "Menu processing completed.");
        report.put("menusInserted", menusInserted);
        report.put("menusSkippedExisting", menusSkipped);
        report.put("submenusInserted", submenusInserted);
        report.put("submenusSkippedExisting", submenusSkipped);
        return report;
    }

    public List<MenuDto> findMenusByRole(String role) {
        String sql = """
            SELECT m.menu_id AS m_id, m.title AS m_title, m.url AS m_url, m.icon AS m_icon,
                   s.sm_id AS s_id, s.title AS s_title, s.url AS s_url, s.icon AS s_icon
            FROM bo_menu m
            LEFT JOIN bo_submenu s ON m.menu_id = s.menu_id
            WHERE m.role = ?
        """;

        Map<Long, MenuDto> menuMap = new LinkedHashMap<>();

        jdbcTemplate.query(sql, rs -> {
            long menuId = rs.getLong("m_id");
            MenuDto menu = menuMap.get(menuId);

            if (menu == null) {
                menu = new MenuDto();
                menu.setTitle(rs.getString("m_title"));
                menu.setUrl(rs.getString("m_url"));
                menu.setIcon(rs.getString("m_icon"));
                menu.setSubmenus(new ArrayList<>());
                menuMap.put(menuId, menu);
            }

            long submenuId = rs.getLong("s_id");
            if (!rs.wasNull()) {
                SubmenuDto submenu = new SubmenuDto();
                submenu.setTitle(rs.getString("s_title"));
                submenu.setUrl(rs.getString("s_url"));
                submenu.setIcon(rs.getString("s_icon"));
                menu.getSubmenus().add(submenu);
            }
        }, role);

        return new ArrayList<>(menuMap.values());
    }
}