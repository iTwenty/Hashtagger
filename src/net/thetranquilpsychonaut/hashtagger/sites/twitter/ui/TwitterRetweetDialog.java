package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterAction;

/**
 * Created by itwenty on 5/11/14.
 */
public class TwitterRetweetDialog extends DialogFragment
{
    public static final String TAG = "twitter_retweet_dialog";
    long retweetId;
    int  position;

    public static TwitterRetweetDialog newInstance( long retweetId, int position )
    {
        TwitterRetweetDialog dialog = new TwitterRetweetDialog();
        Bundle args = new Bundle();
        args.putLong( "retweet_id", retweetId );
        args.putInt( "position", position );
        dialog.setArguments( args );
        return dialog;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        this.retweetId = getArguments().getLong( "retweet_id" );
        this.position = getArguments().getInt( "position" );
        AlertDialog dialog = new AlertDialog.Builder( getActivity() )
                .setTitle( "Retweet" )
                .setMessage( "Retweet this to your followers?" )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        doRetweet();
                    }
                } )
                .setNegativeButton( "No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        TwitterRetweetDialog.this.getDialog().cancel();
                    }
                } )
                .create();
        return dialog;
    }

    private void doRetweet()
    {
        new TwitterAction().executeRetweetAction( this.retweetId, this.position );
    }
}
