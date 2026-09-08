package com.example.demo.Menu.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
    private String title;
    private String url;
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> submenus = new ArrayList<>();

    // If you specifically need the parent's ID in your business logic, 
    // you can extract it via a helper method:
    public Long getParentId() {
        return parent != null ? parent.getId() : null;
    }
    
 // 1. Put @JsonIgnore HERE on the getter to stop Jackson from serializing the full parent object
    @JsonIgnore
    public Menu getParent() {
        return parent;
    }
    
}
