package com.training.springbootbuyitem.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponseDto extends CreateUserResponseDto {

    private String name;
    private String email;

}
