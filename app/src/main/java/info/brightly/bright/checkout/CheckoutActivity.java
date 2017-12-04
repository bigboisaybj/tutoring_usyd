package info.brightly.bright.checkout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import info.brightly.bright.R;

/**
 * Created by bryanjordan on 19/10/17.
 */

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView card_recyclerView;
    private RecyclerView.LayoutManager card_layoutManger;
    private RecyclerView.Adapter card_adapter;
    private ArrayList<String> cardPurchasesDataSet;
    private ArrayList<String> card_totalPrice;
    private ArrayList<String> card_extras;
    private ArrayList<String> card_more;
    private ArrayList<String> merchants;


    public String [] productsForPurchase = {"Flat White", "Crossaint", "Bacon & Egg Roll"};
    public String [] totalPriceData = {"$5.40", "$19.50", "$16.30"};
    public String [] extrasData = {"Milk", "Sugar", "Milk & Sugar"};
    public String [] moreData = {"Hot", "Pick up", "Credit"};
    public String [] merchantsData = {"Reuben Hills", "Cafe Sydney", "Lansdowne Hotel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        cardPurchasesDataSet = new ArrayList<>();
        for (int i = 0; i < productsForPurchase.length; i++) {
            cardPurchasesDataSet.add(productsForPurchase[i]);
        }
        card_totalPrice = new ArrayList<>();
        for (int i = 0; i < totalPriceData.length; i++) {
            card_totalPrice.add(totalPriceData[i]);
        }
        card_extras = new ArrayList<>();
        for (int i = 0; i < extrasData.length; i++) {
            card_extras.add(extrasData[i]);
        }
        card_more = new ArrayList<>();
        for (int i = 0; i < moreData.length; i++) {
            card_more.add(moreData[i]);
        }
        merchants = new ArrayList<>();
        for (int i = 0; i < merchantsData.length; i++) {
            merchants.add(merchantsData[i]);
        }

        card_recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        card_layoutManger = new LinearLayoutManager(this);
        card_recyclerView.setLayoutManager(card_layoutManger);
        card_adapter = new Checkout_Card_Adapter(cardPurchasesDataSet, card_totalPrice, card_extras, card_more, merchants);
        card_recyclerView.setAdapter(card_adapter);
    }
}
