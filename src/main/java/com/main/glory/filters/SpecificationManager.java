package com.main.glory.filters;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.main.glory.services.DataConversion;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecificationManager<T> {
    private Specification<T> createSpecification(Filter input) {
      
        switch (input.getOperator()){
          
          case EQUALS:
             return ( root, query, criteriaBuilder) -> 
             
                criteriaBuilder.equal(root.get(input.getField()),
                 castToRequiredType(root.get(input.getField()).getJavaType(), 
                                    input.getValue()));
          
          case NOT_EQUALS:
             return (root, query, criteriaBuilder) -> 
                criteriaBuilder.notEqual(root.get(input.getField()),
                 castToRequiredType(root.get(input.getField()).getJavaType(), 
                                    input.getValue()));
          
          case GREATER_THAN:
             return (root, query, criteriaBuilder) -> 
                criteriaBuilder.gt(root.get(input.getField()),
                 (Number) castToRequiredType(
                        root.get(input.getField()).getJavaType(), 
                                    input.getValue()));
          
          case LESS_THAN:
             return (root, query, criteriaBuilder) -> 
                criteriaBuilder.lt(root.get(input.getField()),
                 (Number) castToRequiredType(
                        root.get(input.getField()).getJavaType(), 
                                    input.getValue()));
          
          case LIKE:
            return (root, query, criteriaBuilder) -> 
                criteriaBuilder.like(root.get(input.getField()), 
                                "%"+input.getValue()+"%");
          
          case IN:
            return (root, query, criteriaBuilder) -> 
                criteriaBuilder.in(root.get(input.getField()))
                .value(castToRequiredType(
                        root.get(input.getField()).getJavaType(), 
                        input.getValues()));
          
          default:
            throw new RuntimeException("Operation not supported yet");
        }
      }


      private Object castToRequiredType(Class fieldType, String value) {
        System.out.println(fieldType.getName()+" "+value+" ");
         if(fieldType.isAssignableFrom(Double.class)) {
           return Double.valueOf(value);
         } else if(fieldType.isAssignableFrom(Integer.class)) {
           return Integer.valueOf(value);
         } else if(Enum.class.isAssignableFrom(fieldType)) {
           return Enum.valueOf(fieldType, value);
         }
         else if(fieldType.isAssignableFrom(Long.class)) {
          return Long.valueOf(value);
        }
        else if(fieldType.isAssignableFrom(String.class)) {
          return value;
        }
        else if(fieldType.isAssignableFrom(Boolean.class)) {
          return Boolean.valueOf(value);
        }
        
        else if(fieldType.isAssignableFrom(Date.class)) {
          Date date=Date.from(Instant.now());
          try{
            date=DataConversion.stringToDate(value);
          }
          catch(Exception e){
            System.out.println(e.getMessage());
          }
          System.out.println(date.toString());
          return date;
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


       public Specification<T> getSpecificationFromFilters(List<Filter> filter){
         if(filter==null || filter.isEmpty())
         return null;
         System.out.println("Crearing specification");
         Specification<T> specification = 
                   createSpecification(filter.remove(0));
        
         for (Filter input : filter) {
           specification = specification.and(createSpecification(input));
         }
         return specification;
       }
       
       
      
}
