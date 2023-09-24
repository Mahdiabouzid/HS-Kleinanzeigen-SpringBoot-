package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.category.CategoryAdDTO;
import de.hs.da.hskleinanzeigen.dto.category.CategoryInsertDTO;
import de.hs.da.hskleinanzeigen.dto.category.CategoryRequestDTO;
import de.hs.da.hskleinanzeigen.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper()
public interface CategoryMapper {

    @Mapping(source = "id", target = "id")
    Category toCategory(CategoryRequestDTO dto);

    @Mapping(source = "id", target = "id")
    CategoryRequestDTO toDTORequest(Category entity);

    /*
    @Mapping(target = "parent", expression = "java(categoryService.findById(dto.getParentId()))")
    @Mapping(source = "name", target = "name")
    public abstract Category toCategory(CategoryInsertDTO dto);*/

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "name", target = "name")
    CategoryInsertDTO toDTOInsert(Category entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    CategoryAdDTO toCategoryAdDTO(Category entity);
}
