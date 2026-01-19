package com.efraude.api.dto;

import com.efraude.domain.model.enums.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {
    @NotNull(message = "Vote type is required")
    private VoteType type;
}
