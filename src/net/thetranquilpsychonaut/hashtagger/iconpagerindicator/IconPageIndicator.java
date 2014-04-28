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
import android.widget.ImageView;
import android.widget.LinearLayout;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class IconPageIndicator extends LinearLayout implements OnPageChangeListener
{
    private ViewPager mViewPager;
    private ImageView imgvTwitterLogo;
    private ImageView imgvFacebookLogo;

    public IconPageIndicator( Context context )
    {
        this( context, null );
    }

    public IconPageIndicator( Context context, AttributeSet attrs )
    {
        super( context, attrs );
        inflate( context, R.layout.icon_pager_indicator, this );
        imgvTwitterLogo = ( ImageView ) findViewById( R.id.imgv_twitter_logo );
        imgvFacebookLogo = ( ImageView ) findViewById( R.id.imgv_facebook_logo );
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
    }

    @Override
    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels )
    {

    }

    @Override
    public void onPageSelected( int position )
    {
        int children = this.getChildCount();
        for ( int a = 0; a < children; ++a )
        {
            Helper.debug( this.getChildAt( a ).toString() );
        }
    }

    @Override
    public void onPageScrollStateChanged( int state )
    {

    }
}
