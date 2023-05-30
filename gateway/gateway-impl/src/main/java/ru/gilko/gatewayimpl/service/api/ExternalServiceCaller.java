package ru.gilko.gatewayimpl.service.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface ExternalServiceCaller {

    <T> Map<UUID, T> getOrEmpty(List<UUID> entityUids,
                                Function<List<UUID>, List<T>> dataGetter,
                                Function<T, UUID> idExtractor);

    <T> Optional<T> getOrEmpty(UUID entityId,
                               Function<UUID, T> dataGetter);
}
