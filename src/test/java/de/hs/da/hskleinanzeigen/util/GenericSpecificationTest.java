package de.hs.da.hskleinanzeigen.util;

import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.enumeration.SearchOperation;
import de.hs.da.hskleinanzeigen.repository.AdRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import static de.hs.da.hskleinanzeigen.enumeration.Type.OFFER;
import static de.hs.da.hskleinanzeigen.enumeration.Type.REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GenericSpecificationTest {

    @Autowired
    AdRepository adRepository;

    @Test
    void toPredicate_validSpecificationBehavior_1() {
        GenericSpecification<AD> specification = new GenericSpecification<>();
        specification.add(new SearchCriteria("type", OFFER, SearchOperation.EQUAL));
        specification.add(new SearchCriteria("price", 5, SearchOperation.GREATER_THAN_EQUAL));
        specification.add(new SearchCriteria("price", 200, SearchOperation.LESS_THAN_EQUAL));
        Page<AD> adPage = adRepository.findAll(specification, PageRequest.of(0, 5));
        assertThat(adPage.getContent().size()).isEqualTo(0);
    }

    @Test
    void toPredicate_validSpecificationBehavior_2() {
        GenericSpecification<AD> specification = new GenericSpecification<>();
        specification.add(new SearchCriteria("type", REQUEST, SearchOperation.EQUAL));
        specification.add(new SearchCriteria("price", 200, SearchOperation.LESS_THAN));
        specification.add(new SearchCriteria("price", 20, SearchOperation.GREATER_THAN));
        Page<AD> adPage = adRepository.findAll(specification, PageRequest.of(3, 5));
        assertThat(adPage.getContent().size()).isEqualTo(0);
    }

    @Test
    void toPredicate_invalidMatchSpecificationBehavior() {
        GenericSpecification<AD> specification = new GenericSpecification<>();
        specification.add(new SearchCriteria("type", REQUEST, SearchOperation.EQUAL));
        specification.add(new SearchCriteria("price", 200, SearchOperation.NOT_EQUAL));
        specification.add(new SearchCriteria("price", 555, SearchOperation.MATCH));
        assertThatThrownBy(() -> adRepository.findAll(specification, PageRequest.of(3, 5))).isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

}
