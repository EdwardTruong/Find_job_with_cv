package com.woking.demo.dao;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.woking.demo.entity.CvEntity;

@Repository
public interface CvDao extends JpaRepository<CvEntity, Integer> {

    @Query("SELECT c FROM CvEntity c WHERE c.user.id = :userId")
    List<CvEntity> findByUserId(@Param("userId")Integer userId);

    Optional<CvEntity> findByName(String name);

}
