package com.project_lions.back.repository;

import com.project_lions.back.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByTagName(String tagName);
}
