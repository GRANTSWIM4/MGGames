package com.minegusta.mggames.util;

import java.util.Random;

public class RandomUtil {

    private static Random random = new Random();

    public static int getRandom(int max)
    {
        return random.nextInt(max) + 1;
    }
}
