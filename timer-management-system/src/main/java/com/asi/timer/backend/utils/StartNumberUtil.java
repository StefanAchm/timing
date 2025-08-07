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

        Integer min = 10; // Minimum start number

        for(int max = 100; max <= 500; max+=20) {

            List<Integer> randomNumbers = createShuffeldList(min, max, assignedStartNumbers);

            if(!randomNumbers.isEmpty()) {
                return randomNumbers.get(0); // Return the first available random number
            }

        }

        return 0; // Return 0 if no available start number is found in the ranges

    }

    /**
     * Creates a shuffled list of integers from min to max, excluding the assigned start numbers.
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @param assignedStartNumbers list of already assigned start numbers to exclude
     * @return shuffled list of available start numbers in the range [min, max]
     */
    private static List<Integer> createShuffeldList(Integer min, Integer max, List<Integer> assignedStartNumbers) {

        List<Integer> list = IntStream.rangeClosed(min, max)
                .boxed()
                .collect(Collectors.toList());

        list.removeAll(assignedStartNumbers);

        Collections.shuffle(list);

        return list;

    }

}
