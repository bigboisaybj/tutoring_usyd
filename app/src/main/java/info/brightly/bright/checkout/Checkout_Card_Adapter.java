package info.brightly.bright.checkout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import info.brightly.bright.R;

/**
 * Created by bryanjordan on 19/10/17.
 */

public class Checkout_Card_Adapter extends RecyclerView.Adapter<Checkout_Card_Adapter.ViewHolder>{
    private ArrayList<String> titleOfPurchase;
    private ArrayList<String> priceOfPurchase;
    private ArrayList<String> extrasOfPurchase;
    private ArrayList<String> moreOfPurchase;
    private ArrayList<String> merchantOfPurchase;

    public Checkout_Card_Adapter(ArrayList<String> titleOfPurchase, ArrayList<String> priceOfPurchase, ArrayList<String> extrasOfPurchase, ArrayList<String> moreOfPurchase, ArrayList<String> merchantOfPurchase) {
        this.titleOfPurchase = titleOfPurchase;
        this.priceOfPurchase = priceOfPurchase;
        this.extrasOfPurchase = extrasOfPurchase;
        this.moreOfPurchase = moreOfPurchase;
        this.merchantOfPurchase = merchantOfPurchase;
    }

    public interface VenueAdapterInterface {
        void onVenueButtonClick(int position);
    }

    @Override
    public Checkout_Card_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.checkout_scrollable_card, viewGroup, false);
        Checkout_Card_Adapter.ViewHolder viewHolder = new Checkout_Card_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Checkout_Card_Adapter.ViewHolder viewHolder, final int position) {
        viewHolder.itemTitle.setText(titleOfPurchase.get(position));
        viewHolder.itemPrice.setText(priceOfPurchase.get(position));
        viewHolder.itemExtras.setText(extrasOfPurchase.get(position));
        viewHolder.itemMore.setText(moreOfPurchase.get(position));
        viewHolder.merchant.setText(merchantOfPurchase.get(position));
    }

    @Override
    public int getItemCount() {
        return titleOfPurchase.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton itemImage;
        public TextView itemTitle;
        public TextView itemPrice;
        public TextView itemExtras;
        public TextView itemMore;
        public TextView merchant;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            itemPrice = (TextView) itemView.findViewById(R.id.priceContainer);
            itemExtras = (TextView) itemView.findViewById(R.id.extraContainer);
            itemMore = (TextView) itemView.findViewById(R.id.otherContainer);
            merchant = (TextView) itemView.findViewById(R.id.merchantPurchase);
        }
    }
}
