package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterActions;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

/**
 * Created by itwenty on 5/11/14.
 */
public class TwitterRetweetDialog extends DialogFragment
{
    public static final String TAG = "twitter_retweet_dialog";
    Status status;

    public static TwitterRetweetDialog newInstance( Status status )
    {
        TwitterRetweetDialog dialog = new TwitterRetweetDialog();
        Bundle args = new Bundle();
        args.putSerializable( "status", status );
        dialog.setArguments( args );
        return dialog;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        this.status = ( Status ) getArguments().getSerializable( "status" );
        AlertDialog dialog = new AlertDialog.Builder( getActivity() )
                .setTitle( "Retweet" )
                .setMessage( getResources().getString( R.string.str_retweet_confirm ) )
                .setPositiveButton( getResources().getString( R.string.str_yes ), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        doRetweet();
                    }
                } )
                .setNegativeButton( getResources().getString( R.string.str_no ), new DialogInterface.OnClickListener()
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
        TwitterActions.executeRetweetAction( status );
    }
}
