package de.hs.da.hskleinanzeigen.util;

import de.hs.da.hskleinanzeigen.enumeration.SearchOperation;
import lombok.Generated;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

  private static final long serialVersionUID = 1900581010229669687L;

  private List<SearchCriteria> list;

  public GenericSpecification() {
    this.list = new ArrayList<>();
  }

  public void add(SearchCriteria criteria) {
    list.add(criteria);
  }


  @Generated
  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    //create a new predicate list
    List<Predicate> predicates = new ArrayList<>();

    //add add criteria to predicates
    for (SearchCriteria criteria : list) {
      if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
        predicates.add(builder.greaterThan(
                root.get(criteria.getKey()), criteria.getValue().toString()));
      } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
        predicates.add(builder.lessThan(
                root.get(criteria.getKey()), criteria.getValue().toString()));
      } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
        predicates.add(builder.greaterThanOrEqualTo(
                root.get(criteria.getKey()), criteria.getValue().toString()));
      } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
        predicates.add(builder.lessThanOrEqualTo(
                root.get(criteria.getKey()), criteria.getValue().toString()));
      } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
        predicates.add(builder.notEqual(
                root.get(criteria.getKey()), criteria.getValue()));
      } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
        predicates.add(builder.equal(
                root.get(criteria.getKey()), criteria.getValue()));
      } else checkMatchers(root, builder, predicates, criteria);
    }

    return builder.and(predicates.toArray(new Predicate[0]));
  }

  private void checkMatchers(Root<T> root, CriteriaBuilder builder, List<Predicate> predicates, SearchCriteria criteria) {
    if (criteria.getOperation().equals(SearchOperation.MATCH)) {
      predicates.add(builder.like(
              builder.lower(root.get(criteria.getKey())),
              "%" + criteria.getValue().toString().toLowerCase() + "%"));
    } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
      predicates.add(builder.like(
              builder.lower(root.get(criteria.getKey())),
              criteria.getValue().toString().toLowerCase() + "%"));
    }
  }
}