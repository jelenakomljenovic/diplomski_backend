package com.example.university.picture.service;

import com.example.university.exception.UniversityNotFoundException;
import com.example.university.picture.model.GalleryDTO;
import com.example.university.picture.model.Picture;
import com.example.university.picture.model.PictureDTO;
import com.example.university.picture.repository.PictureRepository;
import com.example.university.university.model.University;
import com.example.university.university.repository.UniversityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PictureService {

    private final PictureRepository pictureRepository;

    private final ModelMapper modelMapper;

    private final UniversityRepository universityRepository;

    public GalleryDTO getPictures(Long universityId) {
        List<Picture> pictureList = pictureRepository.findAllByUniversity_IdOrderByIdDesc(universityId);

        return createGalleryDTO(pictureList);
    }

    public GalleryDTO createGalleryDTO(List<Picture> pictureList) {
        GalleryDTO galleryDTO = new GalleryDTO();

        List<PictureDTO> pictureDTOList = pictureList.stream().map(picture -> {
            PictureDTO pictureDTO = modelMapper.map(picture, PictureDTO.class);
            pictureDTO.setPictureBase64(new String(picture.getPictureBase64()));
            return pictureDTO;
        }).collect(Collectors.toList());

        galleryDTO.setGallery(pictureDTOList);

        return galleryDTO;
    }

    public PictureDTO savePicture(PictureDTO pictureDTO, Long universityId) {
        Picture pictureToSave = new Picture();
        pictureToSave.setPictureBase64(pictureDTO.getPictureBase64().getBytes(StandardCharsets.UTF_8));

        University university = universityRepository.findById(universityId).orElseThrow(() -> new UniversityNotFoundException(universityId));
        pictureToSave.setUniversity(university);

        pictureToSave = pictureRepository.save(pictureToSave);

        PictureDTO pictureDTOReturn = new PictureDTO();
        pictureDTOReturn.setPictureBase64(new String(pictureToSave.getPictureBase64()));
        pictureDTOReturn.setId(pictureToSave.getId());
        return pictureDTOReturn;
    }

    @Transactional
    public void deletePicture(Long imageId) {
        pictureRepository.deleteById(imageId);
    }





}
