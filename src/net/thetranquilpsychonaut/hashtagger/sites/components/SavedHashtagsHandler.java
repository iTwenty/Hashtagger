package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.content.Context;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by itwenty on 4/22/14.
 */
public class SavedHashtagsHandler
{
    private static final String FILENAME           = "saved";
    private static final int    MAX_SAVED_HASHTAGS = 10;

    private ArrayList<String> savedHashtags;

    public SavedHashtagsHandler() throws IOException
    {
        savedHashtags = new ArrayList<String>( MAX_SAVED_HASHTAGS );
        readSavedHashtags();
    }

    public void saveHashtag( String hashtag ) throws IOException
    {
        savedHashtags.add( hashtag );
        try
        {
            saveListToFile();
        }
        catch ( IOException e )
        {
            savedHashtags.remove( savedHashtags.size() - 1 );
            throw e;
        }
    }

    public void deleteHashtag( int position ) throws IOException
    {
        String temp = savedHashtags.get( position );
        savedHashtags.remove( position );
        try
        {
            saveListToFile();
        }
        catch ( IOException e )
        {
            savedHashtags.add( position, temp );
            throw e;
        }
    }

    public void readSavedHashtags() throws IOException
    {
        if ( !HashtaggerApp.app.getFileStreamPath( FILENAME ).exists() )
        {
            FileOutputStream fos = HashtaggerApp.app.openFileOutput( FILENAME, Context.MODE_PRIVATE );
            fos.close();
        }
        BufferedReader br = null;
        String hashtag;
        try
        {
            FileDescriptor fd = HashtaggerApp.app.openFileInput( FILENAME ).getFD();
            br = new BufferedReader( new FileReader( fd ) );
            while ( ( hashtag = br.readLine() ) != null )
            {
                savedHashtags.add( hashtag );
            }
        }
        finally
        {
            if ( null != br )
                br.close();
        }
    }

    public ArrayList<String> getSavedHashtags()
    {
        return savedHashtags;
    }

    private void saveListToFile() throws IOException
    {
        BufferedWriter bw = null;
        try
        {
            FileDescriptor fd = HashtaggerApp.app.openFileOutput( FILENAME, Context.MODE_PRIVATE ).getFD();
            bw = new BufferedWriter( new FileWriter( fd ) );
            for ( String hashtag : savedHashtags )
            {
                bw.write( hashtag );
                bw.newLine();
            }
        }
        finally
        {
            if ( null != bw )
                bw.close();
        }
    }
}
