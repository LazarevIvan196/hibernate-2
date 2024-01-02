package com.javarush.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating,String> {
    @Override
    public String convertToDatabaseColumn(Rating rating) {
        return rating.getVal();
    }

    @Override
    public Rating convertToEntityAttribute(String s) {
        Rating [] rating = Rating.values();
        for (Rating rating1 : rating) {
            if(rating1.getVal().equals(s)){
                return rating1;
            }
        }
        return null;
    }
}
