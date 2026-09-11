package com.example.demo.Menu.MenuRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Menu.Entity.Menu1;

@Repository
public interface MenuInsertRepository extends JpaRepository<Menu1, Long> {
	boolean existsByTitle(String title);
}
