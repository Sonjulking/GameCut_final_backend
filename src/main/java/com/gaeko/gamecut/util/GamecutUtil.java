package com.gaeko.gamecut.util;

import java.util.Random;

public class GamecutUtil {
    public String getCode(int n) {
        StringBuilder data = new StringBuilder();
        Random r = new Random();
        for (int i = 1; i <= n; i++) {
            data.append(r.nextInt(10));
        }

        return data.toString();
    }
}
