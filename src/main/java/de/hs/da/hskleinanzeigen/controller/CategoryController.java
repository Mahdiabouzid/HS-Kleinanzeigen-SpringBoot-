package de.hs.da.hskleinanzeigen.controller;

import de.hs.da.hskleinanzeigen.dto.category.CategoryInsertDTO;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Category", description = "Read, set and delete categories and their properties.")
public class CategoryController {
  private final CategoryService categoryService;
  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /*@PostMapping(path = "/api/categories",
          consumes = MediaType.APPLICATION_JSON_VALUE
  )*/
  @PostMapping(path = "/api/categories")
  @Operation(summary = "Saves a new category.")
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "OK"),
          @ApiResponse(responseCode = "400", description = "Request body incomplete or parent category is missing"),
          @ApiResponse(responseCode = "409", description = "Category already exists")
  })
  public ResponseEntity<Category> insert(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true) @RequestBody CategoryInsertDTO dto) {
    if (dto == null)
      return null;
    Category category = new Category();
    category.setName(dto.getName());
    Category parent = dto.getParentId() == null ? null : categoryService.findById(dto.getParentId());
    category.setParent(parent);
    if (this.categoryService.isNotComplete(dto)){
      return ResponseEntity.status(400).body(null);
    }

    if (this.categoryService.categoryExists(category)){
      return ResponseEntity.status(409).body(null);
    }

    Category saved = this.categoryService.insert(category);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);

  }

  @GetMapping("/api/category/all")
  @Operation(summary = "Returns all categories")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "OK"),
  })
  public List<Category> getAll(){
    List<Category> tmp = this.categoryService.getAll();
    return tmp;
  }

  @GetMapping("/api/category/drop")
  @Operation(summary = "Deletes all categories (drop)")
  @ApiResponse(responseCode = "200", description = "OK")
  public ResponseEntity<String> drop(){
    this.categoryService.drop();
    return ResponseEntity.status(HttpStatus.OK).body("all gone");
  }

}
