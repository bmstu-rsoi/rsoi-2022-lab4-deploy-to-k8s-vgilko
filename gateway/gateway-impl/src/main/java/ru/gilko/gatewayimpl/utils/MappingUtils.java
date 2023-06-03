package ru.gilko.gatewayimpl.utils;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.gilko.carsapi.dto.CarOutDto;
import ru.gilko.gatewayapi.dto.wrapper.PageableCollectionOutDto;

@Component
public class MappingUtils {
    private final ModelMapper modelMapper;

    public MappingUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public <T> PageableCollectionOutDto<T> mapToPageCollectionOutDto(Page<CarOutDto> page, Class<T> destinationClass) {
        Page<T> mappedPage = page.map(car -> modelMapper.map(car, destinationClass));

        return buildPageCollectionOutDto(mappedPage);
    }

    private <T> PageableCollectionOutDto<T> buildPageCollectionOutDto(Page<T> page) {
        return new PageableCollectionOutDto<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalPages());
    }
}
