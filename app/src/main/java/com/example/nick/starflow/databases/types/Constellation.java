package com.example.nick.starflow.databases.types;

import android.util.Pair;

import org.joml.Vector3f;

import java.util.ArrayList;

/**
 * Created by Nick on 28.05.2017.
 */

public class Constellation extends ArrayList<Pair<Integer, Integer>> {
    public String name;

    public Constellation(String name) {
        this.name = name;
    }
}
