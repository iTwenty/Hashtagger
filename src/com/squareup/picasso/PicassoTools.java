package com.squareup.picasso;

import android.content.Context;

/**
 * Created by itwenty on 5/29/14.
 */
public final class PicassoTools
{
    public static void clearCache( Context context )
    {
        Picasso.with( context ).cache.clear();
    }
}
