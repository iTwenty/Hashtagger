package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;

/**
 * Created by itwenty on 4/2/14.
 */
public abstract class SitesService extends MyIntentService implements SearchActionName, LoginActionName
{
    private static final String SITES_SERVICE_NAME = HashtaggerApp.NAMESPACE + "sites_service_name";

    public SitesService()
    {
        super( SITES_SERVICE_NAME );
    }

    @Override
    protected void onHandleIntent( Intent intent )
    {
        ActionType actionType = ( ActionType ) intent.getSerializableExtra( ActionType.ACTION_TYPE_KEY );
        Intent resultIntent = new Intent();
        if ( actionType == ActionType.SEARCH )
        {
            resultIntent = doSearch( intent );
            resultIntent.setAction( getSearchActionName() );
        }
        else if ( actionType == ActionType.AUTH )
        {
            resultIntent = doAuth( intent );
            resultIntent.setAction( getLoginActionName() );
        }
        resultIntent.addCategory( Intent.CATEGORY_DEFAULT );
        sendBroadcast( resultIntent );
    }

    protected abstract Intent doSearch( Intent intent );

    protected abstract Intent doAuth( Intent intent );

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
