package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.notepad.UserNotepadResponseDTO;
import de.hs.da.hskleinanzeigen.entity.Notepad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {AdMapper.class})
public interface NotepadMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "ad", target = "advertisement")
    @Mapping(source = "note", target = "note")
    UserNotepadResponseDTO toDTO(Notepad notepad);




}
