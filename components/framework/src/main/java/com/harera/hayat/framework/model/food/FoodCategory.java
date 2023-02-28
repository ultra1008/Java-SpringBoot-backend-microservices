package com.harera.hayat.framework.model.food;

import com.harera.hayat.framework.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "food_category")
public class FoodCategory extends BaseEntity {

    @Column(name = "arabic_name")
    private String arabicName;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "description")
    private String description;
}
