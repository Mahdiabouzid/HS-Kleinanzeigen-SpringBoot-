package de.hs.da.hskleinanzeigen.service;

import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.entity.Notepad;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repository.NotepadRepository;
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
public class NotepadServiceTest {
    @Mock
    NotepadRepository notepadRepository;

    NotepadService notepadService;

    @BeforeEach
    void setUp(){
        notepadService = new NotepadService(notepadRepository);
    }

    @Test
    void save_ReturnsInsertedNotepad(){
        Notepad toInsert = Mockito.mock(Notepad.class);
        toInsert.setId(0);
        Mockito.when(notepadRepository.save(toInsert)).thenReturn(toInsert);

        assertThat(notepadService.save(toInsert).getId()).isEqualTo(toInsert.getId());
    }

    @Test
    void findAllByUserId_ReturnsListOfNotepadsWithSpecificUserId(){
        int listSize = 20;
        List<Notepad> notepads = new ArrayList<>();
        User existingUser = Mockito.mock(User.class);
        existingUser.setId(0);

        for(int i = 0; i < listSize; i++){
            Notepad toInsert = Mockito.mock(Notepad.class);
            toInsert.setUser(existingUser);
            notepads.add(toInsert);
        }

        Mockito.when(notepadRepository.findAllByUserId(existingUser.getId())).thenReturn(notepads);
        assertThat(notepadService.findAllByUserId(existingUser.getId()).size()).isEqualTo(listSize);
    }

    @Test
    void findByUserId_ReturnsNotepadWithSpecificUserId(){
        User existingUser = Mockito.mock(User.class);
        existingUser.setId(0);
        Notepad toGet = Mockito.mock(Notepad.class);
        toGet.setUser(existingUser);

        Mockito.when(notepadRepository.findByUserId(existingUser.getId())).thenReturn(Optional.of(toGet));
        assertThat(notepadService.findByUserId(existingUser.getId()).getId()).isEqualTo(toGet.getId());
    }

    @Test
    void findByUserId_ReturnsNullOnNotExistingUserId(){
        Mockito.when(notepadRepository.findByUserId(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThat(notepadService.findByUserId(0)).isNull();
    }

    @Test
    void findByUserIdAndAdId_ReturnsNotepadOnUserIdAndAdId(){
        User existingUser = Mockito.mock(User.class);
        AD existingAd = Mockito.mock(AD.class);
        Notepad toFind = Mockito.mock(Notepad.class);

        existingUser.setId(0);
        existingAd.setId(0);
        toFind.setId(0);
        toFind.setUser(existingUser);
        toFind.setAd(existingAd);

        Mockito.when(notepadRepository.findByUserIdAndAdId(existingUser.getId(), existingAd.getId())).thenReturn(Optional.of(toFind));
        assertThat(notepadService.findByUserIdAndAdId(existingUser.getId(), existingAd.getId()).getId()).isEqualTo(toFind.getId());
    }

    @Test
    void findByUserIdAndAdId_ReturnsNullOnNotMatchingParameters(){
        User existingUser = Mockito.mock(User.class);
        AD existingAd = Mockito.mock(AD.class);
        Notepad toFind = Mockito.mock(Notepad.class);

        existingUser.setId(0);
        existingAd.setId(0);

        Mockito.when(notepadRepository.findByUserIdAndAdId(Mockito.eq(existingUser.getId()), Mockito.anyInt()))
                .thenReturn(Optional.empty());
        assertThat(notepadService.findByUserIdAndAdId(Mockito.eq(existingUser.getId()), Mockito.anyInt())).isNull();
        Mockito.when(notepadRepository.findByUserIdAndAdId(30, existingAd.getId()))
                .thenReturn(Optional.empty());
        assertThat(notepadService.findByUserIdAndAdId(30, existingAd.getId())).isNull();
        Mockito.when(notepadRepository.findByUserIdAndAdId(0, 0))
                .thenReturn(Optional.empty());
        assertThat(notepadService.findByUserIdAndAdId(0, 0)).isNull();

        // TODO: Freitag fragen wieso nicht mehrmal Mock.anyInt und Mock.eq
    }
}
