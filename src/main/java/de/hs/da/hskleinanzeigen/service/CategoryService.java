package de.hs.da.hskleinanzeigen.service;

import de.hs.da.hskleinanzeigen.dto.category.CategoryInsertDTO;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public Category insert(Category category) {
    return this.categoryRepository.save(category);
  }

  public List<Category> getAll(){
    return this.categoryRepository.findAll();
  }

  public Category findById(Integer id){
    return this.categoryRepository.findById(id).orElse(null);
  }

  public void drop() {
    this.categoryRepository.deleteAll();
  }

  public boolean isNotComplete(CategoryInsertDTO category){
     if (category.getName() == null)
       return true;
     else if (category.getParentId() != null) // TODO: entferne Abfrage. Integer = null => 0
            return this.categoryRepository.findById(category.getParentId()).isEmpty();
     else return false;
  }

  public boolean categoryExists(Category category) {
    return this.categoryRepository.findByName(category.getName()).isPresent();
  }

}
