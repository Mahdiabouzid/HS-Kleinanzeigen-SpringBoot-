package de.hs.da.hskleinanzeigen.service;

import de.hs.da.hskleinanzeigen.dto.category.CategoryInsertDTO;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp(){
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void insert_ReturnsParameter(){
        Category toInsert = Mockito.mock(Category.class);
        toInsert.setName("insert");
        Mockito.when(categoryRepository.save(toInsert)).thenReturn(toInsert);
        Category returnValue = categoryService.insert(toInsert);

        assertThat(returnValue.getId()).isEqualTo(toInsert.getId());
        assertThat(returnValue.getName()).isEqualTo(toInsert.getName());
    }

    @Test
    void getAll_ReturnsAllCategories(){
        List<Category> categories = new ArrayList<>();
        int listSize = 20;
        for(int i = 0; i < listSize; i++){
            categories.add(Mockito.mock(Category.class));
        }
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
        assertThat(categoryService.getAll().size()).isEqualTo(listSize);
    }

    @Test
    void findById_ReturnsCorrectCategory(){
        Category toFind = Mockito.mock(Category.class);
        toFind.setId(5);
        Mockito.when(categoryRepository.findById(toFind.getId())).thenReturn(Optional.of(toFind));
        assertThat(categoryService.findById(toFind.getId()).getId()).isEqualTo(toFind.getId());
    }

    @Test
    void isNotComplete_ReturnsTrueOnNullName(){
        CategoryInsertDTO noName = Mockito.mock(CategoryInsertDTO.class);
        assertThat(categoryService.isNotComplete(noName)).isEqualTo(true);
    }

    @Test
    void isNotComplete_ReturnFalseIfParentIdIsNull(){
        Category parent = new Category();
        parent.setId(0);

        CategoryInsertDTO categoryDTO = new CategoryInsertDTO();
        categoryDTO.setName("exists");
        categoryDTO.setParentId(parent.getId());

        Optional<Category> opParent = Optional.of(parent);

        Mockito.when(categoryRepository.findById(parent.getId())).thenReturn(opParent);
        assertThat(categoryService.isNotComplete(categoryDTO)).isFalse();
    }

    @Test
    void categoryExists_ReturnsTrueIfCategoryExists(){
        Category toFind = Mockito.mock(Category.class);
        toFind.setId(0);
        toFind.setName("name");
        categoryRepository.save(toFind);
        Mockito.when(categoryRepository.findByName(toFind.getName())).thenReturn(Optional.of(toFind));
        assertThat(categoryService.categoryExists(toFind)).isTrue();
    }

    @Test
    void categoryExists_ReturnsFalseIfCategoryDoesNotExist(){
        Optional<Category> op = Optional.empty();
        Category notExisting = new Category();
        notExisting.setName("not");
        Mockito.when(categoryRepository.findByName(Mockito.anyString())).thenReturn(op);
        assertThat(categoryService.categoryExists(notExisting)).isFalse();
    }
}
