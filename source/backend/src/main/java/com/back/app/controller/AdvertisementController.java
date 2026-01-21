package com.back.app.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.back.app.model.AdverNoJoin;
import com.back.app.model.Advertisement;
import com.back.app.service.AdvertisementService;
import com.back.app.service.DateInterval;
import com.back.app.service.ImageFolder;
import com.back.app.service.ImageStorageService;
import com.back.app.service.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Advertisements", description = "Operations related to advertisement listings, searching, and filtering.")
@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdvertisementController {
  @Autowired
  private final AdvertisementService advertisementService;
  private final ReservationService reservationService;
  private final ImageStorageService imageStorageService;

  @Operation(summary = "Retrieve all advertisements", description = "Returns a comprehensive, unfiltered list of all active advertisement listings.")
  @GetMapping("/")
  public ResponseEntity<List<Advertisement>> getAllAdvertisements() {
    List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
    advertisements.forEach((ad) -> advertisementService.hideSensitiveData(ad));
    return ResponseEntity.ok(advertisements);
  }

  @Operation(summary = "Retrieve advertisements by ID of trader", description = "")
  @GetMapping("/account/{id}")
  public ResponseEntity<List<Advertisement>> getAllAdvertisementsType(@PathVariable Integer id) {
    List<Advertisement> advertisements = advertisementService.getAllAdvertisementsByTrader(id);
    advertisements.forEach((ad) -> advertisementService.hideSensitiveData(ad));
    return ResponseEntity.ok(advertisements);
  }

  @Operation(summary = "Retrieve advertisement by ID", description = "Returns the details for a specific advertisement ID.")

  @GetMapping("/{id}")
  public ResponseEntity<Advertisement> getMethodName(@PathVariable Integer id) {
    Advertisement ad = advertisementService.getAdvertisementbyId(id);
    if (ad != null) {
      ad = advertisementService.hideSensitiveData(ad);
      return ResponseEntity.ok().body(ad);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Search and Filter Advertisements", description = "Returns advertisements matching the specified search criteria. All parameters are optional and combined using AND logic.")
  @GetMapping("/search")
  public ResponseEntity<List<Advertisement>> searchAdvertisements(
      @RequestParam(required = false) String itemName,
      @RequestParam(required = false) Integer categoryId,
      @RequestParam(required = false) LocalDate beginDate,
      @RequestParam(required = false) LocalDate endDate,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice) {
    DateInterval interval = new DateInterval(beginDate, endDate);

    log.info("Received search request - categoryId: {}, beginDate: {}, endDate: {}, minPrice: {}, maxPrice: {}",
        categoryId, beginDate, endDate, minPrice, maxPrice);

    if (endDate != null && beginDate != null) {
      List<Advertisement> advertisements = advertisementService.getFilteredAdvertisements(
          itemName, categoryId, beginDate, endDate, minPrice, maxPrice).stream()
          .filter(ad -> reservationService.isReservationIntervalFreeForAd(interval, ad.getAdvertisementId()))
          .collect(Collectors.toList());
      return ResponseEntity.ok().body(advertisements);
    }
    List<Advertisement> advertisements = advertisementService.getFilteredAdvertisements(
        itemName, categoryId, beginDate, endDate, minPrice, maxPrice).stream()
        .filter(ad -> !reservationService.hasNoHoles(ad.getAdvertisementId()))
        .collect(Collectors.toList());
    return ResponseEntity.ok().body(advertisements);
  }

  @PostMapping("/create")
  public ResponseEntity<String> createNewAdvertisement(@RequestBody String newAdvertisementString) {
    try {
      log.info("Received advertisement: {}", newAdvertisementString);

      AdverNoJoin advertisement = AdverNoJoin.convertToAdverNoJoin(newAdvertisementString);

      advertisementService.saveAdverNoJoin(advertisement);
      return ResponseEntity.ok().body(advertisement.getAdvertisementId().toString());

    } catch (JsonProcessingException e) {
      log.error("JSON parsing error: {}", e.getMessage());
      return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
    } catch (Exception e) {
      log.error("Unexpected error: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

          .body("Error: " + e.getMessage());
    }
  }

  @PostMapping("/edit/{id}")
  public ResponseEntity<String> editAdvertisement(@PathVariable Integer id,
      @RequestBody String newAdvertisementString) {
    try {
      log.info("Received advertisement: {}", newAdvertisementString);

      AdverNoJoin advertisement = AdverNoJoin.convertToAdverNoJoin(newAdvertisementString);

      advertisementService.updateAdverNoJoin(id, advertisement);
      return ResponseEntity.ok().body(advertisement.getAdvertisementId().toString());

    } catch (JsonProcessingException e) {
      log.error("JSON parsing error: {}", e.getMessage());
      return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
    } catch (Exception e) {
      log.error("Unexpected error: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

          .body("Error: " + e.getMessage());
    }
  }

  @PostMapping("/delete/{id}")
  public ResponseEntity<String> deleteAdvertisement(@PathVariable Integer id) {
    try {
      advertisementService.deleteAccountById(id);
    } catch (Exception e) {
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error deleting advertisement");
    }
    return ResponseEntity.ok().body("Succesfuly deleted advertisement with id " + id.toString());
  }

  @PostMapping("/images/store/{id}")
  public ResponseEntity<Map<String, String>> storeItemImage(
      @RequestParam("file") MultipartFile file,
      @PathVariable Integer id) {

    try {
      Advertisement ad = advertisementService.getAdvertisementbyId(id);
      if (ad == null) {
        log.error("Error loading image for advertisement {}: Advertisement doesn't exist or has no image\"", id);
        return ResponseEntity.notFound().build();
      }

      String filename = "item" + id.toString();
      String filename_ext = imageStorageService.storeImage(file, filename, ImageFolder.ADVERTISEMENT);

      ad.setItemImagePath("/" + ImageFolder.ADVERTISEMENT.getFolderName() + "/" + filename_ext);
      advertisementService.saveAdvertisement(ad);

      Map<String, String> response = new HashMap<>();
      response.put("filename", filename_ext);
      response.put("originalName", file.getOriginalFilename());
      response.put("size", String.valueOf(file.getSize()));
      response.put("contentType", file.getContentType());
      response.put("url", "/api/advertisements/images/load/" + id);

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", e.getMessage()));
    }
  }

  @GetMapping("/images/load/{id}")
  public ResponseEntity<Resource> getItemImage(@PathVariable Integer id) {
    try {
      Advertisement ad = advertisementService.getAdvertisementbyId(id);
      if (ad == null || ad.getItemImagePath() == null) {
        log.error("Error loading image for advertisement {}: Advertisement doesn't exist", id);
        return ResponseEntity.notFound().build();
      }

      String filename = ad.getItemImagePath().replace("/" + ImageFolder.ADVERTISEMENT.getFolderName() + "/", "");
      log.info("Loading profile image: {}", filename);

      Resource resource = imageStorageService.loadImage(filename, ImageFolder.ADVERTISEMENT);

      Path filePath = imageStorageService.getUploadDir(ImageFolder.ADVERTISEMENT).resolve(filename);
      String contentType = Files.probeContentType(filePath);

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=\"" + resource.getFilename() + "\"")
          .body(resource);
    } catch (Exception ex) {
      log.error("Error loading image for advertisement {}: {}", id, ex.getMessage());
      return ResponseEntity.internalServerError().build();
    }
  }

}
