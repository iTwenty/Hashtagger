package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterAction;

/**
 * Created by itwenty on 5/11/14.
 */
public class TwitterReplyDialog extends DialogFragment implements TextWatcher
{
    public static final String TAG = "twitter_reply_dialog";
    String   inReplyToScreenName;
    long     inReplyToStatusId;
    EditText edtReplyText;
    TextView tvCharCounter;
    TextView tvReplyProgress;

    public static TwitterReplyDialog newInstance( String inReplyToScreenName, long inReplyToStatusId )
    {
        TwitterReplyDialog dialog = new TwitterReplyDialog();
        Bundle args = new Bundle();
        args.putString( "reply_to_screen_name", inReplyToScreenName );
        args.putLong( "reply_to_status_id", inReplyToStatusId );
        dialog.setArguments( args );
        return dialog;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        this.inReplyToScreenName = getArguments().getString( "reply_to_screen_name" );
        this.inReplyToStatusId = getArguments().getLong( "reply_to_status_id" );
        View layout = getActivity().getLayoutInflater().inflate( R.layout.dialog_twitter_reply, null );
        AlertDialog dialog = new AlertDialog.Builder( getActivity() )
                .setView( layout )
                .setIcon( android.R.drawable.ic_menu_edit )
                .setTitle( getResources().getString( R.string.str_reply ) )
                .setPositiveButton( getResources().getString( R.string.str_reply ), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        // Implemented in onStart() method
                    }
                } )
                .setNegativeButton( getResources().getString( R.string.str_cancel ), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        TwitterReplyDialog.this.getDialog().cancel();
                    }
                } )
                .create();

        edtReplyText = ( EditText ) layout.findViewById( R.id.edt_reply_text );
        tvCharCounter = ( TextView ) layout.findViewById( R.id.tv_char_counter );
        tvReplyProgress = ( TextView ) layout.findViewById( R.id.tv_reply_progress );
        edtReplyText.addTextChangedListener( this );
        edtReplyText.setText( "@" + this.inReplyToScreenName + " " );
        edtReplyText.setSelection( edtReplyText.getText().length() );
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
                doReply();
            }
        } );
    }

    public void doReply()
    {
        new TwitterAction( new TwitterAction.TwitterActionListener()
        {
            @Override
            public void onPerforming()
            {
                ( ( AlertDialog ) getDialog() ).getButton( DialogInterface.BUTTON_POSITIVE ).setEnabled( false );
                tvReplyProgress.setText( "Replying..." );
                tvReplyProgress.setVisibility( View.VISIBLE );
                tvReplyProgress.setTextColor( getResources().getColor( android.R.color.black ) );
            }

            @Override
            public void onPerformed()
            {
                Toast.makeText( HashtaggerApp.app, "Replied like a pro!", Toast.LENGTH_SHORT ).show();
                dismiss();
            }

            @Override
            public void onError()
            {
                ( ( AlertDialog ) getDialog() ).getButton( DialogInterface.BUTTON_POSITIVE ).setEnabled( true );
                tvReplyProgress.setText( "Failed to reply." );
                tvReplyProgress.setTextColor( getResources().getColor( android.R.color.holo_red_light ) );
            }
        } ).executeReplyAction( edtReplyText.getText().toString(), inReplyToStatusId );
    }

    @Override
    public void beforeTextChanged( CharSequence s, int start, int count, int after )
    {

    }

    @Override
    public void onTextChanged( CharSequence s, int start, int before, int count )
    {

    }

    @Override
    public void afterTextChanged( Editable s )
    {
        tvCharCounter.setText( s.length() + "/140" );
    }
}
