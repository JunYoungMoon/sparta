package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByUser(User user, Pageable pageable);

//  언더스코어(_)의 역할은 JPA 메서드 이름에서 언더스코어는 연관 관계를 탐색하는 역할을 한다.
//  Product 엔티티에서 productFolderList는 @OneToMany 관계로 매핑된 연관 엔티티다.
//  ProductFolderList_FolderId는 Product → productFolderList라는 연관 엔티티 리스트로 이동.
//  그 리스트의 folderId 필드로 접근을 의미한다.

//  JPQL로도 작성이 가능하다.
//  @Query("SELECT p FROM Product p JOIN p.productFolderList pf WHERE p.user = :user AND pf.folderId = :folderId")
//  Page<Product> findAllByUserAndFolderId(@Param("user") User user, @Param("folderId") Long folderId, Pageable pageable);
    Page<Product> findAllByUserAndProductFolderList_FolderId(User user, Long folderId, Pageable pageable);
}
