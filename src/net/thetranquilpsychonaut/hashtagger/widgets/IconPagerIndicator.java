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
package net.thetranquilpsychonaut.hashtagger.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class IconPagerIndicator extends LinearLayout implements OnPageChangeListener, View.OnClickListener
{
    private ViewPager vpViewPager;

    public IconPagerIndicator( Context context )
    {
        this( context, null, 0 );
    }

    public IconPagerIndicator( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public IconPagerIndicator( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    public void setViewPager( ViewPager view )
    {
        if ( vpViewPager != null )
        {
            vpViewPager.setOnPageChangeListener( null );
        }
        vpViewPager = view;
        view.setOnPageChangeListener( this );
        setSelectedChild( vpViewPager.getCurrentItem() );
    }

    @Override
    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels )
    {
    }

    @Override
    public void onPageSelected( int position )
    {
        setSelectedChild( position );
    }

    public void setSelectedChild( int position )
    {
        ImageButton child;
        int childCount = getChildCount();
        for ( int a = 0; a < childCount; ++a )
        {
            child = ( ImageButton ) getChildAt( a );
            if ( a == position )
            {
                child.setColorFilter( getResources().getColor( ( Integer ) child.getTag() ) );
            }
            else
            {
                child.setColorFilter( null );
            }
        }
    }

    @Override
    public void onPageScrollStateChanged( int state )
    {

    }

    @Override
    public void onClick( View v )
    {
        int childPosition = indexOfChild( v );
        vpViewPager.setCurrentItem( childPosition );
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
            case HashtaggerApp.FACEBOOK_VALUE:
                imageButton.setImageDrawable( getResources().getDrawable( R.drawable.facebook_logo ) );
                imageButton.setTag( R.color.facebook_blue );
                break;
            case HashtaggerApp.GPLUS_VALUE:
                imageButton.setImageDrawable( getResources().getDrawable( R.drawable.gplus_logo ) );
                imageButton.setTag( R.color.gplus_red );
                break;
        }
        imageButton.setOnClickListener( this );
        addView( imageButton );
    }
}
