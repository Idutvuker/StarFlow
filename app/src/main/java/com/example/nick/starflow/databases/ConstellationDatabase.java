package com.example.nick.starflow.databases;


import android.util.Pair;

import com.example.nick.starflow.databases.types.Constellation;

import java.util.ArrayList;

/**
 * Created by Nick on 26.05.2017.
 */
public class ConstellationDatabase extends ArrayList<Constellation> implements MyDatabase {
    @Override
    public void addFromString(String str) {
        String[] data = str.split(",");
        Constellation c = new Constellation(DatabaseManager.abbrevDatabase.get(data[0]));

        for (int i = 1; i < data.length; i++) {
            String[] d = data[i].split(" ");
            c.add(new Pair<>(Integer.valueOf(d[0]), Integer.valueOf(d[1])));
        }

        add(c);

    }


}
