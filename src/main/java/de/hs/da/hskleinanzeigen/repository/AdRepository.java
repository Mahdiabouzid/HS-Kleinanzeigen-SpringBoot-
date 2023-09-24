package de.hs.da.hskleinanzeigen.repository;

import de.hs.da.hskleinanzeigen.entity.AD;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<AD, Integer>, JpaSpecificationExecutor<AD> {
  Optional<AD> findById(Integer id);
  List<AD> findAll();
  @Override
  List<AD> findAll(Specification<AD> specification);


}
