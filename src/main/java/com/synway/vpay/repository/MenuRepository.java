package com.synway.vpay.repository;

import com.synway.vpay.base.repository.BaseRepository;
import com.synway.vpay.entity.Menu;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MenuRepository extends BaseRepository<Menu, UUID> {

    public MenuRepository(EntityManager entityManager) {
        super(Menu.class, entityManager);
    }

    @Query("select m from Menu m where m.parentId is null")
    public List<Menu> findRoots() {
        return entityManager.createQuery("select m from Menu m where m.parentId is null", Menu.class)
                .getResultList();
    }
}
