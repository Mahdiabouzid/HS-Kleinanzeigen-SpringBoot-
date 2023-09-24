package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.notepad.AdNotepadResponseDTO;
import de.hs.da.hskleinanzeigen.dto.notepad.UserNotepadResponseDTO;
import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.Notepad;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class NotepadMapperTest {

    NotepadMapper notepadMapper = new NotepadMapperImpl();

    @Test
    void toDTO_ReturnsNullOnNullParam(){
        assertThat(notepadMapper.toDTO(null)).isNull();
    }

    @Test
    void toDTO_ReturnsCorrectDTO(){
        User existingUser = new User();
        existingUser.setId(0);

        Category existingCategory = new Category();
        existingCategory.setId(0);
        existingCategory.setName("existingCategory");

        AD existingAd = new AD();
        existingAd.setId(0);
        existingAd.setType(Type.OFFER);
        existingAd.setCategory(existingCategory);
        existingAd.setDescription("description");
        existingAd.setPrice(0);
        existingAd.setLocation("location");
        existingAd.setUser(existingUser);

        Notepad notepad = new Notepad();
        notepad.setId(0);
        notepad.setAd(existingAd);
        notepad.setNote("note");
        notepad.setUser(existingUser);

        AdMapper adMapper = new AdMapperImpl();
        AdNotepadResponseDTO x = adMapper.toNotepadResponseDTO(existingAd);

        UserNotepadResponseDTO dto = new UserNotepadResponseDTO();
        dto.setId(notepad.getId());
        dto.setAdvertisement(x);
        dto.setNote(notepad.getNote());

        assertThat(notepadMapper.toDTO(notepad)).isEqualTo(dto);
    }
}
