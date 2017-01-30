package com.wmcalyj.point24.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.Set;

/**
 * Created by mengchaowang on 1/26/17.
 */
public class AllGames {
    private static AllGames ourInstance;
    private static Gson gson;
    public Map<Game, Set<String>> games;


    private AllGames() {
        if (gson == null) {
            gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
                    .create();
        }
    }

    public static AllGames getInstance() {
        if (ourInstance == null) {
            ourInstance = new AllGames();
        }
        return ourInstance;
    }

    public static void setInstance(AllGames instance) {
        ourInstance = instance;
    }

// --Commented out by Inspection START (1/29/17, 11:41 PM):
//    private static void loadAllGame(Context context) {
//        ourInstance = FileService.getInstance().loadAllGameResults(context);
//        if (ourInstance == null) {
//            ourInstance = new AllGames();
//        }
//    }
// --Commented out by Inspection STOP (1/29/17, 11:41 PM)

    public String toGsonString() {
        return gson.toJson(ourInstance);
    }

    public boolean isEmpty() {
        return (ourInstance == null || ourInstance.games == null || ourInstance.games.isEmpty());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Game, Set<String>> g : games.entrySet()) {
            sb.append(g.getKey().toString()).append(": ");
            sb.append("[");
            for (String re : g.getValue()) {
                sb.append(re).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("]").append("\n");
        }
        return sb.toString();
    }

    public Set<String> getResultForNums(Game g) {
        if (games == null) {
            return null;
        }
        return games.get(g);
    }

    public void addNewResult(Game g, Set<String> answers) {
        games.put(g, answers);
    }
}
