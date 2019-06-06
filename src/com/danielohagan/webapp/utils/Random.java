package com.danielohagan.webapp.utils;

import java.util.UUID;

public class Random {

    /**
     * Generate a random positive int
     *
     * @return positive int
     */
    public static int generateRandomPositiveInt() {
        int id = UUID.randomUUID().hashCode();

        if (id < 0) {
            id = Math.abs(id);
        }

        return id;
    }
}
