package com.locboy.locboybackend.repositories;

import com.locboy.locboybackend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long>{
}
