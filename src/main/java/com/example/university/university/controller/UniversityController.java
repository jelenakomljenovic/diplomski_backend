package com.example.university.university.controller;

import com.example.university.email.EmailService;
import com.example.university.pdfFile.PdfFileGenerator;
import com.example.university.pdfFile.model.PdfRequest;
import com.example.university.university.OSMService;
import com.example.university.university.model.University;
import com.example.university.university.model.UniversityKeyWordsRequest;
import com.example.university.university.model.UpdateImageRequest;
import com.example.university.university.service.UniversityService;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/universities")
public class UniversityController {

    private final UniversityService universityService;

    private final EmailService emailService;


    @GetMapping
    public ResponseEntity<List<University>> getAllFaculties() {
        List<University> facultiesList = universityService.findAll();
        return ResponseEntity.ok(facultiesList);
    }

    @GetMapping("/countries")
    public ResponseEntity<Set<String>> getAllCountries() {
        Set<String> countriesList = universityService.getAllCountries();
        return ResponseEntity.ok(countriesList);
    }

    @PostMapping("/cities")
    public ResponseEntity<Set<String>> getAllCities(@RequestBody Set<String> countries) {
        Set<String> citiesList = universityService.getAllCities(countries);
        return ResponseEntity.ok(citiesList);
    }

    private final OSMService osmService;


    @PostMapping("/insert")
    public ResponseEntity<University> addUniversity(@RequestBody University university) {
        University university1 = universityService.insertUniversity(university);
        return ResponseEntity.ok(university1);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable Long id, @RequestBody University university) {
        University university1 = universityService.updateUniversity(id, university);
        return ResponseEntity.ok(university1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<University> findUniversityById(@PathVariable Long id) {
        University university1 = universityService.findById(id);
        return ResponseEntity.ok(university1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/insert/keywords")
    public ResponseEntity<Void> insertKeyWords(@RequestBody UniversityKeyWordsRequest universityKeyWordsRequest) {
        universityService.updateUniversityKeywords(universityKeyWordsRequest);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/filter")
    public ResponseEntity<List<University>> getAllFacultiesByKeyWords(@RequestBody List<String> keywords) {
        List<University> facultiesList = universityService.filterByKeyWords(keywords);
        return ResponseEntity.ok(facultiesList);
    }

    private static final String UPLOAD_DIR = "C://Users//OSD-student1//Desktop//diplomski_rad//frontend//public//images";

    public String formatName(String name) {
        return name.toLowerCase().replaceAll(" ", "-");
    }


    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        University university1 = universityService.findById(id);

        String formattedName = formatName(university1.getName());
        String formattedCity = formatName(university1.getCity());
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();

            String fileExtension = StringUtils.getFilenameExtension(originalFileName);

            String fileName = formattedName + "-" + formattedCity + ".jpg";


            Path filePath = uploadPath.resolve(fileName);

            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File saved: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file");
        }
    }
    @PostMapping("/updateImage")
    public ResponseEntity<String> renameImageIfExists(@RequestBody UpdateImageRequest updateImageRequest) {
        String oldFileName = updateImageRequest.getOldName().toLowerCase().replaceAll("\\s+", "-") + "-" + updateImageRequest.getOldCity().toLowerCase().replaceAll("\\s+", "-") + ".jpg";
        String newFileName = updateImageRequest.getNewName().toLowerCase().replaceAll("\\s+", "-") + "-" + updateImageRequest.getNewCity().toLowerCase().replaceAll("\\s+", "-") + ".jpg";

        File oldFile = new File(UPLOAD_DIR + "//" + oldFileName);
        File newFile = new File(UPLOAD_DIR + "//" + newFileName);

        // Proveri da li fajl postoji
        if (oldFile.exists()) {
            // Pokušaj preimenovanja
            boolean success = oldFile.renameTo(newFile);
            if (success) {
                return ResponseEntity.ok("File saved: " + newFile.toString());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file!");
            }
        } else {
            return ResponseEntity.ok("Image doesn't exists!");
        }
    }

    @Autowired
    private PdfFileGenerator pdfGeneratorService;

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestBody PdfRequest pdfRequest) throws Exception {
        // Kreirajte PDF koristeći podatke iz pdfRequest
        byte[] pdfBytes = pdfGeneratorService.generatePdf(pdfRequest.getFirstName(), pdfRequest.getLastName(), pdfRequest.getAddress(), pdfRequest.getEmail(), pdfRequest.getAbilities(), pdfRequest.getSkills(), pdfRequest.getPredictionResult());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/send-pdf")
    public ResponseEntity<String> sendPdfEmail(
            @RequestBody PdfRequest pdfRequest) {
        try {
            // Generate PDF
            byte[] pdfData = pdfGeneratorService.generatePdf(pdfRequest.getFirstName(), pdfRequest.getLastName(), pdfRequest.getAddress(), pdfRequest.getEmail(), pdfRequest.getAbilities(), pdfRequest.getSkills(), pdfRequest.getPredictionResult());

            // Send email with PDF attachment
            emailService.sendPdfEmail(
                    pdfRequest.getTo(),
                    pdfRequest.getSubject(),
                    "Za pregled rezultata preuzmite PDF dokument.",
                    pdfData,
                    "Report.pdf"
            );

            return ResponseEntity.ok("Email sent successfully");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while generating PDF: " + e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while sending email: " + e.getMessage());
        }
    }

    @PostMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadPdf(@RequestBody PdfRequest pdfRequest) {
        try {
            // Generišemo PDF koristeći podatke iz pdfRequest
            byte[] pdfBytes = pdfGeneratorService.generatePdf(
                    pdfRequest.getFirstName(),
                    pdfRequest.getLastName(),
                    pdfRequest.getAddress(),
                    pdfRequest.getEmail(),
                    pdfRequest.getAbilities(),
                    pdfRequest.getSkills(),
                    pdfRequest.getPredictionResult()
            );

            // Kreiramo ByteArrayResource iz PDF bajtova
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            // Postavljamo HTTP zaglavlja za preuzimanje datoteke
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=university_results.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            // Vraćamo ResponseEntity s PDF datotekom
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .body(resource);

        } catch (IOException | DocumentException e) {
            // Logujemo grešku i vraćamo INTERNAL_SERVER_ERROR status
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/map-data")
    public ResponseEntity<List<University>> findAllUniversitiesByCity(@RequestBody List<String> universities){
        List<University> result = universityService.findUniversitiesByCity(universities, "Banja Luka");
        return ResponseEntity.ok(result);
    }


    @PostMapping("/find-by-city/{city}")
    public ResponseEntity<Set<University>> getAllByCity(@RequestBody Set<String> universities, @PathVariable String city) {
        Set<University> universities1 = universityService.getUniversitiesByCity(universities, city);
        return ResponseEntity.ok(universities1);
    }



}
