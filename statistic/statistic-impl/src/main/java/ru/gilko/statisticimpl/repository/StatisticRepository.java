package ru.gilko.statisticimpl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gilko.statisticimpl.domain.Statistic;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, UUID> {

    List<Statistic> findAllByEventDateIsAfter(LocalDate lastMonth);
//// 1) отчёт по отказам от аренды
////-какие модели отменяют, цена аренды
////2) в какие дни отменяют и за сколько до конца периода и возможно в процентах от всего периода
}
