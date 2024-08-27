package com.example.university.picture.repository;

import com.example.university.picture.model.Picture;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findAllByUniversity_IdOrderByIdDesc(Long universityId);


}

