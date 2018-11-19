package com.spectre.utility;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

class WebService
{
    private final Context mContext;
    public WebService(Context ctx)
    {
        //The only context that is safe to keep without tracking its lifetime
        //is application context. Activity context and Service context can expire
        //and we do not want to keep reference to them and prevent
        //GC from recycling the memory.
        mContext = ctx.getApplicationContext();
    }

    public void someFunc(String filename) throws IOException
    {
        InputStream iS = mContext.getAssets().open("www/"+filename);
    }
}