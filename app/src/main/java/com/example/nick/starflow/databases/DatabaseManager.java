package com.example.nick.starflow.databases;

import android.content.Context;

import com.example.nick.starflow.databases.AbbrevDatabase;
import com.example.nick.starflow.databases.CitiesDatabase;
import com.example.nick.starflow.databases.ConstellationDatabase;
import com.example.nick.starflow.databases.MyDatabase;
import com.example.nick.starflow.databases.StarDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Nick on 01.02.2017.
 */
public class DatabaseManager
{
    static public StarDatabase starDatabase;
    static public ConstellationDatabase constDatabase;
    static public AbbrevDatabase abbrevDatabase;
    static public CitiesDatabase citiesDatabase;
    public static boolean loaded = false;

    static public MyDatabase loadFromAssets(MyDatabase database, Context context, String filename) throws IOException
    {
        InputStream is = context.getAssets().open(filename);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        bufferedReader.readLine(); //Pass table header

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            database.addFromString(line);
        }

        bufferedReader.close();
        return database;
    }
}
