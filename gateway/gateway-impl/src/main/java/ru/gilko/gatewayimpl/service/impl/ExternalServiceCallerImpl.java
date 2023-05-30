package ru.gilko.gatewayimpl.service.impl;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gilko.gatewayimpl.service.api.ExternalServiceCaller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExternalServiceCallerImpl implements ExternalServiceCaller {
    @Override
    public <T> Map<UUID, T> getOrEmpty(List<UUID> entityUids,
                                       Function<List<UUID>, List<T>> dataGetter,
                                       Function<T, UUID> idExtractor) {
        try {
            List<T> extractedData = dataGetter.apply(entityUids);
            log.info("Get {} entities of type {}", extractedData.size(), extractedData.getClass());

            return extractedData.stream()
                    .collect(Collectors.toMap(idExtractor, Function.identity()));
        } catch (FeignException e) {
            log.error("Unable to entities by uids {}. Exception {}", entityUids, e.getMessage());
            return Collections.emptyMap();
        }
    }

    @Override
    public <T> Optional<T> getOrEmpty(UUID entityId,
                                      Function<UUID, T> dataGetter) {
        try {
            T entity = dataGetter.apply(entityId);
            log.info("Get entity of type {}. Entity: {}", entity.getClass(), entity);

            return Optional.of(entity);
        } catch (FeignException e) {
            log.error("Unable to get entity by id {}. Exception: {}", entityId, e.getMessage());

            return Optional.empty();
        }
    }
}
