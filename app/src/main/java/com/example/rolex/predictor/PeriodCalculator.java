package com.example.rolex.predictor;

import android.util.Log;

import com.example.rolex.period_days.PeriodDaysBean;
import com.example.rolex.period_days.PeriodDaysManager;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class PeriodCalculator {

    private static final String TAG = "PeriodCalculator";

    private final PeriodDaysManager manager;

    public PeriodCalculator(PeriodDaysManager manager) {
        this.manager = manager;
    }

    public void calculate() {
        List<PeriodDaysBean> allBeans = manager.getAllPeriodDaysBeans();

        if (allBeans.size() == 1) {
            throw new IllegalStateException("Illegal state, there have to be at least two periods!");
        }

        final LocalDate lastPeriodDate = actualLastPeriodDate();
        allBeans = Lists.newArrayList(Iterables.filter(allBeans, new Predicate<PeriodDaysBean>() {
            @Override
            public boolean apply(PeriodDaysBean periodDaysBean) {
                return !periodDaysBean.getEarliestDate().isAfter(lastPeriodDate) &&
                        !periodDaysBean.getLatestDate().isAfter(lastPeriodDate);
            }
        }));


        allBeans.addAll(computeAllPeriodDaysSinceLastPeriod(lastPeriodDate));
        PeriodDaysBean nextPeriod = allBeans.get(allBeans.size() - 1);
        LocalDate now = new LocalDate();

        if (!now.isBefore(nextPeriod.getEarliestDate())) { // new period start
            LocalDate currentPeriodStart = nextPeriod.getPeriodDays().get(0);
            manager.updateLastPeriodDate(currentPeriodStart);
            allBeans.addAll(computeAllPeriodDaysSinceLastPeriod(currentPeriodStart));
        } else {
            PeriodDaysBean actualPeriod = allBeans.get(allBeans.size() - 2);
            manager.updateLastPeriodDate(actualPeriod.getPeriodDays().get(0));
        }

        manager.updateAllPeriodBeans(allBeans);
    }

    private LocalDate actualLastPeriodDate() {
        Optional<LocalDate> lastPeriodDateOpt = manager.getLastPeriodDate();
        if (!lastPeriodDateOpt.isPresent()) {
            Log.e(TAG, "Recalculation on demand and no last period date found!");
            throw new IllegalStateException("Lack of last period date!");
        }
        return lastPeriodDateOpt.get();
    }

    private List<PeriodDaysBean> computeAllPeriodDaysSinceLastPeriod(LocalDate lastPeriodDate) {
        List<PeriodDaysBean> list = new ArrayList<>();
        LocalDate now = new LocalDate();
        LocalDate lastPeriod = new LocalDate(lastPeriodDate);
        int periodLength = manager.getPeriodLength();
        PeriodDaysBean bean;

        do  {
            bean = computePeriodBean(lastPeriod);
            list.add(bean);
            lastPeriod = lastPeriod.plusDays(periodLength);
        } while (!bean.getEarliestDate().isAfter(now));

        return list;
    }

    private PeriodDaysBean computePeriodBean(LocalDate periodStart) {
        List<LocalDate> periodDays = getPeriod(periodStart);
        List<LocalDate> fertileDays = getFertile(periodStart);
        List<LocalDate> fertileDay = getFertiler(periodStart);
        LocalDate ovulationDay = getOvulation(fertileDay);

        return new PeriodDaysBean(periodDays, fertileDays, ovulationDay);
    }

    private List<LocalDate> getPeriod(LocalDate periodStart) {
        List<LocalDate> period = new ArrayList<>();
        LocalDate periodDay = new LocalDate(periodStart);
        period.add(periodDay);

        for (int i = 0; i < manager.getMenstruationLength() - 1; ++i) {
            periodDay = periodDay.plusDays(1);
            period.add(periodDay);
        }

        return period;
    }




    private List<LocalDate> getFertile(LocalDate periodStart) {
        int difa = manager.getMenstruationLength();
        int difb = manager.getPeriodLength() - 8;
        LocalDate fertileDay = periodStart.plusDays(difa);
        LocalDate fertileDayb = periodStart.plusDays(difb);
        List<LocalDate> fertile = new ArrayList<>();
        fertile.add(fertileDay);


        for (int i = 0; i < 1; ++i) {
            fertileDay = fertileDay.plusDays(1);
            fertile.add(fertileDay);
        }
        fertile.add(fertileDayb);
        for (int i = 0; i < 7; ++i) {
            fertileDayb = fertileDayb.plusDays(1);
            fertile.add(fertileDayb);
        }

        return fertile;
    }

    private List<LocalDate> getFertiler(LocalDate periodStart) {
        int diff = manager.getPeriodLength() - 20;
        LocalDate fertileDayw = periodStart.plusDays(diff);
        List<LocalDate> fertiler = new ArrayList<>();
        fertiler.add(fertileDayw);

        for (int i = 0; i < 6; ++i) {
            fertileDayw = fertileDayw.plusDays(1);
            fertiler.add(fertileDayw);
        }

        return fertiler;
    }

    private LocalDate getOvulation(List<LocalDate> fertileDay) {

        int size = fertileDay.size();
        if (size < 2) {
            return fertileDay.get(0);
        } else {
            return fertileDay.get(size - 2);
        }
    }
}
