package com.vinieduoliveira.ecommerce.model;

import com.vinieduoliveira.ecommerce.common.BaseModel;
import com.vinieduoliveira.ecommerce.model.enuns.CategoryType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

@Entity
@Table(name = "tb_category")
public class Category extends BaseModel {

    private Integer categoryType;

    @NotBlank
    private String description;

    public Category() {
    }

    public Category(CategoryType categoryType, String description) {
        setCategoryType(categoryType);
        this.description = description;
    }

    public CategoryType getCategoryType() {
        return CategoryType.valueOf(categoryType);
    }

    public void setCategoryType(@NonNull CategoryType categoryType) {
        this.categoryType = categoryType.getCode();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryType=" + categoryType +
                ", description='" + description + '\'' +
                '}';
    }
}