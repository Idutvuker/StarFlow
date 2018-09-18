package com.example.nick.starflow.databases.types;

import org.joml.Vector3f;

import static com.example.nick.starflow.control.Constants.STAR_DEF_MAG;
import static com.example.nick.starflow.control.Constants.STAR_MAX_SIZE;
import static com.example.nick.starflow.control.Constants.STAR_MIN_SIZE;
import static com.example.nick.starflow.databases.StarDatabaseRows.BF;
import static com.example.nick.starflow.databases.StarDatabaseRows.DEC;
import static com.example.nick.starflow.databases.StarDatabaseRows.ID;
import static com.example.nick.starflow.databases.StarDatabaseRows.MAG;
import static com.example.nick.starflow.databases.StarDatabaseRows.PROPER;
import static com.example.nick.starflow.databases.StarDatabaseRows.RA;
import static com.example.nick.starflow.databases.StarDatabaseRows.RV;
import static com.example.nick.starflow.databases.StarDatabaseRows.SPECT;
import static com.example.nick.starflow.databases.StarDatabaseRows.XN;
import static com.example.nick.starflow.databases.StarDatabaseRows.YN;
import static com.example.nick.starflow.databases.StarDatabaseRows.ZN;


/**
 * Created by Nick on 01.02.2017.
 */
public class StarData {
    public final int id;
    public final String proper;

    public final float ra;
    public final float dec;
    public Vector3f position = new Vector3f();
    public final String spect;
    public int color = 0xFFFFFFFF; // default color
    public final float apmag;
    public final String bf;
    public final float rv;

    public Vector3f viewpos = new Vector3f();

    public int px_size;
    public int alpha = 0xFF;

    public StarData(String[] data) {
        id = Integer.parseInt(data[ID.ordinal()]);
        proper = data[PROPER.ordinal()];

        position.set(Float.parseFloat(data[XN.ordinal()]),
                Float.parseFloat(data[YN.ordinal()]),
                Float.parseFloat(data[ZN.ordinal()]));

        spect = data[SPECT.ordinal()];
        apmag = Float.parseFloat(data[MAG.ordinal()]);
        ra = Float.parseFloat(data[RA.ordinal()]) * 15;
        dec = Float.parseFloat(data[DEC.ordinal()]);
        bf = data[BF.ordinal()];
        rv = Float.parseFloat(data[RV.ordinal()]);

        //Get star color
        char f = spect.charAt(0);
        if (SpectralType.all.indexOf(f) != -1)
            color = SpectralType.valueOf(String.valueOf(f)).color;


        //Star size on screen
        px_size = (int) Math.max(Math.min(
                Math.round((Math.pow(1.9, STAR_DEF_MAG - apmag))),
                STAR_MAX_SIZE), STAR_MIN_SIZE);
    }

    public String printInfo() {
        return "Proper name:" + proper +
                "\nHYG id: " + id +
                "\nThe Bayer designation: " + bf +
                "\nRight ascension: " + ra +
                "°\nDeclination: " + dec +
                "°\nSpecral class: " + spect +
                "\nApparent magnitude:" + apmag +
                "\nRadial velocity: " + rv;

    }

    @Override
    public String toString() {
        return proper;
    }
}
