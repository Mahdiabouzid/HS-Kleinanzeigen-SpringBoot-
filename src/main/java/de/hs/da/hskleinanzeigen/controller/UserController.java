package de.hs.da.hskleinanzeigen.controller;

import de.hs.da.hskleinanzeigen.dto.notepad.AdNotepadUpdateRequestDTO;
import de.hs.da.hskleinanzeigen.dto.notepad.AdNotepadUpdateResponseDTO;
import de.hs.da.hskleinanzeigen.dto.notepad.UserNotepadResponseDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserRegisterDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserRequestDTO;
import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.entity.Notepad;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.mapper.NotepadMapper;
import de.hs.da.hskleinanzeigen.mapper.UserMapper;
import de.hs.da.hskleinanzeigen.service.AdService;
import de.hs.da.hskleinanzeigen.service.NotepadService;
import de.hs.da.hskleinanzeigen.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@Tag(name = "User", description = "Read, set and delete users and their properties.")
public class UserController {
  private final UserService userService;
  private final NotepadService notepadService;
  private final AdService adService;

  @Autowired
  public UserController(UserService userService, NotepadService notepadService, AdService adService) {
    this.userService = userService;
    this.notepadService = notepadService;
    this.adService = adService;
  }

  @PutMapping("/api/users/{userId}/notepad")
  public ResponseEntity<AdNotepadUpdateResponseDTO> addAdToNotepad(@PathVariable("userId") Integer userId,
                                                                   @RequestBody AdNotepadUpdateRequestDTO request) {
    User user = this.userService.findById(userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    if (request.getNote() == null || request.getAdvertisementId() == null) {
      return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    AD ad = this.adService.findById(request.getAdvertisementId());
    if (ad == null) {
      return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    Notepad notepad = this.notepadService.findByUserId(userId);
    if (notepad == null) {
      notepad = new Notepad();
    }
    notepad.setAd(ad);
    notepad.setUser(user);
    notepad.setNote(request.getNote());
    notepad.setCreated(LocalDateTime.now());
    Notepad saved = this.notepadService.save(notepad);
    return ResponseEntity.ok(new AdNotepadUpdateResponseDTO(saved.getId(), ad.getId(), saved.getNote()));
  }

  @GetMapping("/api/users/{userId}/notepad")
  public ResponseEntity<List<UserNotepadResponseDTO>> getNotepad(@PathVariable("userId") Integer userId) {
    if (this.userService.findById(userId) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    List<Notepad> notepads = this.notepadService.findAllByUserId(userId);
    if (notepads == null || notepads.isEmpty()) {
      return ResponseEntity.status(HttpStatus.valueOf(204)).build();
    }

    List<UserNotepadResponseDTO> resultList = new ArrayList<>();
    for (Notepad notepad : notepads) {
      resultList.add(Mappers.getMapper(NotepadMapper.class).toDTO(notepad));
    }
    return ResponseEntity.ok(resultList);
  }

  @DeleteMapping("/api/users/{userId}/notepad/{adId}")
  public ResponseEntity<Void> deleteAdFromNotepad(@PathVariable Integer userId, @PathVariable Integer adId) {
    Notepad notepad = this.notepadService.findByUserIdAndAdId(userId, adId);
    this.notepadService.deleteById(notepad.getId());
    return ResponseEntity.status(HttpStatus.valueOf(204)).build();
  }


  @PostMapping("/api/users")
  @Operation(summary = "Registers a new user")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "OK"),
    @ApiResponse (responseCode = "400", description = "Request body incomplete"),
    @ApiResponse(responseCode = "409", description = "User with the given email address is already registered")
  })
  public ResponseEntity<UserRequestDTO> registerUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Attributes of the user to be created", required = true) @RequestBody UserRegisterDTO dto) {

    User user = Mappers.getMapper(UserMapper.class).toUser(dto);
    user.setCreated(LocalDateTime.now());

    if (this.userService.isNotComplete(user)) {
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(null);
    }

    if (!this.userService.validateUser(user)) {
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(null);
    }

    if (this.userService.isEmailTaken(user.getEmail())) {
      return ResponseEntity.status(HttpStatus.valueOf(409)).body(null);
    }

    User saved = userService.insertUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.getMapper(UserMapper.class).toUserResponse(saved));
  }

  @GetMapping("/api/users/{id}")
  @Operation(summary = "Returns a specific user by its identifier")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "OK"),
          @ApiResponse(responseCode = "404", description = "No user found")
  })
  public ResponseEntity<User> findUserById(@Parameter(name = "Id of the user to be obtained") @PathVariable("id") Integer id){
    User user = this.userService.findById(id);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @GetMapping("/api/users")
  @Operation(summary = "Returns users meeting the request requirements")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "OK"),
          @ApiResponse(responseCode = "204", description = "No ads found"),
          @ApiResponse(responseCode = "400", description = "Request parameters pageStart or pageSize not set or invalid")
  })
  public ResponseEntity<List<User>> findAllUsers(@Parameter(name = "Start of the page of the users to be obtained", required = true) @RequestParam Integer pageStart, @Parameter(name = "Number of the pages of ads to be obtained", required = true) @RequestParam Integer pageSize){
    if (pageStart == null || pageSize == null || pageStart < 0 || pageSize < 0){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    List<User> users = this.userService.findAllSortedAscending(pageStart, pageSize);
    if (users == null || users.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }
}
