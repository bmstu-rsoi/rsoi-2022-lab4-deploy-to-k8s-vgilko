package ru.gilko.gatewayimpl.config.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Configuration;
import ru.gilko.gatewayapi.dto.rental.RentalDto;
import ru.gilko.gatewayapi.dto.rental.StatisticDto;

import javax.annotation.PostConstruct;

@Configuration
public class RentalDtoToStatisticDto {
    private final ModelMapper modelMapper;


    public RentalDtoToStatisticDto(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    void createMapping() {
        modelMapper.addConverter(new Converter<RentalDto, StatisticDto>() {
            @Override
            public StatisticDto convert(MappingContext<RentalDto, StatisticDto> mappingContext) {
                RentalDto source = mappingContext.getSource();

                StatisticDto statisticDto = new StatisticDto();

                statisticDto.setDateFrom(source.getDateFrom());
                statisticDto.setDateTo(source.getDateTo());
                statisticDto.setBrand(source.getCar().getBrand());
                statisticDto.setModel(source.getCar().getModel());
                statisticDto.setPrice(source.getPayment().getPrice());

                return statisticDto;
            }
        });
    }
}
