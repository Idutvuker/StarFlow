package com.example.nick.starflow.databases;

import com.example.nick.starflow.databases.types.StarData;

import java.util.ArrayList;

/**
 * Created by Nick on 02.02.2017.
 */

public class StarDatabase extends ArrayList<StarData> implements MyDatabase
{
    private ArrayList<StarData> named = new ArrayList<>();

    @Override
    public void addFromString(String str)
    {
        StarData star = new StarData(str.split(","));
        add(star);

        if (star.proper.length() > 0) {
            named.add(star);
        }
    }

    public ArrayList<StarData> getNamedStars() {
        return named;
    }
}
