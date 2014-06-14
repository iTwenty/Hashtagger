package net.thetranquilpsychonaut.hashtagger.sites.retrofit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by itwenty on 6/14/14.
 */
public class MapParams
{
    private Map<String, String> params;

    public MapParams()
    {
        params = new HashMap<String, String>();
    }

    public Map<String, String> getParams()
    {
        return this.params;
    }
}
