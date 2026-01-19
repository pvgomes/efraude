package com.efraude.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateFraudRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 300, message = "Title must be less than 300 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String caution;

    @Size(max = 2048, message = "URL must be less than 2048 characters")
    private String url;

    @Size(max = 64, message = "Country must be less than 64 characters")
    private String country;
}
