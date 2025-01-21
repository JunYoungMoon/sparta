package com.sparta.springprepare.memo.repository;

import com.sparta.springprepare.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByOrderByModifiedAtDesc();

    List<Memo> findAllByUsername(String username);

    List<Memo> findAllByUsernameAndContents(String username, String contents);

    List<Memo> findByContentsContainingOrderByModifiedAtDesc(String content);
}