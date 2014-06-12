/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class SitesPagerIndicator extends ViewPagerIndicator implements View.OnClickListener
{

    public SitesPagerIndicator( Context context )
    {
        super( context );
    }

    public SitesPagerIndicator( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public SitesPagerIndicator( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    public void onClick( View v )
    {
        getViewPager().setCurrentItem( indexOfChild( v ) );
    }

    public void addIcon( int iconType )
    {
        ImageButton imageButton = new ImageButton( getContext(), null, android.R.style.Widget_DeviceDefault_Button_Borderless );
        LayoutParams params = new LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT, 1 );
        params.gravity = Gravity.CENTER;
        imageButton.setLayoutParams( params );
        switch ( iconType )
        {
            case HashtaggerApp.TWITTER_VALUE:
                imageButton.setImageDrawable( getResources().getDrawable( R.drawable.twitter_logo ) );
                imageButton.setTag( R.color.twitter_logo_blue );
                break;
            case HashtaggerApp.GPLUS_VALUE:
                imageButton.setImageDrawable( getResources().getDrawable( R.drawable.gplus_logo ) );
                imageButton.setTag( R.color.gplus_red );
                break;
            case HashtaggerApp.FACEBOOK_VALUE:
                imageButton.setImageDrawable( getResources().getDrawable( R.drawable.facebook_logo ) );
                imageButton.setTag( R.color.facebook_blue );
                break;

        }
        imageButton.setOnClickListener( this );
        addView( imageButton );
    }
}
