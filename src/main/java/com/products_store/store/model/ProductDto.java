package com.products_store.store.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProductDto
{
    @NotEmpty(message = "The name is required")
    private String name;
    @NotEmpty(message = "The brand is required")
    private String brand;
    @NotEmpty(message = "The category is required")
    private String category;
    @Min(0)
    private double price;

    @Size(min=10,message = ("The description should be at least 10 characters"))
    @Size(max=2000,message = ("The description can not exceed 2000 characters"))
    private String description;

    private MultipartFile imgFileName;

    public @NotEmpty(message = "The name is required") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "The name is required") String name) {
        this.name = name;
    }

    public @NotEmpty(message = "The brand is required") String getBrand() {
        return brand;
    }

    public void setBrand(@NotEmpty(message = "The brand is required") String brand) {
        this.brand = brand;
    }

    public @NotEmpty(message = "The category is required") String getCategory() {
        return category;
    }

    public void setCategory(@NotEmpty(message = "The category is required") String category) {
        this.category = category;
    }

    @Min(0)
    public double getPrice() {
        return price;
    }

    public void setPrice(@Min(0) double price) {
        this.price = price;
    }

    public @Size(min = 10, message = ("The description should be at least 10 characters")) @Size(max = 2000, message = ("The description can not exceed 2000 characters")) String getDescription() {
        return description;
    }

    public void setDescription(@Size(min = 10, message = ("The description should be at least 10 characters")) @Size(max = 2000, message = ("The description can not exceed 2000 characters")) String description) {
        this.description = description;
    }

    public MultipartFile getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(MultipartFile imgFileName) {
        this.imgFileName = imgFileName;
    }
}
