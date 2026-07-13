package com.example.urlshortener.repository;

import com.example.urlshortener.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

    List<ClickEvent> findByUrlMappingIdOrderByClickedAtDesc(Long urlMappingId);
}
