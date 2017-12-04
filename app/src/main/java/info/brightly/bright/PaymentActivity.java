package info.brightly.bright;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

/**
 * Retrieves customer credit card details, tokenizes it and
 * then sends it to the server for payment processing.
 *
 * @author Jimmy Chen (jchen)
 */
public class PaymentActivity extends AppCompatActivity {


    private CardInputWidget mCardInputWidget;
    private Stripe mStripe;
    private static final String TAG = "PaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

//        Bundle extras = getIntent().getExtras();
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

        Button mConfirmPaymentButton = (Button) findViewById(R.id.btn_purchase);
        mConfirmPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPurchase();
            }
        });

        mStripe = new Stripe(this, "pk_test_6pRNASCoBOKtIshFeQd4XMUh");
    }

    private void attemptPurchase() {
        Card card = mCardInputWidget.getCard();
        if (card == null) {
            displayError("Card Input Error");
            return;
        }

        if (!card.validateCard()) {
            displayError("Invalid Card");
            return;
        }

        mStripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to server
                        Log.d("PaymentActivity", "token: " + token.getId());
                        attemptCharge(token.getId());
                    }
                    public void onError(Exception error) {
                        Log.e("PaymentActivity", error.getMessage(), error);
                    }
                }
        );

        dismissKeyboard();
    }

    private void displayError(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void dismissKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);
    }

    private void attemptCharge(String token) {
        // this is how you call the endpoint from the client
        Log.d(TAG, "Charging with payment token: " + token);
        new EndpointsAsyncTask().execute(new Pair<Context, String>(this, token));
    }
}
