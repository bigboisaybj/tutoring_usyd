package info.brightly.bright.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import info.brightly.bright.Data;
import info.brightly.bright.DishContext;
import info.brightly.bright.Order;
import info.brightly.bright.R;
import info.brightly.bright.StackSwipeAdapter;
import info.brightly.bright.TabsMode;
import info.brightly.bright.checkout.CheckoutActivity;

import static info.brightly.bright.DishContext.MAIN;
import static info.brightly.bright.Order.getFoodCategory;


/**
 * Class that handles the bottom bar.
 *
 * @author Justin Ng (jn3141)
 */

public class BottomBarController {

    private Context context;
    private HashMap<String, ImageView> tabs;
    private ImageView bottomBar;
    private TabsMode tabsMode;
    private long startClickTime;
    private ArrayList<View> reservedMiniCards;
    private int reservedMiniCardsIndex;
    private ArrayList<View> reservedAddCards;
    private int reservedAddCardsIndex;
    private static final int MAX_CLICK_DURATION = 200;
    private StackSwipeAdapter swipeAdapterMini;
    private ViewPager viewPagerMini;

    public BottomBarController(final Context context, final HashMap<String, ImageView> tabs, final ImageView bottomBar,
                               final StackSwipeAdapter swipeAdapterMini, final ViewPager viewPagerMini) {
        this.context = context;
        this.tabs = tabs;
        this.bottomBar = bottomBar;
        this.tabsMode = TabsMode.CLOSED;
        this.startClickTime = -1;
        this.reservedMiniCards = null;
        this.reservedMiniCardsIndex = -1;
        this.swipeAdapterMini = swipeAdapterMini;
        this.viewPagerMini = viewPagerMini;

        // Reset the colours for all of them
        for (HashMap.Entry<String, ImageView> pair : tabs.entrySet()) {
            ImageView curr = pair.getValue();
            curr.setColorFilter(curr.getContext().getColor(R.color.colorSubText));
        }

        bottomBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Get the x and y coordinates of the touch event.
                float x = event.getX();

                // Divide the bottom bar width by 5 to get fifths.
                int fifth = bottomBar.getWidth() / 5;

                // Get the type of action the MotionEvent is.
                int action = event.getAction();

                if (action == android.view.MotionEvent.ACTION_DOWN) {
                    // Set the time at which the initial depress is done.
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                } else if (action == MotionEvent.ACTION_UP && startClickTime != -1) {
                    // Calculate the time difference between the initial depress and the release.
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;

                    // Set the start click time to -1 / null.
                    startClickTime = -1;

                    // If it's short enough of a time period / is a click.
                    if (clickDuration < MAX_CLICK_DURATION) {
                        if (((x >= 0 && x <= fifth) && tabsMode == TabsMode.SETTINGS) ||
                            ((x >= fifth && x <= 2 * fifth) && tabsMode == TabsMode.SEARCH) ||
                            ((x >= 2 * fifth && x <= 3 * fifth) && tabsMode == TabsMode.ADD) ||
                            ((x >= 3 * fifth && x <= 4 * fifth) && tabsMode == TabsMode.CATEGORIES) ||
                            ((x >= 4 * fifth && x <= 5 * fifth) && tabsMode == TabsMode.CONFIRM)) {
                            // Reset the bottom bar
                            resetBottomBar();
                            System.out.println("Reset the cards???");
                        } else {
//                            System.out.println(tabsMode);
//                            System.out.println(x);
//                            System.out.println(fifth);
                            // Open the relevant one.
                            // If it's in a closed state, then save the state prior to opening the tab.
                            if (tabsMode == TabsMode.CLOSED) {
                                reservedMiniCards = (ArrayList<View>) swipeAdapterMini.getViews().clone();
                                reservedMiniCardsIndex = viewPagerMini.getCurrentItem();
                            }

                            // Reset the colours for all of them.
                            for (HashMap.Entry<String, ImageView> pair : tabs.entrySet()) {
                                ImageView curr = pair.getValue();
                                curr.setColorFilter(curr.getContext().getColor(R.color.colorSubText));
                            }

                            // Open category depending on which section it was.
                            if (x >= 0 && x <= fifth) {
                                tabsMode = TabsMode.SETTINGS;
                                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
                                ImageView curr = tabs.get("settings");
                                curr.setColorFilter(curr.getContext().getColor(R.color.colorPrimary));
                            } else if (x > fifth && x <= 2 * fifth) {
                                tabsMode = TabsMode.SEARCH;
                                Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show();
                                ImageView curr = tabs.get("search");
                                curr.setColorFilter(curr.getContext().getColor(R.color.colorPrimary));
                            } else if (x > 2 * fifth && x <= 3 * fifth) {
                                // Do something with categories
                                loadAdd();
                                tabsMode = TabsMode.ADD;
                                Toast.makeText(context, "Add", Toast.LENGTH_SHORT).show();
                                ImageView curr = tabs.get("add");
                                curr.setColorFilter(curr.getContext().getColor(R.color.colorPrimary));
                            } else if (x > 3 * fifth && x <= 4 * fifth) {
                                // Do something with categories
                                loadCategories();
                                tabsMode = TabsMode.CATEGORIES;
                                Toast.makeText(context, "Categories", Toast.LENGTH_SHORT).show();
                                ImageView curr = tabs.get("categories");
                                curr.setColorFilter(curr.getContext().getColor(R.color.colorPrimary));
                            } else if (x > 4 * fifth && x <= 5 * fifth) {
                                tabsMode = TabsMode.CONFIRM;
                                Toast.makeText(context, "Confirm", Toast.LENGTH_SHORT).show();
                                ImageView curr = tabs.get("confirm");
                                curr.setColorFilter(curr.getContext().getColor(R.color.colorPrimary));
                                Log.i("TAG", "confirm clicked");
                                //startActivity(new Intent(Order.class, CheckoutActivity.class));
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * Resets the bottom bar, changing the colours back to unselected,
     */
    public void resetBottomBar() {
        // Reset the bar for cards to be loaded
        if (reservedMiniCards != null) {
            swipeAdapterMini.removeAllViews(viewPagerMini);
            swipeAdapterMini.setViews(reservedMiniCards);
            swipeAdapterMini.notifyDataSetChanged();
            viewPagerMini.setAdapter(swipeAdapterMini);
            viewPagerMini.setCurrentItem(reservedMiniCardsIndex);
            reservedMiniCards = null;
            reservedMiniCardsIndex = -1;
        }

        tabsMode = TabsMode.CLOSED;

        for (HashMap.Entry<String, ImageView> pair : tabs.entrySet()) {
            ImageView curr = pair.getValue();
            curr.setColorFilter(curr.getContext().getColor(R.color.colorSubText));
        }
    }

    /**
     * Loads the add section
     */
    private void loadAdd() {
        // Get the food item the add-ons need to be loaded for
        final View currentView = swipeAdapterMini.getViews().get(viewPagerMini.getCurrentItem());
        String cardText = ((TextView)currentView.findViewById(R.id.dishMini)).getText().toString();

        // Remove all the cards in the mini cards
        swipeAdapterMini.removeAllViews(viewPagerMini);

        // Get the add-on arrays; currently hardcoded, change it once we know where to hook it
        final DishContext dishContext = getFoodCategory(cardText);
        String[] addOns;
        String[] addOnsPrices;

        switch (dishContext) {
            case MAIN:
                addOns = Data.mainsAddOns;
                addOnsPrices = Data.mainsAddOnsPrices;
                break;
            case DRINK:
                addOns = Data.drinksAddOns;
                addOnsPrices = Data.drinksAddOnsPrices;
                break;
            case SIDE:
                addOns = Data.sideAddOns;
                addOnsPrices = Data.sideAddOnsPrices;
                break;
            case SHARE:
                addOns = Data.shareAddOns;
                addOnsPrices = Data.shareAddOnsPrices;
                break;
            case DESSERT:
                addOns = Data.dessertsAddOns;
                addOnsPrices = Data.dessertsAddOnsPrices;
                break;
            default:
                return;
        }

        // Go iterate through each of the arrays, adding the cards
        for (int i = 0; i < addOns.length; i++) {
            // Inflate the dish card.
            // Grab references to each of the elements inside of the inflated layout.
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
            ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
            TextView textDish = (TextView) card.findViewById(R.id.dishMini);
            TextView textPrice = (TextView) card.findViewById(R.id.priceMini);

            // Set each of the elements of the card with items from the array.
            textDish.setText(addOns[i]);
            textPrice.setText(addOnsPrices[i]);

            // Make it so that clicking the card makes it show the relevant add dialog.
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reservedAddCards = (ArrayList<View>) swipeAdapterMini.getViews().clone();
                    reservedAddCardsIndex = viewPagerMini.getCurrentItem();
                    loadAddOnAmountDialog();
                }
            });

            // Place the cards in the ViewPager
            swipeAdapterMini.addView(card);
            swipeAdapterMini.notifyDataSetChanged();
        }

        // Add the expand card
        // Inflate the dish card.
        // Grab references to each of the elements inside of the inflated layout.
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
        ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
        TextView textDish = (TextView) card.findViewById(R.id.dishMini);
        TextView textPrice = (TextView) card.findViewById(R.id.priceMini);

        // Set each of the elements of the card with items from the array.
        textDish.setText("See all add-ons");
        textPrice.setText("");

        // Make it so that clicking the card makes it show the relevant add dialog.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservedAddCards = (ArrayList<View>) swipeAdapterMini.getViews().clone();
                reservedAddCardsIndex = viewPagerMini.getCurrentItem();
                loadAddOnExtra(dishContext);
            }
        });

        // Place the cards in the ViewPager
        swipeAdapterMini.addView(card);
        swipeAdapterMini.notifyDataSetChanged();

        // refreshes the viewPagerMini
        viewPagerMini.setAdapter(swipeAdapterMini);
        viewPagerMini.setCurrentItem(0);
    }

    private void loadAddOnExtra(DishContext dishContext) {
        int position = viewPagerMini.getCurrentItem();

        // Remove the expand card
        swipeAdapterMini.removeView(viewPagerMini, swipeAdapterMini.getCount()-1);
        swipeAdapterMini.notifyDataSetChanged();

        // Get the add-on arrays; currently hardcoded, change it once we know where to hook it
        String[] addOns;
        String[] addOnsPrices;

        switch (dishContext) {
            case MAIN:
                addOns = Data.mainsAddOnsExtra;
                addOnsPrices = Data.mainsAddOnsPricesExtra;
                break;
            case DRINK:
                addOns = Data.drinksAddOnsExtra;
                addOnsPrices = Data.drinksAddOnsPricesExtra;
                break;
            case SIDE:
                addOns = Data.sideAddOnsExtra;
                addOnsPrices = Data.sideAddOnsPricesExtra;
                break;
            case SHARE:
                addOns = Data.shareAddOnsExtra;
                addOnsPrices = Data.shareAddOnsPricesExtra;
                break;
            case DESSERT:
                addOns = Data.dessertsAddOnsExtra;
                addOnsPrices = Data.dessertsAddOnsPricesExtra;
                break;
            default:
                return;
        }

        // Go iterate through each of the arrays, adding the cards
        for (int i = 0; i < addOns.length; i++) {
            // Inflate the dish card.
            // Grab references to each of the elements inside of the inflated layout.
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
            ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
            TextView textDish = (TextView) card.findViewById(R.id.dishMini);
            TextView textPrice = (TextView) card.findViewById(R.id.priceMini);

            // Set each of the elements of the card with items from the array.
            textDish.setText(addOns[i]);
            textPrice.setText(addOnsPrices[i]);

            // Make it so that clicking the card makes it show the relevant add dialog.
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reservedAddCards = (ArrayList<View>) swipeAdapterMini.getViews().clone();
                    reservedAddCardsIndex = viewPagerMini.getCurrentItem();
                    loadAddOnAmountDialog();
                }
            });

            // Place the cards in the ViewPager
            swipeAdapterMini.addView(card);
            swipeAdapterMini.notifyDataSetChanged();
        }

        // refreshes the viewPagerMini
        viewPagerMini.setAdapter(swipeAdapterMini);
        viewPagerMini.setCurrentItem(position);
    }

    /**
     * Loads the dialog for choosing the amount of an add-on
     */
    private void loadAddOnAmountDialog() {
        // Get the add-on item that the dialog needs to be loaded for
        final View currentCard = swipeAdapterMini.getViews().get(viewPagerMini.getCurrentItem());

        // Remove all the cards in the mini cards
        swipeAdapterMini.removeAllViews(viewPagerMini);

        // Add the minus card
        // Grab references to each of the elements inside of the inflated layout.
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
        ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
        TextView textDish = (TextView) card.findViewById(R.id.dishMini);
        TextView textPrice = (TextView) card.findViewById(R.id.priceMini);

        // Set each of the elements of the card with items from the array.
        textDish.setText("-");
        textPrice.setText("");

        // Make it so that clicking the card makes it decrement
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just make a toast for now; hook it later
                Toast.makeText(context, "Minus", Toast.LENGTH_SHORT).show();
            }
        });

        // Place the minus card in the ViewPager
        swipeAdapterMini.addView(card);
        swipeAdapterMini.notifyDataSetChanged();

        // Add the card that is being adjusted
        imageView = (ImageView) currentCard.findViewById(R.id.foodPictureMini);
        // Make it so that clicking the card makes it show the relevant add dialog.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just make a toast for now; hook it later
                Toast.makeText(context, "Finished adjusting amount", Toast.LENGTH_SHORT).show();
                swipeAdapterMini.setViews(reservedAddCards);
                swipeAdapterMini.notifyDataSetChanged();
                viewPagerMini.setAdapter(swipeAdapterMini);
                viewPagerMini.setCurrentItem(reservedAddCardsIndex);
                reservedAddCards = null;
                reservedAddCardsIndex = -1;
            }
        });

        swipeAdapterMini.addView(currentCard);
        swipeAdapterMini.notifyDataSetChanged();

        // Add the plus card
        // Grab references to each of the elements inside of the inflated layout.
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
        imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
        textDish = (TextView) card.findViewById(R.id.dishMini);
        textPrice = (TextView) card.findViewById(R.id.priceMini);

        // Set each of the elements of the card with items from the array.
        textDish.setText("+");
        textPrice.setText("");

        // Make it so that clicking the card makes it increment
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just make a toast for now; hook it later
                Toast.makeText(context, "Plus", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the plus card
        swipeAdapterMini.addView(card);
        swipeAdapterMini.notifyDataSetChanged();

        // refreshes the viewPagerMini
        viewPagerMini.setAdapter(swipeAdapterMini);
        viewPagerMini.setCurrentItem(1);
    }

    /**
     * Loads the category picking section
     */
    private void loadCategories() {
        swipeAdapterMini.removeAllViews(viewPagerMini);
        for (int i = 0; i < Data.foodNames.length; i++) {
            // Inflate the dish card.
            // Grab references to each of the elements inside of the inflated layout.
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
            ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
            TextView textDish = (TextView) card.findViewById(R.id.dishMini);
            TextView textPrice = (TextView) card.findViewById(R.id.priceMini);

            // Set each of the elements of the card with items from the array.
            imageView.setImageResource(Data.foodPictures[i]);
            textDish.setText(Data.foodNames[i]);
            textPrice.setText(Data.foodPrices[i]);

            final int j = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // This just acts as a placeholder right now, and are hardcoded; once we have
                    //   the proper categories, can put them in here.
                    final View currentView = swipeAdapterMini.getViews().get(viewPagerMini.getCurrentItem());
                    String cardText = ((TextView)currentView.findViewById(R.id.dishMini)).getText().toString();
                    DishContext dishContext = getFoodCategory(cardText);
                    resetBottomBar();
                    jumpToCategory(dishContext);
                }
            });

            // Place the cards in the ViewPager
            swipeAdapterMini.addView(card);
            swipeAdapterMini.notifyDataSetChanged();
        }

        // refreshes the viewPagerMini
        viewPagerMini.setAdapter(swipeAdapterMini);
        int location = 0;
        viewPagerMini.setCurrentItem(location);
    }

    /**
     * Jumps to the category based on the selection
     */
    private void jumpToCategory(DishContext dC) {
        String[] food;
        String[] prices;
        int[] pictures;

        switch (dC) {
            case STARTER:
                food = Data.starter;
                prices = Data.starterPrices;
                pictures = Data.startPictures;
                break;
            case MAIN:
                food = Data.mains;
                prices = Data.mainsPrices;
                pictures = Data.mainsPictures;
                break;
            case DRINK:
                food = Data.drinks;
                prices = Data.drinksPrices;
                pictures = Data.drinksPictures;
                break;
            case SIDE:
                food = Data.side;
                prices = Data.sidePrices;
                pictures = Data.sidePictures;
                break;
            case SHARE:
                food = Data.share;
                prices = Data.sharePrices;
                pictures = Data.sharePictures;
                break;
            case DESSERT:
                food = Data.desserts;
                prices = Data.dessertsPrices;
                pictures = Data.dessertPictures;
                break;
            default:
                food = Data.foodNames;
                prices = Data.foodPrices;
                pictures = Data.foodPictures;
        }

        swipeAdapterMini.removeAllViews(viewPagerMini);
        for (int j = 0; j < food.length; j++) {
            // Inflate the dish card.
            // Grab references to each of the elements inside of the inflated layout.
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
            final ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
            final TextView textDish = (TextView) card.findViewById(R.id.dishMini);
            final TextView textPrice = (TextView) card.findViewById(R.id.priceMini);

            // Set each of the elements of the card with items from the array.
            imageView.setImageResource(pictures[j]);
            textDish.setText(food[j]);
            textPrice.setText(prices[j]);

            // Place the cards in the ViewPager
            swipeAdapterMini.addView(card);
        }

        // refreshes the viewPagerMini
        viewPagerMini.setAdapter(swipeAdapterMini);
        viewPagerMini.setCurrentItem(0);
    }
}

