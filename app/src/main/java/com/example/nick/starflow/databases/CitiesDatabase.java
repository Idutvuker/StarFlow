package com.example.nick.starflow.databases;

import com.example.nick.starflow.databases.types.City;

import java.util.ArrayList;

/**
 * Created by Nick on 25.06.2017.
 */

public class CitiesDatabase extends ArrayList<City> implements MyDatabase
{
    @Override
    public void addFromString(String str) {
        String[] data = str.split(",");
        add(new City(data[0], Float.valueOf(data[1]), Float.valueOf(data[2]), data[3]));
    }

    private String[] entries = null;
    public String[] getEntries()
    {
        if (entries == null)
            entries = new String[size()];

        for (int i = 0; i < size(); i++)
            entries[i] = get(i).name + ", " + get(i).county;

        return entries;
    }


    private String[] ids = null;
    public String[] getIds()
    {
        if (ids == null)
            ids = new String[size()];

        for (int i = 0; i < size(); i++)
            ids[i] = String.valueOf(i);

        return ids;
    }

}
