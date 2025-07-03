package com.devteria.identityservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// When serialize this object to JSON, exclude fields with value of `null`
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> { // generic class
    int code = 1000;
    String message;
    T result;
}
