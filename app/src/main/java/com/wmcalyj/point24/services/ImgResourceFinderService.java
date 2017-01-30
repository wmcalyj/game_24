package com.wmcalyj.point24.services;

import com.wmcalyj.point24.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengchaowang on 1/21/17.
 */
public class ImgResourceFinderService {
    private static final ImgResourceFinderService ourInstance = new ImgResourceFinderService();
    private Map<String, Integer> resourceFinder;

    private ImgResourceFinderService() {
        if (resourceFinder == null) {
            resourceFinder = new HashMap<>();
        }
        resourceFinder.put("c1", R.drawable.c1);
        resourceFinder.put("c2", R.drawable.c2);
        resourceFinder.put("c3", R.drawable.c3);
        resourceFinder.put("c4", R.drawable.c4);
        resourceFinder.put("c5", R.drawable.c5);
        resourceFinder.put("c6", R.drawable.c6);
        resourceFinder.put("c7", R.drawable.c7);
        resourceFinder.put("c8", R.drawable.c8);
        resourceFinder.put("c9", R.drawable.c9);
        resourceFinder.put("c10", R.drawable.c10);
        resourceFinder.put("c11", R.drawable.c11);
        resourceFinder.put("c12", R.drawable.c12);
        resourceFinder.put("c13", R.drawable.c13);
        resourceFinder.put("d1", R.drawable.d1);
        resourceFinder.put("d2", R.drawable.d2);
        resourceFinder.put("d3", R.drawable.d3);
        resourceFinder.put("d4", R.drawable.d4);
        resourceFinder.put("d5", R.drawable.d5);
        resourceFinder.put("d6", R.drawable.d6);
        resourceFinder.put("d7", R.drawable.d7);
        resourceFinder.put("d8", R.drawable.d8);
        resourceFinder.put("d9", R.drawable.d9);
        resourceFinder.put("d10", R.drawable.d10);
        resourceFinder.put("d11", R.drawable.d11);
        resourceFinder.put("d12", R.drawable.d12);
        resourceFinder.put("d13", R.drawable.d13);
        resourceFinder.put("s1", R.drawable.s1);
        resourceFinder.put("s2", R.drawable.s2);
        resourceFinder.put("s3", R.drawable.s3);
        resourceFinder.put("s4", R.drawable.s4);
        resourceFinder.put("s5", R.drawable.s5);
        resourceFinder.put("s6", R.drawable.s6);
        resourceFinder.put("s7", R.drawable.s7);
        resourceFinder.put("s8", R.drawable.s8);
        resourceFinder.put("s9", R.drawable.s9);
        resourceFinder.put("s10", R.drawable.s10);
        resourceFinder.put("s11", R.drawable.s11);
        resourceFinder.put("s12", R.drawable.s12);
        resourceFinder.put("s13", R.drawable.s13);
        resourceFinder.put("h1", R.drawable.h1);
        resourceFinder.put("h2", R.drawable.h2);
        resourceFinder.put("h3", R.drawable.h3);
        resourceFinder.put("h4", R.drawable.h4);
        resourceFinder.put("h5", R.drawable.h5);
        resourceFinder.put("h6", R.drawable.h6);
        resourceFinder.put("h7", R.drawable.h7);
        resourceFinder.put("h8", R.drawable.h8);
        resourceFinder.put("h9", R.drawable.h9);
        resourceFinder.put("h10", R.drawable.h10);
        resourceFinder.put("h11", R.drawable.h11);
        resourceFinder.put("h12", R.drawable.h12);
        resourceFinder.put("h13", R.drawable.h13);
    }

    public static ImgResourceFinderService getInstance() {
        return ourInstance;
    }

    public int findResource(String cardId) {
        return resourceFinder.get(cardId);
    }
}
