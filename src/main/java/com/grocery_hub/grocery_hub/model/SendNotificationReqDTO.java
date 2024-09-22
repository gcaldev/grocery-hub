package com.grocery_hub.grocery_hub.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendNotificationReqDTO {

    @NotEmpty
    @Valid
    private List<ProductDTO> products;

    @Email
    @NotNull
    private String email;
}
