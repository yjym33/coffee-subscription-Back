package com.beanbliss.subscription.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionDtos {
    @Data
    public static class CreateRequest {
        @NotNull
        private Long productId;

        @NotBlank
        private String frequency; // weekly | biweekly | monthly

        @Min(1)
        private int quantity;
    }

    @Data
    public static class UpdateRequest {
        @NotNull
        private Long id;
        private String frequency;
        private Integer quantity;
        private String status; // active | paused | cancelled
    }
}


