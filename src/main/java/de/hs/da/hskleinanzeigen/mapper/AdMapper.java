package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.notepad.AdNotepadResponseDTO;
import de.hs.da.hskleinanzeigen.entity.AD;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class, CategoryMapper.class})
public interface AdMapper {
    // Source = parameter, target = Return-value
    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "user", target = "user")
    AdNotepadResponseDTO toNotepadResponseDTO(AD entity);
}
