package de.hs.da.hskleinanzeigen.service;

import de.hs.da.hskleinanzeigen.dto.Ad.ADDto;
import de.hs.da.hskleinanzeigen.dto.category.CategoryRequestDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserAdRequestDTO;
import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import de.hs.da.hskleinanzeigen.repository.AdRepository;
import de.hs.da.hskleinanzeigen.util.GenericSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AdServiceTest {
    private AdService adService;

    @Mock
    private AdRepository adRepository;

    @BeforeEach
    public void setUp() {
        adService = new AdService(adRepository);
    }

    @Test
    void findById_findValidAd() {
        int id = 1;
        Mockito.when(adRepository.findById(1))
                .thenReturn(Optional.of(arrangeAd()));
        AD ad = adService.findById(id);
        assertThat(ad.getId()).isEqualTo(1);
    }

    @Test
    void findAllByPredicate_findValid() {
        AD ad = arrangeAd();
        ArrayList<AD> ads = new ArrayList<>();
        ads.add(ad);
        PageImpl<AD> page = new PageImpl<>(ads);
        Mockito.when(adRepository.findAll(any(GenericSpecification.class), any(PageRequest.class))).thenReturn(page);

        Page<AD> pageRes = adService.findAllByPredicate(new GenericSpecification<>(), 0, 5);

        assertThat(pageRes.getTotalPages()).isEqualTo(1);
        assertThat(pageRes.getContent().get(0).getId()).isEqualTo(1);
    }

    @Test
    void findAll_findValid() {
        AD ad1 = arrangeAd();
        AD ad2 = arrangeAd();
        ArrayList<AD> ads = new ArrayList<>();
        ads.add(ad1);
        ads.add(ad2);
        Mockito.when(adRepository.findAll()).thenReturn(ads);

        List<AD> adsRes = adService.findAll();

        assertThat(adsRes.size()).isEqualTo(ads.size());
        assertThat(adsRes.get(0)).isEqualTo(ads.get(0));
        assertThat(adsRes.get(1)).isEqualTo(ads.get(1));
    }
/*
    @Test
    void findById_adAlreadyExists() {

    }*/

    @Test
    void save_saveValidAd() {
        AD ad = arrangeAd();
        Mockito.when(adRepository.save(any(AD.class)))
                .thenReturn(ad);
        AD nAd = adService.save(ad);
        assertThat(nAd.getId()).isEqualTo(ad.getId());
    }

    @Test
    void isNotComplete_checkCompleteness() {
        ADDto incomplete = new ADDto();
        incomplete.setType(Type.OFFER);
        assertThat(adService.isNotComplete(incomplete)).isTrue();
        incomplete.setCategory(new CategoryRequestDTO());
        assertThat(adService.isNotComplete(incomplete)).isTrue();
        incomplete.setUser(new UserAdRequestDTO());
        assertThat(adService.isNotComplete(incomplete)).isTrue();
        incomplete.setDescription("description");
        assertThat(adService.isNotComplete(incomplete)).isTrue();
        incomplete.setLocation("Darmstadt");
        assertThat(adService.isNotComplete(incomplete)).isTrue();
        incomplete.setPrice(1);
        assertThat(adService.isNotComplete(incomplete)).isTrue();
        incomplete.setTitle("incomplete");
        assertThat(adService.isNotComplete(incomplete)).isFalse();
    }

    private AD arrangeAd() {
        AD ad = new AD();
        ad.setPrice(1);
        ad.setTitle("cool ad");
        ad.setId(1);
        ad.setDescription("cool description");
        ad.setUser(new User());
        ad.setCategory(new Category());
        ad.setType(Type.OFFER);
        ad.setLocation("Darmstadt");
        return ad;
    }
}
