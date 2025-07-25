package com.devteria.identityservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

// This is not an @Entity
// Spring will automatically use Jackson to convert JSON <--> java object
//@Setter + @Getter & many other things
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    // validation in DTO classes, not inside Entity
    // message must be identical to Enum ErrorCode keys
    @Size(min = 3,message = "USERNAME_INVALID")
    String username;

    @Size(min = 8,message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    // can create custom validation annotation: age must be greater than 18 years old
    LocalDate dob;
}
