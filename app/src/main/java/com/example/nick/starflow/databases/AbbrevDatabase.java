package com.example.nick.starflow.databases;
import java.util.HashMap;

/**
 * Created by Nick on 04.06.2017.
 */

//Database for abbreviations of constellation names
public class AbbrevDatabase extends HashMap<String, String> implements MyDatabase {

    @Override
    public void addFromString(String str) {
        String[] data = str.split(",");

        put(data[0], data[1]);
    }
}
