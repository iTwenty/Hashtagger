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
package net.thetranquilpsychonaut.hashtagger.iconpagerindicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
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
    private ViewPager   mViewPager;
    private ImageButton imgbTwitterLogo;
    private ImageButton imgbFacebookLogo;

    public IconPagerIndicator( Context context )
    {
        this( context, null );
    }

    public IconPagerIndicator( Context context, AttributeSet attrs )
    {
        super( context, attrs );
        inflate( context, R.layout.icon_pager_indicator, this );
        imgbTwitterLogo = ( ImageButton ) findViewById( R.id.imgb_twitter_logo );
        imgbFacebookLogo = ( ImageButton ) findViewById( R.id.imgb_facebook_logo );
        imgbTwitterLogo.setOnClickListener( this );
        imgbFacebookLogo.setOnClickListener( this );
    }

    public void setViewPager( ViewPager view )
    {
        if ( mViewPager == view )
        {
            return;
        }
        if ( mViewPager != null )
        {
            mViewPager.setOnPageChangeListener( null );
        }
        mViewPager = view;
        view.setOnPageChangeListener( this );
        setSelectedChild( mViewPager.getCurrentItem() );
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
        int childCount = getChildCount();
        int color;
        for ( int a = 0; a < childCount; ++a )
        {
            if ( a == position )
            {
                switch ( a )
                {
                    case HashtaggerApp.TWITTER_POSITION:
                        color = R.color.twitter_logo_blue;
                        break;
                    case HashtaggerApp.FACEBOOK_POSITION:
                        color = R.color.facebook_blue;
                        break;
                    default:
                        color = android.R.color.transparent;
                        break;
                }
                ( ( ImageButton ) getChildAt( a ) ).setColorFilter( getResources().getColor( color ) );
            }
            else
            {
                ( ( ImageButton ) getChildAt( a ) ).setColorFilter( null );
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
        mViewPager.setCurrentItem( childPosition );
    }
}
