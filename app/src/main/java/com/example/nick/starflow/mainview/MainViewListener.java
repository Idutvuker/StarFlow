package com.example.nick.starflow.mainview;

import com.example.nick.starflow.databases.types.StarData;

/**
 * Created by Nick on 12.04.2017.
 */
public abstract class MainViewListener
{
    public abstract void onFoundStar(StarData star);

    public abstract void updateActiveStarBtn(StarData star);

    public abstract void hideActiveStarBtn();
}
