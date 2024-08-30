package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectResponse {
    private boolean valid;

    // Getter
    public boolean isValid() {
        return valid;
    }

    // Setter
    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
