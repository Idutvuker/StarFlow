package com.example.nick.starflow.databases.types;

/**
 * Created by Nick on 31.03.2017.
 */

public enum SpectralType
{
    O(0xff9bb0ff),
    B(0xffaabfff),
    A(0xffcad7ff),
    F(0xfff8f7ff),
    G(0xfffff4ea),
    K(0xffffd2a1),
    M(0xffffcc6f);

    public final int color;
    public static final String all = "OBAFGKM";


    SpectralType(int color) {
        this.color = color;
    }
}
