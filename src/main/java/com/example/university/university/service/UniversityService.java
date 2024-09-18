package com.example.university.university.service;

import com.example.university.department.model.Department;
import com.example.university.department.repository.DepartmentRepository;
import com.example.university.department.service.DepartmentService;
import com.example.university.exception.UniversityNotFoundException;
import com.example.university.picture.model.Picture;
import com.example.university.picture.repository.PictureRepository;
import com.example.university.picture.service.PictureService;
import com.example.university.university.Location;
import com.example.university.university.OSMService;
import com.example.university.university.model.University;
import com.example.university.university.model.UniversityKeyWordsRequest;
import com.example.university.university.repository.UniversityRepository;
import com.example.university.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UniversityService {

    private final UniversityRepository universityRepository;

    private final DepartmentRepository departmentRepository;

    private final DepartmentService departmentService;

    private final PictureRepository pictureRepository;

    private final PictureService pictureService;

    private final UserRepository userRepository;

    private final OSMService osmService;

    private final JdbcTemplate jdbcTemplate;


    public List<University> findAll() {
        return universityRepository.findAll();
    }

    public Set<String> getAllCountries() {
        Set<String> countries = new HashSet<>();
        universityRepository.findAll().stream().forEach(faculty -> countries.add(faculty.getCountry()));
        return countries;
    }

    public Set<String> getAllCities(Set<String> countries) {
        Set<String> cities = new HashSet<>();
        universityRepository.findAll().stream().filter(faculty -> countries.contains(faculty.getCountry())).forEach(faculty -> cities.add(faculty.getCity()));
        return cities;
    }

    public Set<University> getUniversitiesByCity(Set<String> universities, String city) {
        Set<University> result = new HashSet<>();
        List<University> allUniversities = findAll();

        for (University u : allUniversities) {
            if (universities.contains(u.getName()) && u.getCity().equals(city)) {
                result.add(u);
            }
        }

        if (result.isEmpty()) {
            for (University u : allUniversities) {
                for (String universityName : universities) {
                    if (u.getSecondaryName() != null &&
                            u.getSecondaryName().equalsIgnoreCase(universityName) &&
                            u.getCity().equals(city)) {
                        result.add(u);
                    }
                }
            }
        }

        if (result.isEmpty()) {
            for (University u : allUniversities) {
                if (universities.contains(u.getName())) {
                    result.add(u);
                }
            }
        }

        return result;
    }


    public University insertUniversity(University university) {
        University university1 = new University();

        university1.setAddress(university.getAddress());
        university1.setCity(university.getCity());
        university1.setCountry(university.getCountry());

        university1.setName(university.getName());
        university1.setEmail(university.getEmail());
        university1.setPhoneNumber(university.getPhoneNumber());
        university1.setWebsite(university.getWebsite());
        university1.setClassification(university.getClassification());
        university1.setType(true);

        Location location = osmService.fetchLocation(university.getName() + ", " + university.getAddress() + ", " + university.getCity());
        university1.setCoordinates(location.getLat() + ", " + location.getLon());
        university1.setPostalCode(location.getAddress().getPostcode());

        return universityRepository.saveAndFlush(university1);
    }

    public University updateUniversity(Long universityId, University university) {
        University university1 = universityRepository.findById(universityId).orElseThrow(() -> new UniversityNotFoundException(universityId));

        university1.setAddress(university.getAddress());
        university1.setCity(university.getCity());
        university1.setCountry(university.getCountry());

        university1.setName(university.getName());
        university1.setEmail(university.getEmail());
        university1.setPhoneNumber(university.getPhoneNumber());
        university1.setWebsite(university.getWebsite());
        university1.setClassification(university.getClassification());
        university1.setType(true);

        Location location = osmService.fetchLocation(university.getName() + ", " + university.getAddress() + ", " + university.getCity());
        university1.setCoordinates(location.getLat() + ", " + location.getLon());
        university1.setPostalCode(location.getAddress().getPostcode());

        return universityRepository.saveAndFlush(university1);
    }

    public University findById(Long id) {
        University university = universityRepository.findById(id).orElseThrow(() -> new UniversityNotFoundException(id));
        return university;
    }

    public void deleteUniversity(Long id) {
        List<Department> departments = departmentRepository.findByUniversity_Id(id);
        for (Department d : departments) {
            departmentService.deleteDepartment(d.getId());
        }

        List<Picture> pictures = pictureRepository.findAllByUniversity_IdOrderByIdDesc(id);
        for (Picture p : pictures) {
            pictureService.deletePicture(p.getId());
        }
        universityRepository.deleteById(id);
    }

    public void updateUniversityKeywords(UniversityKeyWordsRequest universityKeyWordsRequest) {
        String sql = "UPDATE university SET key_words = ? WHERE name = ?";
        jdbcTemplate.update(sql, universityKeyWordsRequest.getKeyWords(), universityKeyWordsRequest.getName());
    }

    public List<University> filterByKeyWords(List<String> keywords) {
        List<University> universities = findAll();

        return universities.stream()
                .filter(university -> university.getKeyWords() != null)
                .filter(university -> Arrays.stream(university.getKeyWords().split(","))
                        .map(String::trim)
                        .anyMatch(keyword -> keywords.contains(keyword)))
                .collect(Collectors.toList());
    }

    public List<University> findUniversitiesByCity(List<String> universities, String city) {
        List<University> allUniversities = findAll();
        List<University> result = new ArrayList<>();

        for (University u : allUniversities) {
            for (String s : universities) {
                if (u.getName().equals(s)) {
                    if (u.getCity().equals(city)) {
                        result.add(u);
                    }
                }
            }
        }
        return result;
    }


}
