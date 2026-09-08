package com.example.demo.Menu.MenuRepository;


import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.Menu.Entity.Menu;

@Repository
public class MenuRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Menu> menuRowMapper = (rs, rowNum) -> {
        Menu menu = new Menu();
        menu.setId(rs.getLong("id"));
        menu.setTitle(rs.getString("title"));
        menu.setUrl(rs.getString("url"));
        //menu.setIcon(rs.getString("icon"));
        
        Long parentId = rs.getObject("parent_id") != null ? rs.getLong("parent_id") : null;
        if (parentId != null) {
            Menu parent = new Menu();
            parent.setId(parentId);
            menu.setParent(parent);
        }
        
        return menu;
    };

    public List<Menu> findMenusByRoleNames(List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Collections.emptyList();
        }

        //String sql = "SELECT DISTINCT m.id, m.title, m.url, m.icon, m.parent_id " +
        String sql = "SELECT DISTINCT m.id, m.title, m.url, m.parent_id " +
                     "FROM menus m " +
                     "JOIN role_menus rm ON m.id = rm.menu_id " +
                     "JOIN roles r ON rm.role_id = r.id " +
                     "WHERE r.name IN (:roleNames)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("roleNames", roleNames);

        return namedParameterJdbcTemplate.query(sql, parameters, menuRowMapper);
    }
}