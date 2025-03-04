package com.ana29.deliverymanagement.externalApi.aistudio.repository;

import com.ana29.deliverymanagement.externalApi.aistudio.entity.Gemini;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface GeminiRepository extends JpaRepository<Gemini, UUID> {
    // 삭제되지 않은 행만 조회하기 위한 메소드 (필요 시)
    Optional<Gemini> findByIdAndIsDeletedFalse(UUID id);
}
