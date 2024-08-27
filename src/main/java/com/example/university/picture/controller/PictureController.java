package com.example.university.picture.controller;

import com.example.university.picture.model.GalleryDTO;
import com.example.university.picture.model.PictureDTO;
import com.example.university.picture.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/universities/pictures")
public class PictureController {

    private final PictureService pictureService;

    @GetMapping("/{universityId}")
    public GalleryDTO findAllByUniversityId(@PathVariable Long universityId) {
        return pictureService.getPictures(universityId);
    }

    @PostMapping("/save/{universityId}")
    public PictureDTO savePicture(@RequestBody PictureDTO pictureDTO, @PathVariable Long universityId) {
        return pictureService.savePicture(pictureDTO, universityId);
    }

    @DeleteMapping("/{imageId}/delete")
    public void deletePicture(@PathVariable Long imageId) {
        pictureService.deletePicture(imageId);
        return;
    }
}

