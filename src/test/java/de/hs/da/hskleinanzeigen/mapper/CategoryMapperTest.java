package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.category.CategoryAdDTO;
import de.hs.da.hskleinanzeigen.dto.category.CategoryInsertDTO;
import de.hs.da.hskleinanzeigen.dto.category.CategoryRequestDTO;
import de.hs.da.hskleinanzeigen.entity.Category;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CategoryMapperTest {

    CategoryMapper categoryMapper = new CategoryMapperImpl();

    @Test
    void toCategory_ReturnsNullOnNullParam(){
        assertThat(categoryMapper.toCategory(null)).isNull();
    }

    @Test
    void toCategory_ReturnsCorrectCategory(){
        CategoryRequestDTO dto = new CategoryRequestDTO();
        dto.setId(0);
        Category category = new Category();
        category.setId(dto.getId());

        assertThat(categoryMapper.toCategory(dto).getId()).isEqualTo(category.getId());
    }

    @Test
    void toDTORequest_ReturnsNullOnNullParam(){
        assertThat(categoryMapper.toDTORequest(null)).isNull();
    }

    @Test
    void toDTORequest_ReturnsCorrectDTO(){
        Category category = new Category();
        category.setId(0);
        CategoryRequestDTO dto = new CategoryRequestDTO();
        dto.setId(category.getId());

        assertThat(categoryMapper.toDTORequest(category).getId()).isEqualTo(dto.getId());
    }

    @Test
    void toDTOInsert_ReturnsNullOnNullParam(){
        assertThat(categoryMapper.toDTOInsert(null)).isNull();
    }

    @Test
    void toDTOInsert_ReturnsCorrectDTO(){
        Category withoutParent = new Category();
        withoutParent.setId(0);
        withoutParent.setName("withoutParent");
        CategoryInsertDTO dto = new CategoryInsertDTO();
        dto.setName(withoutParent.getName());

        assertThat(categoryMapper.toDTOInsert(withoutParent).getName()).isEqualTo(dto.getName());

        Category withParent = new Category();
        Category parent = new Category();
        parent.setId(0);
        withParent.setParent(parent);
        withParent.setName("withParent");
        dto.setName(withParent.getName());
        dto.setParentId(parent.getId());

        assertThat(categoryMapper.toDTOInsert(withParent).getParentId()).isEqualTo(dto.getParentId());
        assertThat(categoryMapper.toDTOInsert(withParent).getName()).isEqualTo(dto.getName());
    }

    @Test
    void toCategoryAdDTO_ReturnsNullOnNullParam(){
        assertThat(categoryMapper.toCategoryAdDTO(null)).isNull();
    }

    @Test
    void toCategoryAdDTO_ReturnsCorrectDTO(){
        Category param = new Category();
        param.setId(0);
        param.setName("param");

        CategoryAdDTO dto = new CategoryAdDTO(param.getId(), param.getName());

        assertThat(categoryMapper.toCategoryAdDTO(param)).isEqualTo(dto);
    }
}
