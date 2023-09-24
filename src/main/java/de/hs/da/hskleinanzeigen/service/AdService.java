package de.hs.da.hskleinanzeigen.service;


import de.hs.da.hskleinanzeigen.dto.Ad.ADDto;
import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.repository.AdRepository;
import de.hs.da.hskleinanzeigen.util.GenericSpecification;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdService {
  private final AdRepository adRepository;

  @Autowired
  public AdService(AdRepository adRepository){
    this.adRepository = adRepository;
  }

  public AD findById(Integer id){
    Optional<AD> ad = this.adRepository.findById(id);
    return ad.orElse(null);
  }

  public Page<AD> findAllByPredicate(GenericSpecification<AD> specification, Integer pageStart, Integer pageSize){
    return this.adRepository.findAll(specification, PageRequest.of(pageStart, pageSize));
  }

  public List<AD> findAll(){
    return this.adRepository.findAll();
  }

  @Generated
  public boolean isNotComplete(ADDto ad){
    return ad.getType() == null ||
            ad.getCategory() == null ||
            ad.getUser() == null ||
            ad.getDescription() == null ||
            ad.getLocation() == null ||
            ad.getPrice() == null ||
            ad.getTitle() == null;
  }

  public AD save(AD ad){
    return this.adRepository.save(ad);
  }

  public void drop(){
    this.adRepository.deleteAll();
  }

}
