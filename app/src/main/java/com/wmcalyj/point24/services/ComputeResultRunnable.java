package com.wmcalyj.point24.services;

import android.content.Context;

import com.wmcalyj.point24.AllGames;
import com.wmcalyj.point24.Game;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mengchaowang on 1/26/17.
 */

class ComputeResultRunnable implements Runnable {
    private static final String TAG = "ComputeResult";
    private Context mContext;
    private int maxNum;

    public ComputeResultRunnable(Context mContext, int maxNum) {
        this.maxNum = maxNum;
        this.mContext = mContext;
    }

    private ComputeResultRunnable() {
        // Do nothing
    }

    @Override
    public void run() {
        System.out.println("All Games start");
        if (!AllGames.getInstance().isEmpty()) {
            System.out.println("All Games is empty");
            return;
        }
        Map<Game, Set<String>> games = new HashMap<>();
        for (int i = 1; i <= maxNum; i++) {
            for (int j = 1; j <= maxNum; j++) {
                for (int m = 1; m <= maxNum; m++) {
                    for (int n = 1; n <= maxNum; n++) {
                        Game g = new Game(i, j, m, n);
                        if (games.containsKey(g)) {
                            continue;
                        }
                        String result = CalculationService.getInstance().getSingleResult(g.nums);
                        if (result != null && !result.isEmpty()) {
                            games.put(g, new HashSet<>(Collections.singletonList(result)));
                        }
                        System.out.println(g.toString());
                    }
                }
            }
        }

        AllGames.getInstance().games = games;
        FileService.getInstance().saveAllGameResults(mContext);
        System.out.println("All Games finished");
        System.out.println(games.size());

    }
}
