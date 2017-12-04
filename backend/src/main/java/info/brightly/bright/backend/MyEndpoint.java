/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package info.brightly.bright.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.bright.brightly.info",
                ownerName = "backend.bright.brightly.info",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String token) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {

        // Set your secret key: remember to change this to your live secret key in production
        // See your keys here: https://dashboard.stripe.com/account/apikeys
        Stripe.apiKey = "sk_test_BQokikJOvBiI2HlWgH4olfQ2";

        // Charge the user's card:
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", 100);
        params.put("currency", "aud");
        params.put("description", "Example charge");
        params.put("source", token);

        Charge charge = Charge.create(params);
        String data = charge.getAmount().toString();
        MyBean response = new MyBean();
        response.setData(data);

        return response;
    }

}
