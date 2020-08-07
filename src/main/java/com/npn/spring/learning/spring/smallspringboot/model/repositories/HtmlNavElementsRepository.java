package com.npn.spring.learning.spring.smallspringboot.model.repositories;

import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DAO для элементов меню
 */
public interface HtmlNavElementsRepository extends JpaRepository<HtmlNavElement, Long> {
    HtmlNavElement findFirstByNameOrderByElementOrder(String name);

    List<HtmlNavElement> findByParentIsNullOrderByElementOrder();


}
