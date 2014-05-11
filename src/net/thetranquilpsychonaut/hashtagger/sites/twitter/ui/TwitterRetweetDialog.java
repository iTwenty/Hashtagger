package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterAction;

/**
 * Created by itwenty on 5/11/14.
 */
public class TwitterRetweetDialog extends DialogFragment
{
    public static final String TAG = "twitter_retweet_dialog";
    long retweetId;

    public static TwitterRetweetDialog newIntance( long retweetId )
    {
        TwitterRetweetDialog dialog = new TwitterRetweetDialog();
        Bundle args = new Bundle();
        args.putLong( "retweet_id", retweetId );
        dialog.setArguments( args );
        return dialog;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        this.retweetId = getArguments().getLong( "retweet_id" );
        AlertDialog dialog = new AlertDialog.Builder( getActivity() )
                .setTitle( "Retweet" )
                .setMessage( "Retweet this to your followers?" )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {

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

    @Override
    public void onStart()
    {
        super.onStart();
        AlertDialog ad = ( AlertDialog ) getDialog();
        ad.getButton( Dialog.BUTTON_POSITIVE ).setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                doRetweet();
            }
        } );
    }

    private void doRetweet()
    {
        final AlertDialog dialog = ( AlertDialog ) getDialog();
        new TwitterAction( new TwitterAction.TwitterActionListener()
        {
            @Override
            public void onPerforming()
            {
                dialog.getButton( Dialog.BUTTON_POSITIVE ).setEnabled( false );
                dialog.setMessage( "Retweeting..." );
            }

            @Override
            public void onPerformed()
            {
                Toast.makeText( HashtaggerApp.app, "Retweeted like a pro!", Toast.LENGTH_SHORT ).show();
                dismiss();
            }

            @Override
            public void onError()
            {
                dialog.getButton( Dialog.BUTTON_POSITIVE ).setEnabled( true );
                dialog.setMessage( "Failed to retweet. Try again?" );
            }
        } ).executeRetweetAction( retweetId );
    }
}
