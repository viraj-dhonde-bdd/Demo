package com.example.demo.Menu.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MenuDto {
    private String title;
    private String url;
    private String icon;
    private List<SubmenuDto> submenus;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public List<SubmenuDto> getSubmenus() { return submenus; }
    public void setSubmenus(List<SubmenuDto> submenus) { this.submenus = submenus; }
}
