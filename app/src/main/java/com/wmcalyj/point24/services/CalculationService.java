package com.wmcalyj.point24.services;

import com.wmcalyj.point24.AllGames;
import com.wmcalyj.point24.Game;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class CalculationService {
    private static Random random;
    private static CalculationService instance;
    private static Set<String> answers;
    private static final Double DIFF = Math.pow(10, -9);

    private CalculationService() {
        random = new Random();
        random.setSeed(System.currentTimeMillis());

    }

    public static CalculationService getInstance() {
        if (instance == null) {
            instance = new CalculationService();
        }
        return instance;
    }

    private static boolean solvableValues(Set<String> results) {
        return results != null && results.size() > 0;
    }

    private static void filterOutResult(Set<String> results, Map<String, Double> allResults,
                                        String equ) {
        for (Entry<String, Double> result : allResults.entrySet()) {
            if (Math.abs(result.getValue() - 24) <= DIFF) {
                results.add(result.getKey());
            }
        }

    }

    private static Set<String> getFormedEquations(int[] nums) {
        Set<String> re = new HashSet<>();
        Set<Integer> used = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            used.add(i);
            formNextNumber(nums, i, String.valueOf(nums[i]), used, re);
            used.remove(i);
        }
        return re;
    }

    private static void formNextNumber(int[] nums, int n, String tmp, Set<Integer> used,
                                       Set<String> tmpResult) {
        if (used.size() == 4) {
            // System.out.println(tmp);
            tmpResult.add(tmp);
            return;
        }

        for (int i = 0; i < 4; i++) {
            if (!used.contains(i)) {
                used.add(i);
                formNextNumber(nums, i, tmp + "+" + nums[i], used, tmpResult);
                formNextNumber(nums, i, tmp + "-" + nums[i], used, tmpResult);
                formNextNumber(nums, i, tmp + "*" + nums[i], used, tmpResult);
                formNextNumber(nums, i, tmp + "/" + nums[i], used, tmpResult);
                used.remove(i);
            }
        }
    }

// --Commented out by Inspection START (1/29/17, 11:41 PM):
//    public Set<String> getResult(int[] nums) {
//        Set<String> formedEquations = getFormedEquations(nums);
//        Set<String> results = new HashSet<>();
//        for (String equ : formedEquations) {
//            Map<String, Double> allResults = computeAllResults(equ);
//            filterOutResult(results, allResults, equ);
//        }
//        return results;
//    }
// --Commented out by Inspection STOP (1/29/17, 11:41 PM)


    private Map<String, Double> computeAllResults(String input) {
        Map<String, Double> re = new HashMap<>();
        for (int j = input.length(), i = j - 1; i >= 0; i--) {
            char c = input.charAt(i);
            Map<String, Double> leftList, rightList;
            if (!Character.isDigit(c)) {
                leftList = computeAllResults(input.substring(0, i));
                rightList = computeAllResults(input.substring(i + 1, j));
                re.putAll(getMapResult(leftList, rightList, c));
            }
        }
        if (re.size() == 0) {
            re.put(input, Double.valueOf(input));
        }
        return re;
    }

    private Map<String, Double> getMapResult(Map<String, Double> leftMap, Map<String,
            Double> rightMap, char c) {
        Map<String, Double> re = new HashMap<>();
        for (Entry<String, Double> l : leftMap.entrySet()) {
            for (Entry<String, Double> r : rightMap.entrySet()) {
                StringBuilder ls = new StringBuilder();
                StringBuilder rs = new StringBuilder();
                if (!isNumber(l.getKey())) {
                    ls.append("(").append(l.getKey()).append(")");
                } else {
                    ls.append(l.getKey());
                }
                if (!isNumber(r.getKey())) {
                    rs.append("(").append(r.getKey()).append(")");
                } else {
                    rs.append(r.getKey());
                }
                switch (c) {

                    case '+':
                        re.put(ls.toString() + "+" + rs.toString(), l.getValue() + r.getValue());
                        break;

                    case '-':
                        re.put(ls.toString() + "-" + rs.toString(), l.getValue() - r.getValue());

                        break;

                    case '*':
                        re.put(ls.toString() + "*" + rs.toString(), l.getValue() * r.getValue());

                        break;

                    case '/':
                        if (r.getValue() != 0) {
                            re.put(ls.toString() + "/" + rs.toString(), l.getValue() / r.getValue
                                    ());
                        }
                        break;

                }
            }
        }
        return re;
    }

    private boolean isNumber(String key) {
        return key.indexOf('+') == -1 && key.indexOf('-') == -1 && key.indexOf('*') == -1 && key
                .indexOf('/') == -1;
    }

    public Game generateGame(int k) {
        if (k <= 13) {
            return generatePreCalculatedAnswerNumber(k);
        } else {
            Set<String> results;
            int[] re = new int[4];
            do {
                for (int i = 0; i < 4; i++) {
                    re[i] = random.nextInt(k) + 1; // [1-k]
                }
                results = new HashSet<>(Collections.singletonList(getSingleResult(re)));
            } while (!solvableValues(results));
            Game g = new Game(re[0], re[1], re[2], re[3]);
            answers = results;
            AllGames.getInstance().addNewResult(g, answers);
            return g;
        }
    }

    private Game generatePreCalculatedAnswerNumber(int k) {
        int[] nums = new int[4];
        Game g = null;
        Set<String> re = null;
        while (g == null || re == null || re.size() == 0) {
            for (int i = 0; i < 4; i++) {
                nums[i] = random.nextInt(k) + 1; // [1-k]
            }
            g = new Game(nums);
            re = AllGames.getInstance().getResultForNums(g);
        }
        answers = re;
        return g;
    }

    public String getSingleResult(int[] nums) {
        Set<String> formedEquations = getFormedEquations(nums);
        for (String equ : formedEquations) {
            Map<String, Double> allResults = computeAllResults(equ);
            for (Entry<String, Double> result : allResults.entrySet()) {
                if (Math.abs(result.getValue() - 24) <= DIFF) {
                    return result.getKey();
                }
            }
        }
        return "";
    }

    public String getSingleResult(Game g) {
        return getSingleResult(g.nums);
    }

    public Set<String> getAllAnswers() {
        return answers;
    }

}
