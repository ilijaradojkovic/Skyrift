package bees.io.Berzza.domain;

import bees.io.Berzza.domain.enums.GameState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGenerator {
    private static final Random rand = new Random();

    public static double generateMaxMultiplier() {
        double max = roundTheNumber(returnGameNumber());
        if (max > 1000) {
            max = 1000;
        }
        return max;
    }

    // ✅ Samo rastući brojevi
    public static Game generateListFromMaxIncreasingOnly(double gameMultiplier) {
        Game game = new Game();
        game.setMaxMultiplier(gameMultiplier);
        List<Double> runList = new ArrayList<>();
        game.setRunList(runList);

        runList.add(1.0);

        if (gameMultiplier <= 1.0) {
            return game; // fail instantly
        }

        int steps = generateRandomIntBetween(5, 12);
        double previous = 1.0;

        for (int i = 0; i < steps - 1; i++) {
            double next = generateRandomDoubleBetweenRounded(previous + 0.01, gameMultiplier);
            if (next >= gameMultiplier) {
                break;
            }
            runList.add(next);
            previous = next;
        }

        runList.add(gameMultiplier);
        game.setBreakingPoints(0);
        return game;
    }

    // ✅ Interpolacija - zadržava rastući niz
    public static Game interpolateList(Game game) {
        List<Integer> interpolatedList = new ArrayList<>();
        int step = 25;
        int counter = 0;
        int bonusSpeed = 0;

        List<Double> original = game.getRunList();

        for (int i = 0; i < original.size() - 1; i++) {
            int start = (int) (original.get(i) * 1000);
            int end = (int) (original.get(i + 1) * 1000);
            interpolatedList.add(start);

            for (int j = start + step; j < end; j += step) {
                interpolatedList.add(j);
                counter++;
                if (counter > 10 && counter % 2 == 0) {
                    bonusSpeed += 3;
                }
                if (counter % 10 == 0) {
                    step = generateRandomIntBetween(20, 40) + bonusSpeed;
                }
            }
        }

        List<Double> interpolatedListDouble = new ArrayList<>();
        for (Integer x : interpolatedList) {
            interpolatedListDouble.add((double) x / 1000.0);
        }

        interpolatedListDouble.add(original.get(original.size() - 1));
        game.setRunList(interpolatedListDouble);
        game.setGameState(GameState.CREATED);
        return game;
    }

    // ===================== HELPER METODE =====================

    public static double roundTheNumber(double x) {
        BigDecimal bd = new BigDecimal(x);
        bd = bd.setScale(2, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }

    // Nova logika za generisanje broja sa biasom ka manjim vrednostima i retkim velikim vrednostima
    public static double returnGameNumber() {
        int x = rand.nextInt(100);
        if (x % 33 == 0) {
            return -1.0;  // fail uslov
        }

        double value;
        double chance = rand.nextDouble();

        if (chance < 0.85) {
            // 85% šanse da dobijemo manje kvote (do 20) sa jakim biasom
            value = Math.pow(rand.nextDouble(), 3) * 20;
        } else {
            // 15% šanse za veće kvote (do 1000), sa blažim biasom
            value = Math.pow(rand.nextDouble(), 2) * 1000;
        }

        return roundTheNumber(0.01 + value);
    }

    public static double generateRandomDoubleBetweenRounded(double min, double max) {
        if (min > max) {
            return min;
//            throw new IllegalArgumentException("Max must be greater than min");
        } else if (min == max) {
            return min;
        } else {
            double x = min + (max - min) * rand.nextDouble();
            return roundTheNumber(x);
        }
    }

    public static int generateRandomIntBetween(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
