package com.wmcalyj.point24.objects;

import java.util.Arrays;

/**
 * Created by mengchaowang on 1/26/17.
 */

public class Game {
    public int[] nums = new int[4];

    private Game() {
        // Do nothing
    }

    public Game(int num1, int num2, int num3, int num4) {
        nums[0] = num1;
        nums[1] = num2;
        nums[2] = num3;
        nums[3] = num4;
        Arrays.sort(nums);
    }

    public Game(int[] re) {
        Arrays.sort(re);
        nums = re;
    }

    private Game(String s1, String s2, String s3, String s4) {
        try {
            int n1 = Integer.valueOf(s1);
            int n2 = Integer.valueOf(s2);
            int n3 = Integer.valueOf(s3);
            int n4 = Integer.valueOf(s4);
            new Game(n1, n2, n3, n4);
        } catch (NumberFormatException e) {
            return;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().isAssignableFrom(Game.class)) {
            return false;
        }
        Game g = (Game) o;
        if (g.nums == null || g.nums.length < 4) {
            return false;
        }
        return this.nums[0] == g.nums[0] && this.nums[1] == g.nums[1] && this.nums[2] == g
                .nums[2] && this.nums[3] == g.nums[3];
    }

    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(nums[i]);
        }
        return sb.toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 4; i++) {
            sb.append(nums[i]).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}
