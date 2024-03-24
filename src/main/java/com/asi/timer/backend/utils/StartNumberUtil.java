package com.asi.timer.backend.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StartNumberUtil {

    private StartNumberUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Integer getRandomStartNumber(List<Integer> assignedStartNumbers) {

        Integer min = 1;
        Integer randomStartNumber = 0;

        for(int max = 100; max <= 500; max+=20) {

            List<Integer> randomNumbers = createShuffeldList(min, max, assignedStartNumbers);

            if(!randomNumbers.isEmpty()) {
                randomStartNumber = randomNumbers.get(0);
                break;
            }

        }

        return randomStartNumber;

    }

    private static List<Integer> createShuffeldList(Integer min, Integer max, List<Integer> assignedStartNumbers) {

        List<Integer> list = IntStream.rangeClosed(min, max)
                .boxed()
                .collect(Collectors.toList());

        list.removeAll(assignedStartNumbers);

        Collections.shuffle(list);

        return list;

    }

}
