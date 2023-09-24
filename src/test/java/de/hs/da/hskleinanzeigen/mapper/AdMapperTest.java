package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.category.CategoryAdDTO;
import de.hs.da.hskleinanzeigen.dto.notepad.AdNotepadResponseDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserRequestDTO;
import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AdMapperTest {

    AdMapper adMapper = new AdMapperImpl();

    @Test
    void toNotepadResponseDTO_ReturnsCorrectDTO(){
        CategoryAdDTO toInsertCategory = new CategoryAdDTO(0, "category");
        UserRequestDTO toInsertUser = new UserRequestDTO();
        toInsertUser.setId(0);

        Category category = new Category();
        category.setId(toInsertCategory.getId());
        category.setName(toInsertCategory.getName());

        User user = new User();
        user.setId(toInsertUser.getId());

        AdNotepadResponseDTO dto = new AdNotepadResponseDTO();
        dto.setId(0);
        dto.setType(Type.OFFER);
        dto.setCategory(toInsertCategory);
        dto.setDescription("description");
        dto.setPrice(0);
        dto.setLocation("location");
        dto.setUser(toInsertUser);

        AD ad = new AD();
        ad.setId(dto.getId());
        ad.setType(dto.getType());
        ad.setCategory(category);
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setLocation(dto.getLocation());
        ad.setUser(user);

        assertThat(adMapper.toNotepadResponseDTO(ad)).isEqualTo(dto);
    }

    @Test
    void toNotepadResponseDTO_ReturnsNullOnNullEntity(){
        assertThat(adMapper.toNotepadResponseDTO(null)).isNull();
    }
}
