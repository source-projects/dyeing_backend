package com.main.glory.filters;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.*;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.services.DataConversion;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecificationManager<T> {
  private Specification<T> createSpecification(Filter input,HashMap<String,List<String>> subModelCase) {
    return new Specification<T>() {
      @Override
      public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<String> attributes=  input.getField();
        if(subModelCase!=null)
        if(subModelCase.containsKey(input.getField().get(0))){
          attributes= subModelCase.get(input.getField().get(0));
        }

//        if(subModelCase.containsKey(input.getField().get(0).equalsIgnoreCase("batchList")))
//        {
//          List<Predicate> predicates = new ArrayList<>();
//
//          Filter searchFilter = input;
//          Join<Object, Object> joinParent = root.join("batchData");
//
//          //String property = searchFilter.getField().get(0);
//          Path expression = joinParent.get("batchId");
//          predicates.add(criteriaBuilder.like(expression, "%" + searchFilter.getValue() + "%"));
//          return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//
//        }

        var path=root.get(attributes.get(0));
        for(int i=1;i<attributes.size();i++){
          path=path.get(attributes.get(i));
        }
        

        switch (input.getOperator()) {

          case EQUALS:

            return criteriaBuilder.equal(path,
                castToRequiredType(path.getJavaType(), input.getValue()));

          case NOT_EQUALS:
            return criteriaBuilder.notEqual(path,
                castToRequiredType(path.getJavaType(), input.getValue()));

          case LESS_THAN:

            if (path.getJavaType().isAssignableFrom(Date.class))
              return criteriaBuilder.lessThan((Expression)path,
                  (Date) castToRequiredType(path.getJavaType(), input.getValue()));
            else
              return criteriaBuilder.lt((Expression)path,
                  (Number) castToRequiredType(path.getJavaType(), input.getValue()));

          case GREATER_THAN:
            if (path.getJavaType().isAssignableFrom(Date.class))
              return criteriaBuilder.greaterThan((Expression)path,
                  (Date) castToRequiredType(path.getJavaType(), input.getValue()));
            else
              return criteriaBuilder.gt((Expression)path,
                  (Number) castToRequiredType(path.getJavaType(), input.getValue()));

          case LIKE:
            return criteriaBuilder.like((Expression)path, "%" + input.getValue() + "%");
          
            case START_WITH:
            return criteriaBuilder.like((Expression)path, input.getValue() + "%");
            case END_WITH:
            return criteriaBuilder.like((Expression)path, "%" + input.getValue());

          case IN:
            return criteriaBuilder.in(path)
                .value(castToRequiredType(path.getJavaType(), input.getValues()));


          case IN_RANGE:
            return criteriaBuilder.between((Expression)path,(Date)castToRequiredType(path.getJavaType(), input.getValues().get(0)),(Date)castToRequiredType(path.getJavaType(), input.getValues().get(1)));

          default:
            throw new RuntimeException("Operation not supported yet");
        }

      }
    };

  }

  private Object castToRequiredType(Class fieldType, String value) {
    System.out.println(fieldType.getName() + " " + value + " ");
    if (fieldType.isAssignableFrom(Double.class)) {
      return Double.valueOf(value);
    } else if (fieldType.isAssignableFrom(Integer.class)) {
      return Integer.valueOf(value);
    } else if (Enum.class.isAssignableFrom(fieldType)) {
      return Enum.valueOf(fieldType, value);
    } else if (fieldType.isAssignableFrom(Long.class)) {
      return Long.valueOf(value);
    } else if (fieldType.isAssignableFrom(String.class)) {
      return value;
    } else if (fieldType.isAssignableFrom(Boolean.class)) {
      return Boolean.valueOf(value);
    }

    else if (fieldType.isAssignableFrom(Date.class)) {
      Date date = Date.from(Instant.now());
      try {
        date = DataConversion.stringToDate(value);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      System.out.println(date.getTime());
      return date;
      // return date;
    }

    return null;
  }

  private Object castToRequiredType(Class fieldType, List<String> value) {
    List<Object> lists = new ArrayList<>();
    for (String s : value) {
      lists.add(castToRequiredType(fieldType, s));
    }
    return lists;
  }

  public Specification<T> getSpecificationFromFilters(List<Filter> filter, boolean isAnd,HashMap<String,List<String>> subModelCase) {
    if (filter == null || filter.isEmpty())
      return null;
    System.out.println("Creating specification");
    Specification<T> specification = createSpecification(filter.remove(0),subModelCase);


    for (Filter input : filter) {

        if (isAnd)
          specification = specification.and(createSpecification(input, subModelCase));

        else
          specification = specification.or(createSpecification(input, subModelCase));



    }
    return specification;
  }

  public static Boolean getOperationResult(String val1,String val2,Class fieldType,QueryOperator operator){
    if (val1==null){
      if(val2==null)
        return true;
      
      else
      return false;
    }
    if (fieldType==String.class){
      String s1=(String) val1;
      String s2=(String) val2;
      switch (operator){
        case EQUALS:
        return s1.equals(s2);
        case NOT_EQUALS:
        return !s1.equals(s2);
        case LIKE:
        return s2.contains(s1);
        case START_WITH:
        return s2.substring(0, s1.length()).equals(s1);
        case END_WITH:
        return s2.substring(s2.length()- s1.length(),s2.length()).equals(s1);
      }
      return false;
    }
    else if (fieldType==Long.class){
      Long n1=Long.parseLong(val1);
      Long n2=Long.parseLong( val2);
      switch (operator){
        case EQUALS:
        return n1==n2;
        case NOT_EQUALS:
        return !(n1!=n2);
        case GREATER_THAN:
        return n1>n2;
        case LESS_THAN:
        return n1<n2;
      }
      return false;
    }
    else if (fieldType==Double.class){
      Double n1=Double.parseDouble(val1);
      Double n2=Double.parseDouble( val2);
      switch (operator){
        case EQUALS:
        return n1==n2;
        case NOT_EQUALS:
        return !(n1!=n2);
        case GREATER_THAN:
        return n1>n2;
        case LESS_THAN:
        return n1<n2;
      }
      return false;
    }
    else if (fieldType==Integer.class){
      Integer n1=Integer.getInteger(val1);
      Integer n2=Integer.getInteger( val2);
      switch (operator){
        case EQUALS:
        return n1==n2;
        case NOT_EQUALS:
        return !(n1!=n2);
        case GREATER_THAN:
        return n1>n2;
        case LESS_THAN:
        return n1<n2;
      }
      return false;
    }
    

    return true;
  }

  public Specification<T> addJoinSpecification(Specification<T> filterSpec, Filter batchFilter) {

    if(batchFilter.getValue().length()>0)
    {
      //filterSpec = getSpecificationFromFiltersForBatchRecord(batchFilter);
    }
    return filterSpec;
  }


}
