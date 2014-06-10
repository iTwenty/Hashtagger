package net.thetranquilpsychonaut.hashtagger.utils;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import com.twitter.Autolink;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesActivity;

/**
 * Created by itwenty on 6/10/14.
 */
public class Linkifier
{
    private static Autolink twitterTextLinker = new Autolink();

    public static Spannable getLinkedTwitterText( String text )
    {
        String htmlText = twitterTextLinker.autoLink( text );
        SpannableString ss = new SpannableString( Html.fromHtml( htmlText ) );
        return stripUnderlines( ss );
    }

    private static Spannable stripUnderlines( Spannable spannable )
    {
        URLSpan[] spans = spannable.getSpans( 0, spannable.length(), URLSpan.class );
        for ( URLSpan span : spans )
        {
            int start = spannable.getSpanStart( span );
            int end = spannable.getSpanEnd( span );
            boolean isHashtag = spannable.subSequence( start, start + 1 ).toString().equals( "#" );
            spannable.removeSpan( span );
            if ( isHashtag )
            {
                span = new HashtagSpan( span.getURL(), spannable.subSequence( start, end ) );
            }
            else
            {
                span = new URLSpanNoUnderline( span.getURL() );
            }
            spannable.setSpan( span, start, end, 0 );
        }
        return spannable;
    }

    private static class URLSpanNoUnderline extends URLSpan
    {
        public URLSpanNoUnderline( String url )
        {
            super( url );
        }

        @Override
        public void updateDrawState( TextPaint ds )
        {
            super.updateDrawState( ds );
            ds.setUnderlineText( false );
        }
    }

    private static final class HashtagSpan extends URLSpanNoUnderline
    {
        CharSequence hashtag;

        public HashtagSpan( String url, CharSequence hashtag )
        {
            super( url );
            this.hashtag = hashtag;
        }

        @Override
        public void onClick( final View widget )
        {
            PopupMenu menu = new PopupMenu( widget.getContext(), widget );
            menu.getMenuInflater().inflate( R.menu.hashtag_popup_menu, menu.getMenu() );
            MenuItem searchHashtagItem = menu.getMenu().findItem( R.id.it_search_for_hashtag );
            searchHashtagItem.setTitle( "Search for " + this.hashtag );
            searchHashtagItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick( MenuItem item )
                {
                    Intent intent = new Intent( Intent.ACTION_SEARCH );
                    intent.putExtra( SearchManager.QUERY, HashtagSpan.this.hashtag.toString() );
                    intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    intent.setComponent( new ComponentName( widget.getContext(), SitesActivity.class ) );
                    widget.getContext().startActivity( intent );
                    return true;
                }
            } );
            menu.show();
        }
    }
}
