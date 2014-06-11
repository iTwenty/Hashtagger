package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by itwenty on 6/11/14.
 */
public class GPlusDateDeserializer implements JsonDeserializer<Date>
{
    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private SimpleDateFormat sdf;

    public GPlusDateDeserializer()
    {
        sdf = new SimpleDateFormat( FORMAT );
        sdf.setLenient( true );
    }

    @Override
    public Date deserialize( JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext ) throws JsonParseException
    {
        String dateStr = jsonElement.getAsJsonPrimitive().getAsString();
        try
        {
            return sdf.parse( dateStr );
        }
        catch ( ParseException e )
        {
            throw new RuntimeException( "Unexpected Date format from Google : " + dateStr );
        }
    }
}
