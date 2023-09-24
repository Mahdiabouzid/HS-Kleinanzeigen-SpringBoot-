package de.hs.da.hskleinanzeigen.controller;

import de.hs.da.hskleinanzeigen.dto.Ad.ADDto;
import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.enumeration.SearchOperation;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import de.hs.da.hskleinanzeigen.repository.UserRepository;
import de.hs.da.hskleinanzeigen.service.AdService;
import de.hs.da.hskleinanzeigen.service.CategoryService;
import de.hs.da.hskleinanzeigen.util.GenericSpecification;
import de.hs.da.hskleinanzeigen.util.SearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Tag(name = "Ad", description = "Read and set ads and their properties.")
public class AdController {
  private final AdService adService;
  private final CategoryService categoryService;

  private final UserRepository userRepository;


  @Autowired
  public AdController(AdService adService, CategoryService categoryService , UserRepository userRepository) {
    this.adService = adService;
    this.categoryService = categoryService;
    this.userRepository = userRepository;
  }

  //responseentity
  @GetMapping("/api/advertisements/{id}")
  @Operation(summary = "Returns a specific ad by its identifier.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "OK"),
          @ApiResponse(responseCode = "404", description = "No ad found")
  })
  public ResponseEntity<AD> findADById(@Parameter(name = "Id of the ad to be obtained", required = true) @PathVariable("id") Integer id) {
    AD ad = this.adService.findById(id);
    if (ad == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(ad);
  }

  @GetMapping("/api/advertisements")
  @Operation(summary = "Returns ads meeting the request requirements")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "OK"),
          @ApiResponse(responseCode = "204", description = "No ads found"),
          @ApiResponse(responseCode = "400", description = "Request parameters pageStart or pageSize not set or invalid")
  })
  public ResponseEntity<Page<AD>> findAdByInformation(
          @Parameter(name = "Type of the ads to be obtained") @RequestParam(required = false) Type type, @Parameter(name = "Category of the ads to be obtained") @RequestParam(required = false)  Integer category, @Parameter(name = "Start of the price range of the ads to be obtained") @RequestParam(required = false)  Integer priceFrom, @Parameter(name = "The end of the price range of the ads to be obtained") @RequestParam(required = false)  Integer priceTo,
          @Parameter(name = "Start of the page of the ads to be obtained", required = true) @RequestParam Integer pageStart,@Parameter(name = "Number of the pages of ads to be obtained", required = true) @RequestParam Integer pageSize) {

    if (pageSize == null || pageStart == null  || pageSize < 0 || pageStart < 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    GenericSpecification<AD> specification = new GenericSpecification<>();
    ResponseEntity<Page<AD>> resEntity = checkSpecification(specification,category, priceFrom, priceTo, type);

    if (resEntity != null) return resEntity;

    Page<AD> ads = this.adService.findAllByPredicate(specification, pageStart, pageSize  );
    if (ads.isEmpty())
      return ResponseEntity.status(HttpStatus.valueOf(204)).body(null);
    return ResponseEntity.status(HttpStatus.OK).body(ads);
  }

  private ResponseEntity<Page<AD>> checkSpecification(GenericSpecification<AD> specification, Integer category, Integer priceFrom, Integer priceTo, Type type) {
    if (type != null ){
      specification.add(new SearchCriteria("type", type, SearchOperation.EQUAL));
    }
    ResponseEntity<Page<AD>> body = getPageResponseEntity(specification, category);
    if (body != null) return body;
    if (priceFrom != null && priceFrom > 0){
      specification.add(new SearchCriteria("price", priceFrom, SearchOperation.GREATER_THAN_EQUAL));
    }
    if (priceTo != null && priceTo > 0){
      specification.add(new SearchCriteria("price", priceTo, SearchOperation.LESS_THAN_EQUAL));
    }
    return null;
  }

  private ResponseEntity<Page<AD>> getPageResponseEntity(GenericSpecification<AD> specification, Integer category) {
    if (category != null && category >= 0){
      Category byId = categoryService.findById(category);
      if (byId == null){
        return ResponseEntity.status(HttpStatus.valueOf(204)).body(null);
      }
      specification.add(new SearchCriteria("category", category, SearchOperation.EQUAL));
    }
    return null;
  }


  @PostMapping("/api/advertisements")
  @Operation(summary = "Saves and returns a new ad")
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Successfully persisted"),
          @ApiResponse(responseCode = "400", description = "Body incomplete"),
  })
  public ResponseEntity<AD> insert(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Attributes of the ad to be inserted", required = true) @RequestBody ADDto dto){
    if (this.adService.isNotComplete(dto))
      return ResponseEntity.status(400).body(null);

    Category category = categoryService.findById(dto.getCategory().getId());
    // FIXME Using repository, because of cache. Caching führt zu detached objects.
    User user = userRepository.findById(dto.getUser().getId()).orElse(null);
    if(category == null || user == null)
      return ResponseEntity.status(400).body(null);

    AD ad = new AD();
    ad.setCategory(category);
    ad.setUser(user);
    ad.setType(dto.getType());
    ad.setTitle(dto.getTitle());
    ad.setDescription(dto.getDescription());
    ad.setPrice(dto.getPrice());
    ad.setLocation(dto.getLocation());
    ad.setCreated(LocalDateTime.now());

    AD saved = this.adService.save(ad);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

}
