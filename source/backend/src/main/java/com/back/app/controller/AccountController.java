package com.back.app.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.back.app.model.Account;
import com.back.app.service.AccountService;
import com.back.app.service.ImageFolder;
import com.back.app.service.ImageStorageService;
import com.back.app.service.OAuthRoleService;
import com.back.app.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Accounts", description = "Manages user accounts, including retrieval by ID and for the currently authenticated user.")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountController {

  @Value("${app.frontend.url}")
  String CLIENT_BASE_URL;

  private final AccountService accountService;
  private final OAuthRoleService oAuthRoleService;
  private final PaymentService paymentService;
  private final ImageStorageService imageStorageService;

  @Operation(summary = "Retrieve all accounts", description = "Returns a comprehensive list of all registered accounts.")
  @GetMapping("/")
  public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok().body(accountService.getAllAccounts());
  }

  @Operation(summary = "Retrieve account by ID", description = "Returns the account details for a specific account ID.")
  @GetMapping("/{id}")
  public ResponseEntity<Account> getMethodName(@PathVariable Integer id) {
    return ResponseEntity.ok().body(accountService.getUserbyId(id));
  }

  @Operation(summary = "Retrieve current user account", description = "Returns the account details associated with the principal "
      +
      "of the currently logged-in user (based on OAuth2 ID). Requires TRADER, BUYER, or ADMIN role.")
  @Secured({ "ROLE_TRADER", "ROLE_BUYER", "ROLE_ADMIN" })
  @GetMapping("/current")
  public Account getCurrentUserAccount(HttpServletRequest request) {

    String oauth2Id = request.getUserPrincipal().getName();
    return accountService.getAccountByOAuth2Id(oauth2Id);

  }

  @GetMapping("/current_id")
  public Integer getCurrentUserId(HttpServletRequest request) {

    String oauth2Id = request.getUserPrincipal().getName();
    return accountService.getAccountByOAuth2Id(oauth2Id).getAccountId();

  }

  @PostMapping("/create/buyer")
  public void createBuyer(@RequestBody String newAccountString,
      @AuthenticationPrincipal OAuth2User oauth2User,
      HttpServletRequest request,
      HttpServletResponse response) {

    try {
      String accessToken = oAuthRoleService.getCurrentUserAccessToken();

      if (accessToken == null) {
        return;
      }

      String userEmail = oAuthRoleService.fetchEmailWithAccessToken(oauth2User, accessToken);

      if (userEmail == null) {

        return;
      }

      String oauth2Id = oauth2User.getName();
      log.info("Extracted - OAuth2 ID: {}, Email: {}", oauth2Id, userEmail);

      Account newAccount = Account.convertToAccount(newAccountString);
      newAccount.setOauth2Id(oauth2Id);
      newAccount.setUserEmail(userEmail);

      if (newAccount.getRegistrationDate() == null) {
        newAccount.setRegistrationDate(LocalDate.now());
      }

      newAccount.setAccountRole("buyer");

      accountService.saveAccount(newAccount);
      log.info("Account created successfully for: {}", userEmail);

      accountService.logoutUser(request, response);

    } catch (Exception e) {
      log.error("Unexpected error: {}", e.getMessage(), e);
    }

  }

  @PostMapping("/create/trader")
  public String createTrader(@RequestBody String newAccountString,
      @AuthenticationPrincipal OAuth2User oauth2User,
      HttpServletRequest request,
      HttpServletResponse response) {

    try {
      String accessToken = oAuthRoleService.getCurrentUserAccessToken();

      if (accessToken == null) {
        return "/";
      }

      String userEmail = oAuthRoleService.fetchEmailWithAccessToken(oauth2User, accessToken);

      if (userEmail == null) {

        return "/";
      }

      String oauth2Id = oauth2User.getName();
      log.info("Extracted - OAuth2 ID: {}, Email: {}", oauth2Id, userEmail);

      Account newAccount = Account.convertToAccount(newAccountString);
      newAccount.setOauth2Id(oauth2Id);
      newAccount.setUserEmail(userEmail);

      if (newAccount.getRegistrationDate() == null) {
        newAccount.setRegistrationDate(LocalDate.now());
      }

      newAccount.setAccountRole("trader");
      accountService.saveAccount(newAccount);

      String paymentRedirectUrl = paymentService.createPaymentLink(newAccount, 100);
      log.info(paymentRedirectUrl);
      return paymentRedirectUrl;

    } catch (Exception e) {
      log.error("Unexpected error: {}", e.getMessage(), e);
      return "/";
    }

  }

  @PostMapping("/create")
  public void createAccount(
      @RequestBody String newAccountString,
      @AuthenticationPrincipal OAuth2User oauth2User,
      HttpServletRequest request,
      HttpServletResponse response) {

    try {
      String accessToken = oAuthRoleService.getCurrentUserAccessToken();

      if (accessToken == null) {
        return;
      }

      String userEmail = oAuthRoleService.fetchEmailWithAccessToken(oauth2User, accessToken);

      if (userEmail == null) {

        return;
      }

      String oauth2Id = oauth2User.getName();
      log.info("Extracted - OAuth2 ID: {}, Email: {}", oauth2Id, userEmail);

      Account newAccount = Account.convertToAccount(newAccountString);
      newAccount.setOauth2Id(oauth2Id);
      newAccount.setUserEmail(userEmail);

      if (newAccount.getRegistrationDate() == null) {
        newAccount.setRegistrationDate(LocalDate.now());
      }
      if (newAccount.getAccountRole() == null || newAccount.getAccountRole().trim().isEmpty()) {
        newAccount.setAccountRole("buyer");
      }

      accountService.saveAccount(newAccount);
      log.info("Account created successfully for: {}", userEmail);

      accountService.logoutUser(request, response);

    } catch (Exception e) {
      log.error("Unexpected error: {}", e.getMessage(), e);
    }
  }

  @GetMapping("/delete/current")
  public ResponseEntity<String> deleteCurrentUser(HttpServletRequest request) {

    try {
      String oauth2Id = request.getUserPrincipal().getName();
      Integer id = accountService.getAccountByOAuth2Id(oauth2Id).getAccountId();
      accountService.deleteAccountById(id);

    } catch (Exception e) {
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error deleting current account");
    }
    return ResponseEntity.ok().body("Succesfuly deleted current account");
  }

  @GetMapping("/delete/{id}")
  public ResponseEntity<String> deleteCurrentUser(@PathVariable Integer id) {

    try {
      accountService.deleteAccountById(id);
      return ResponseEntity.ok().body("Succesfuly deleted account with id " + id.toString());

    } catch (Exception e) {
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error deleting account");
    }
    return ResponseEntity.ok().body("Succesfuly deleted account with id " + id.toString());

  }

  @GetMapping("/logout")
  public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response) {
    try {
      accountService.logoutUser(request, response);
      return ResponseEntity.ok("Successfully logged out");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error logging out: " + e.getMessage());
    }
  }

  @PostMapping("/images/store/{id}")
  public ResponseEntity<Map<String, String>> storeProfileImage(
      @RequestParam("file") MultipartFile file,
      @PathVariable Integer id) {
    try {
      Account account = accountService.getUserbyId(id);
      if (account == null) {
        log.error("Error loading image for account {}: Account doesn't exist", id);
        return ResponseEntity.notFound().build();
      }

      String filename = "profile" + id.toString();
      String filename_ext = imageStorageService.storeImage(file, filename, ImageFolder.ACCOUNT);

      account.setProfileImagePath("/" + ImageFolder.ACCOUNT.getFolderName() + "/" + filename_ext);
      accountService.saveAccount(account);

      Map<String, String> response = new HashMap<>();
      response.put("filename", filename_ext);
      response.put("originalName", file.getOriginalFilename());
      response.put("size", String.valueOf(file.getSize()));
      response.put("contentType", file.getContentType());
      response.put("url", "/api/accounts/images/load/" + id);

      return ResponseEntity.ok(response);
    } catch (IOException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", e.getMessage()));
    }
  }

  @GetMapping("/images/load/{id}")
  public ResponseEntity<Resource> getProfileImage(@PathVariable Integer id) {
    try {
      Account account = accountService.getUserbyId(id);
      if (account == null || account.getProfileImagePath() == null) {
        log.error("Error loading image for account {}: Account doesn't exist or has no image", id);
        return ResponseEntity.notFound().build();
      }

      String filename = account.getProfileImagePath().replace("/" + ImageFolder.ACCOUNT.getFolderName() + "/", "");
      log.info("Loading profile image: {}", filename);

      Resource resource = imageStorageService.loadImage(filename, ImageFolder.ACCOUNT);

      Path filePath = imageStorageService.getUploadDir(ImageFolder.ACCOUNT).resolve(filename);
      String contentType = Files.probeContentType(filePath);

      if (contentType == null) {
        contentType = "image/jpeg";
      }

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=\"" + filename + "\"")
          .body(resource);
    } catch (Exception ex) {
      log.error("Error loading profile image for account {}: {}", id, ex.getMessage());
      return ResponseEntity.internalServerError().build();
    }
  }
}
