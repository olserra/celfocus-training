package com.training.springbootbuyitem.configuration;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Optional;

@Configuration
public class ModelMapperConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		Converter<Double, BigDecimal> converter = mappingContext -> BigDecimal
				.valueOf(Optional.ofNullable(mappingContext.getSource()).orElse(Double.valueOf(0)));
		modelMapper.createTypeMap(Double.class, BigDecimal.class).setConverter(converter);
		return modelMapper;
	}

}
