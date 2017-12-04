package info.brightly.bright;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.bartoszlipinski.flippablestackview.StackPageTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import info.brightly.bright.controllers.BottomBarController;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter;

import static info.brightly.bright.DishContext.MAIN;


public class Order extends AppCompatActivity {

    // So we know what category dish belongs in.
    public static DishContext dishContext = DishContext.NONE;
    static int cardPosition = 0;

    // Bottom bar variables
    private HashMap<String, ImageView> tabs;
    private BottomBarController bottomBarController;
    private TabsMode tabsMode;
    private long startClickTime;
    private ArrayList<View> reservedMiniCards;
    private static final int MAX_CLICK_DURATION = 200;

    // ViewPagers, stacks, and the adapters
    private ViewPager viewPagerTop, viewPagerMini;
    private StackSwipeAdapter swipeAdapterTop, swipeAdapterMini;
    private FlippableStackView stack;

    // Deprecated variables
//    TextView slidingTitle;
//    SlidingUpPanelLayout slidingPanel;
//    PanelScrollView buttons;
//    SliderMode sliderMode;
//    ImageView slidingBar;

    /**
     * Gets the location in the array for the item being searched for.
     *
     * @param word the name of the item being searched for.
     * @return int the location index for the item.
     */
    public static int getLocation(String word) {
        switch (dishContext) {
            case DRINK:
                for (int i = 0; i < Data.drinks.length; i++) {
                    if (Data.drinks[i].equals(word)) {
                        return i;
                    }
                }
            case MAIN:
                for (int i = 0; i < Data.mains.length; i++) {
                    if (Data.mains[i].equals(word)) {
                        return i;
                    }
                }
            case DESSERT:
                for (int i = 0; i < Data.desserts.length; i++) {
                    if (Data.desserts[i].equals(word)) {
                        return i;
                    }
                }
        }
        return 0;
    }

    // Allows us to detect what category a dish is
    public static DishContext getFoodCategory(String dish) {
        ArrayList<String> mainList = new ArrayList<String>(Arrays.asList(Data.mains));
        ArrayList<String> drinkList = new ArrayList<String>(Arrays.asList(Data.drinks));
        ArrayList<String> dessertList = new ArrayList<String>(Arrays.asList(Data.desserts));
        ArrayList<String> sideList = new ArrayList<String>(Arrays.asList(Data.side));
        ArrayList<String> shareList = new ArrayList<String>(Arrays.asList(Data.share));

        if (mainList.contains(dish)) {
            return DishContext.MAIN;
        } else if (drinkList.contains(dish)) {
            return DishContext.DRINK;
        } else if (dessertList.contains(dish)) {
            return DishContext.DESSERT;
        } else if (sideList.contains(dish)) {
            return DishContext.SIDE;
        } else if (shareList.contains(dish)) {
            return DishContext.SHARE;
        } else {
            return DishContext.STARTER;
        }
    }

    /**
     * Synchronises the mini cards to the top cards
     */
    private void syncMiniCards() {
        ArrayList<View> views = swipeAdapterTop.getViews();
        final View currentView = views.get(viewPagerTop.getCurrentItem());
        String cardText = ((TextView) currentView.findViewById(R.id.dish)).getText().toString();
        // So we can sync mini cards accordingly
        dishContext = getFoodCategory(cardText);
        String[] food;
        String[] prices;
        int[] pictures;

        switch (dishContext) {
            case STARTER:
                food = Data.starter;
                prices = Data.starterPrices;
                pictures = Data.startPictures;
                dishContext = DishContext.STARTER;
                break;
            case MAIN:
                food = Data.mains;
                prices = Data.mainsPrices;
                pictures = Data.mainsPictures;
                dishContext = MAIN;
                break;
            case DRINK:
                food = Data.drinks;
                prices = Data.drinksPrices;
                pictures = Data.drinksPictures;
                dishContext = DishContext.DRINK;
                break;
            case SIDE:
                food = Data.side;
                prices = Data.sidePrices;
                pictures = Data.sidePictures;
                dishContext = DishContext.SIDE;
                break;
            case SHARE:
                food = Data.share;
                prices = Data.sharePrices;
                pictures = Data.sharePictures;
                dishContext = DishContext.SHARE;
                break;
            case DESSERT:
                food = Data.desserts;
                prices = Data.dessertsPrices;
                pictures = Data.dessertPictures;
                dishContext = DishContext.DESSERT;
                break;
            default:
                food = Data.foodNames;
                prices = Data.foodPrices;
                pictures = Data.foodPictures;
                dishContext = DishContext.NONE;
        }

        swipeAdapterMini.removeAllViews(viewPagerMini);
        for (int j = 0; j < food.length; j++) {
            // Inflate the dish card.
            // Grab references to each of the elements inside of the inflated layout.
            LayoutInflater layoutInflater = getLayoutInflater();
            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
            final ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
            final TextView textDish = (TextView) card.findViewById(R.id.dishMini);
            final TextView textPrice = (TextView) card.findViewById(R.id.priceMini);

            // Set each of the elements of the card with items from the array.
            imageView.setImageResource(pictures[j]);
            textDish.setText(food[j]);
            textPrice.setText(prices[j]);

//            // Set each image in minicards to be clickable and change corresponding big card
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TextView cardDish = ((TextView) currentView.findViewById(R.id.dish));
//                    TextView cardPrice = ((TextView) currentView.findViewById(R.id.price));
//                    ImageView cardImage = ((ImageView) currentView.findViewById(R.id.foodPicture));
//                    cardDish.setText(textDish.getText());
//                    cardPrice.setText(textPrice.getText());
//                    cardImage.setImageDrawable(imageView.getDrawable());
//                }
//            });
            // Place the cards in the ViewPager
            swipeAdapterMini.addView(card);
        }

        // refreshes the viewPagerMini
        viewPagerMini.setAdapter(swipeAdapterMini);
        int location = 0;
        location = getLocation(cardText);
        viewPagerMini.setCurrentItem(location);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialising the whole View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Set up mini cards
        viewPagerMini = (ViewPager) findViewById(R.id.mini_card_swipe_pager);
        viewPagerMini.setClipToPadding(false);
        viewPagerMini.setOffscreenPageLimit(4);
        int marginMini = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108 * 2, getResources().getDisplayMetrics());
        viewPagerMini.setPageMargin(-marginMini);

        // Give the mini cards the overscroll bounce effect
        new HorizontalOverScrollBounceEffectDecorator(new IOverScrollDecoratorAdapter() {
            @Override
            public View getView() {
                return viewPagerMini;
            }

            @Override
            public boolean isInAbsoluteStart() {
                return !viewPagerMini.canScrollHorizontally(-1);
            }

            @Override
            public boolean isInAbsoluteEnd() {
                return !viewPagerMini.canScrollHorizontally(1);
            }
        });

        // Populate the mini ViewPager with cards
        swipeAdapterMini = new StackSwipeAdapter();
        viewPagerMini.setAdapter(swipeAdapterMini);
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < Data.restaurantNames.length; i++) {
            // Inflate the dish card.
            // Grab references to each of the elements inside of the inflated layout.
            layoutInflater = getLayoutInflater();
            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
            ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
            TextView textDish = (TextView) card.findViewById(R.id.dishMini);
            TextView textPrice = (TextView) card.findViewById(R.id.priceMini);

            // Set each of the elements of the card with items from the array.
            imageView.setImageResource(Data.foodPictures[i]);
            textDish.setText(Data.foodNames[i]);
            textPrice.setText(Data.foodPrices[i]);

            // Place the cards in the ViewPager
            swipeAdapterMini.addView(card);
            swipeAdapterMini.notifyDataSetChanged();
        }

        // Set up top cards
        viewPagerTop = (ViewPager) findViewById(R.id.order_swipe_pager);
        viewPagerTop.setClipToPadding(false);
        viewPagerTop.setOffscreenPageLimit(4);
        int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32 * 2, getResources().getDisplayMetrics());
        viewPagerTop.setPageMargin(-marginTop);

        // Give the top cards the overscroll bounce effect
        new HorizontalOverScrollBounceEffectDecorator(new IOverScrollDecoratorAdapter() {
            @Override
            public View getView() {
                return viewPagerTop;
            }

            @Override
            public boolean isInAbsoluteStart() {
                // canScrollUp() is an example of a method you must implement
                return !viewPagerTop.canScrollHorizontally(-1);
            }

            @Override
            public boolean isInAbsoluteEnd() {
                // canScrollDown() is an example of a method you must implement
                return !viewPagerTop.canScrollHorizontally(1);
            }
        });

        // Populate the top ViewPager with stacks
        swipeAdapterTop = new StackSwipeAdapter();
        viewPagerTop.setAdapter(swipeAdapterTop);

        for (int i = 0; i < Data.restaurantNames.length; i++) {

            // Inflate the card stack, and set up it's adapter
            final FlippableStackView stack = (FlippableStackView) layoutInflater.inflate(R.layout.card_stack, null);
            stack.initStack(1, StackPageTransformer.Orientation.VERTICAL, 0.85f, 0.7f, 1.0f, StackPageTransformer.Gravity.CENTER);
            StackSwipeAdapter stackSwipeAdapter = new StackSwipeAdapter();
            stack.setAdapter(stackSwipeAdapter);
            stack.setOverScrollMode(View.OVER_SCROLL_NEVER);

            // Inflate the dish card.
            // Grab references to each of the elements inside of the inflated layout.
            layoutInflater = getLayoutInflater();
            ConstraintLayout card1 = (ConstraintLayout) layoutInflater.inflate(R.layout.card_food, null);
            ImageView imageView = (ImageView) card1.findViewById(R.id.foodPicture);
            TextView textDish = (TextView) card1.findViewById(R.id.dish);
            TextView textPrice = (TextView) card1.findViewById(R.id.price);

            // Set each of the elements of the card with items from the array.
            imageView.setImageResource(Data.foodPictures[i]);
            textDish.setText(Data.foodNames[i]);
            textPrice.setText(Data.foodPrices[i]);

            // Inflate the restaurant card.
            // Grab references to each of the elements inside of the inflated layout.
            layoutInflater = getLayoutInflater();
            ConstraintLayout card2 = (ConstraintLayout) layoutInflater.inflate(R.layout.card_rest, null);
            imageView = (ImageView) card2.findViewById(R.id.restaurantPicture);
            TextView textView = (TextView) card2.findViewById(R.id.restaurantName);

            // Set each of the elements of the card with items from the array.
            imageView.setImageResource(Data.restaurantPictures[i]);
            textView.setText(Data.restaurantNames[i]);

            // Place the cards in the stack
            stackSwipeAdapter.addView(card2);
            stackSwipeAdapter.addView(card1);
            stackSwipeAdapter.notifyDataSetChanged();
            stack.setCurrentItem(1, true);

            // Place the stack in the top ViewPager
            swipeAdapterTop.addView(stack);
            swipeAdapterTop.notifyDataSetChanged();
        }

        // Set the page change listener for the top dishes cards
        viewPagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            /**
             * Makes the cards that are off the screen to reset back to default food view.
             * Additionally, synchronises the mini cards with the top cards
             *
             * @param position the position in the ViewPager it was scrolled to.
             */
            @Override
            public void onPageSelected(int position) {

                // Reset the cards offscreen.
                int i = 0;
                ArrayList<View> views = swipeAdapterTop.getViews();
                for (View view : views) {
                    if (i < position - 1 || i > position + 1) {
                        ((FlippableStackView) view).setCurrentItem(1, true);
                    }
                    i++;
                }

                // Synchronise the mini cards to the top cards.
                viewPagerMini.animate().alpha(0.0f).setDuration(200);
                viewPagerMini.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        syncMiniCards();
                        viewPagerMini.animate().alpha(1.0f).setDuration(200);
                    }
                }, 200);

                // Resets the bottom bar
                bottomBarController.resetBottomBar();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Set up bottom bar
        tabs = new HashMap<String, ImageView>();
        tabs.put("settings", (ImageView) findViewById(R.id.tab_settings));
        tabs.put("search", (ImageView) findViewById(R.id.tab_search));
        tabs.put("add", (ImageView) findViewById(R.id.tab_add));
        tabs.put("categories", (ImageView) findViewById(R.id.tab_categories));
        tabs.put("confirm", (ImageView) findViewById(R.id.tab_confirm));

        ImageView bottomBar = (ImageView)findViewById(R.id.bottomBar);
        bottomBarController = new BottomBarController(getBaseContext(), tabs, bottomBar,
                swipeAdapterMini, viewPagerMini);

//        // Get the references to the relevant Views in the bottom sliding bar
//        slidingBar = (ImageView) findViewById(R.id.slidingBar);
//        slidingTitle = (TextView) findViewById(R.id.slidingTitle);
//        slidingPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel);
//        buttons = (PanelScrollView) findViewById(R.id.buttons);

//        // Initialise the state of the bottom sliding bar
//        slidingPanel.setTouchEnabled(false);
//        sliderMode = SliderMode.CLOSED;
//        slidingPanel.setAnchorPoint(0.67f);
//        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

//        // Set the panel's slide listener for the bottom drawer.
//        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
//
//            /**
//             * Just returns; does nothing.
//             * @param panel       the sliding panel being worked on.
//             * @param slideOffset how far it was slid; goes from 0 to 1.
//             */
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//                return;
//            }
//
//            /**
//             * Adjusts the scrolling of the buttons and whether the sliding panel is touch enabled
//             * depending on the state.
//             * @param panel         the sliding panel being worked on.
//             * @param previousState the old state pre-change.
//             * @param newState      the new state post-change.
//             */
//            @Override
//            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//                if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
//                    buttons.smoothScrollTo(0, 0);
//                    buttons.setEnableScrolling(false);
//                    slidingPanel.setTouchEnabled(true);
//                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                    buttons.setEnableScrolling(false);
//                    slidingPanel.setTouchEnabled(false);
//                    slidingBar.setImageResource(R.drawable.bar_blank);
//                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
//                    buttons.setEnableScrolling(true);
//                    slidingPanel.setTouchEnabled(true);
//                } else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED) {
//                    buttons.setEnableScrolling(false);
//                    slidingPanel.setTouchEnabled(true);
//                }
//            }
//        });

//        // Set the touch listener for the bottom sliding bar.
//        slidingBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Get the x and y coordinates of the touch event.
//                float x = event.getX();
//                float y = event.getY();
//
//                // Get the width of the bar, and divide by 3 to get thirds.
//                int width = slidingBar.getWidth();
//                int third = width / 3;
//
//                // Get the height of the bar.
//                int height = slidingBar.getHeight();
//
//                // Get the type of action the MotionEvent is.
//                int action = event.getAction();
//
//
//                if (action == android.view.MotionEvent.ACTION_DOWN) {
//                    // Set the time at which the initial depress is done.
//                    startClickTime = Calendar.getInstance().getTimeInMillis();
//                } else if (action == MotionEvent.ACTION_UP && startClickTime != -1) {
//                    // Calculate the time difference between the initial depress and the release.
//                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
//                    // Set the start click time to -1 / null.
//                    startClickTime = -1;
//
//                    // If it's short enough of a time period / is a click.
//                    if (clickDuration < MAX_CLICK_DURATION) {
//                        if (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                            // It's in a collapsed state, so open the relevant panel depending on click.
//                            if (x >= 0 && x <= third) {
//                                loadPanel(SliderMode.MAP);
//                            } else if (x > third && x <= 2 * third) {
//                                loadPanel(SliderMode.ADD);
//                            } else if (x > 2 * third && x <= 3 * third) {
//                                loadPanel(SliderMode.PURCHASE);
//                            }
//                        } else {
//                            // It's already open, so close the panel.
//                            loadPanel(SliderMode.CLOSED);
//                        }
//                    }
//                } else if (action == MotionEvent.ACTION_MOVE) {
//                    // Swiping around the bar here.
//                    // Set the start click time to -1 / null.
//                    startClickTime = -1;
//
//                    // Open the relevant panel depending on where it's swiped to
//                    if (x >= 0 && x <= third) {
//                        loadPanel(SliderMode.MAP);
//                    } else if (x > third && x <= 2 * third) {
//                        loadPanel(SliderMode.ADD);
//                    } else if (x > 2 * third && x <= 3 * third) {
//                        loadPanel(SliderMode.PURCHASE);
//                    }
//                }
//                return true;
//            }
//        });
    }

    public void Map(View view) {
        Intent intent = new Intent(this, Map.class);
        int pos = this.viewPagerTop.getCurrentItem();
        Data data = new Data();
        intent.putExtra("Address", Data.restaurantNames[pos]);
        startActivity(intent);
    }

    // Called when the user taps the pay button
    public void launchPaymentActivity(View view) {
        Intent paymentLaunchIntent = new Intent(this, PaymentActivity.class);
        startActivity(paymentLaunchIntent);
    }


//    //This allows us to click on the different categories of food in the category button
//    public void CategoryClick(View view){
//        int id = view.getId();
//
//        //Allows us to detect which button was clicked
//        switch(id){
//            case 2131427471:
//                dishContext = STARTER;
//                break;
//            case 2131427472:
//                dishContext = MAIN;
//                break;
//            case 2131427473:
//                dishContext = SHARE;
//                break;
//            case 2131427475:
//                dishContext = SIDE;
//                break;
//            case 2131427476:
//                dishContext = DRINK;
//                break;
//            case 2131427477:
//                dishContext = DESSERT;
//                break;
//        }
//
//        ArrayList<View> views = swipeAdapterTop.getViews();
//        final View currentView = views.get(viewPagerTop.getCurrentItem());
//        String cardText = ((TextView) currentView.findViewById(R.id.dish)).getText().toString();
//        String[] food;
//        String[] prices;
//        int[] pictures;
//
//        switch(dishContext){
//            case STARTER:
//                food = Data.starter;
//                prices = Data.starterPrices;
//                pictures = Data.startPictures;
//                dishContext = DishContext.STARTER;
//                break;
//            case MAIN:
//                food = Data.mains;
//                prices = Data.mainsPrices;
//                pictures = Data.mainsPictures;
//                dishContext = MAIN;
//                break;
//            case DRINK:
//                food = Data.drinks;
//                prices = Data.drinksPrices;
//                pictures = Data.drinksPictures;
//                dishContext = DishContext.DRINK;
//                break;
//            case SIDE:
//                food = Data.side;
//                prices = Data.sidePrices;
//                pictures = Data.sidePictures;
//                dishContext = DishContext.SIDE;
//                break;
//            case SHARE:
//                food = Data.share;
//                prices = Data.sharePrices;
//                pictures = Data.sharePictures;
//                dishContext = DishContext.SHARE;
//                break;
//            case DESSERT:
//                food = Data.desserts;
//                prices = Data.dessertsPrices;
//                pictures = Data.dessertPictures;
//                dishContext = DishContext.DESSERT;
//                break;
//            default:
//                food = Data.foodNames;
//                prices = Data.foodPrices;
//                pictures = Data.foodPictures;
//                dishContext = DishContext.NONE;
//        }
//
//        swipeAdapterMini.removeAllViews();
//        for (int j = 0; j < food.length; j++) {
//            // Inflate the dish card.
//            // Grab references to each of the elements inside of the inflated layout.
//            LayoutInflater layoutInflater = getLayoutInflater();
//            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
//            final ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
//            final TextView textDish = (TextView) card.findViewById(R.id.dishMini);
//            final TextView textPrice = (TextView) card.findViewById(R.id.priceMini);
//
//            // Set each of the elements of the card with items from the array.
//            imageView.setImageResource(pictures[j]);
//            textDish.setText(food[j]);
//            textPrice.setText(prices[j]);
//
//            //Set each image in minicards to be clickable and change corresponding big card
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TextView cardDish = ((TextView) currentView.findViewById(R.id.dish));
//                    TextView cardPrice = ((TextView) currentView.findViewById(R.id.price));
//                    ImageView cardImage = ((ImageView) currentView.findViewById(R.id.foodPicture));
//                    cardDish.setText(textDish.getText());
//                    cardPrice.setText(textPrice.getText());
//                    cardImage.setImageDrawable(imageView.getDrawable());
//                }
//            });
//            // Place the cards in the ViewPager
//            swipeAdapterMini.addView(card);
//        }
//
//        // refreshes the viewPagerMini
//        viewPagerMini.setAdapter(swipeAdapterMini);
//        int location = 0;
//        location = getLocation(cardText);
//        viewPagerMini.setCurrentItem(location);
//    }

//    /**
//     * Loads the cateogry picking section
//     */
//    private void loadCategories() {
//        swipeAdapterMini.removeAllViews();
//        for (int i = 0; i < Data.foodNames.length; i++) {
//            // Inflate the dish card.
//            // Grab references to each of the elements inside of the inflated layout.
//            LayoutInflater layoutInflater = getLayoutInflater();
//            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
//            ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
//            TextView textDish = (TextView) card.findViewById(R.id.dishMini);
//            TextView textPrice = (TextView) card.findViewById(R.id.priceMini);
//
//            // Set each of the elements of the card with items from the array.
//            imageView.setImageResource(Data.foodPictures[i]);
//            textDish.setText(Data.foodNames[i]);
//            textPrice.setText(Data.foodPrices[i]);
//
//            final int j = i;
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // This just acts as a placeholder right now, and are hardcoded; once we have
//                    //   the proper categories, can put them in here.
//                    switch(j) {
//                        case 0:
//                            jumpToCategory(DishContext.MAIN);
//                            break;
//                        case 1:
//                            jumpToCategory(DishContext.DRINK);
//                            break;
//                        case 2:
//                            jumpToCategory(DishContext.SIDE);
//                            break;
//                        case 3:
//                            jumpToCategory(DishContext.DESSERT);
//                            break;
//                        case 4:
//                            jumpToCategory(DishContext.SHARE);
//                            break;
//                    }
//                    resetBottomBar();
//                }
//            });
//
//            // Place the cards in the ViewPager
//            swipeAdapterMini.addView(card);
//            swipeAdapterMini.notifyDataSetChanged();
//        }
//
//        // refreshes the viewPagerMini
//        viewPagerMini.setAdapter(swipeAdapterMini);
//        int location = 0;
//        viewPagerMini.setCurrentItem(location);
//    }
//
//    /**
//     * Jumps to the category based on the selection
//     */
//    private void jumpToCategory(DishContext dC) {
//        String[] food;
//        String[] prices;
//        int[] pictures;
//
//        switch(dC){
//            case STARTER:
//                food = Data.starter;
//                prices = Data.starterPrices;
//                pictures = Data.startPictures;
//                dishContext = DishContext.STARTER;
//                break;
//            case MAIN:
//                food = Data.mains;
//                prices = Data.mainsPrices;
//                pictures = Data.mainsPictures;
//                dishContext = MAIN;
//                break;
//            case DRINK:
//                food = Data.drinks;
//                prices = Data.drinksPrices;
//                pictures = Data.drinksPictures;
//                dishContext = DishContext.DRINK;
//                break;
//            case SIDE:
//                food = Data.side;
//                prices = Data.sidePrices;
//                pictures = Data.sidePictures;
//                dishContext = DishContext.SIDE;
//                break;
//            case SHARE:
//                food = Data.share;
//                prices = Data.sharePrices;
//                pictures = Data.sharePictures;
//                dishContext = DishContext.SHARE;
//                break;
//            case DESSERT:
//                food = Data.desserts;
//                prices = Data.dessertsPrices;
//                pictures = Data.dessertPictures;
//                dishContext = DishContext.DESSERT;
//                break;
//            default:
//                food = Data.foodNames;
//                prices = Data.foodPrices;
//                pictures = Data.foodPictures;
//                dishContext = DishContext.NONE;
//        }
//
//        swipeAdapterMini.removeAllViews();
//        for (int j = 0; j < food.length; j++) {
//            // Inflate the dish card.
//            // Grab references to each of the elements inside of the inflated layout.
//            LayoutInflater layoutInflater = getLayoutInflater();
//            ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(R.layout.mini_card, null);
//            final ImageView imageView = (ImageView) card.findViewById(R.id.foodPictureMini);
//            final TextView textDish = (TextView) card.findViewById(R.id.dishMini);
//            final TextView textPrice = (TextView) card.findViewById(R.id.priceMini);
//
//            // Set each of the elements of the card with items from the array.
//            imageView.setImageResource(pictures[j]);
//            textDish.setText(food[j]);
//            textPrice.setText(prices[j]);
//
//            // Place the cards in the ViewPager
//            swipeAdapterMini.addView(card);
//        }
//
//        // refreshes the viewPagerMini
//        viewPagerMini.setAdapter(swipeAdapterMini);
//        viewPagerMini.setCurrentItem(0);
//    }

//    /**
//      * Loads the panel depending on which mode has been specified
//      *
//      * @param sM the mode being switched to
//      */
//    private void loadPanel(SliderMode sM) {
//        if (sM == SliderMode.MAP) {
//            slidingBar.setImageResource(R.drawable.bar_map);
//            slidingTitle.setText("Location");
//            sliderMode = SliderMode.MAP;
//            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
//            findViewById(R.id.locationRow1).setVisibility(View.VISIBLE);
//            findViewById(R.id.locationRow2).setVisibility(View.VISIBLE);
//            findViewById(R.id.addRow1).setVisibility(View.GONE);
//            findViewById(R.id.addRow2).setVisibility(View.GONE);
//            findViewById(R.id.menuRow1).setVisibility(View.GONE);
//            findViewById(R.id.menuRow2).setVisibility(View.GONE);
//        } else if (sM == SliderMode.ADD) {
//            slidingBar.setImageResource(R.drawable.bar_plus);
//            slidingTitle.setText("Add features");
//            sliderMode = SliderMode.ADD;
//            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
//            findViewById(R.id.locationRow1).setVisibility(View.GONE);
//            findViewById(R.id.locationRow2).setVisibility(View.GONE);
//            findViewById(R.id.addRow1).setVisibility(View.VISIBLE);
//            findViewById(R.id.addRow2).setVisibility(View.VISIBLE);
//            findViewById(R.id.menuRow1).setVisibility(View.GONE);
//            findViewById(R.id.menuRow2).setVisibility(View.GONE);
//        } else if (sM == SliderMode.PURCHASE) {
//            slidingBar.setImageResource(R.drawable.bar_hamburger);
//            slidingTitle.setText("Menu");
//            sliderMode = SliderMode.PURCHASE;
//            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
//            findViewById(R.id.locationRow1).setVisibility(View.GONE);
//            findViewById(R.id.locationRow2).setVisibility(View.GONE);
//            findViewById(R.id.addRow1).setVisibility(View.GONE);
//            findViewById(R.id.addRow2).setVisibility(View.GONE);
//            findViewById(R.id.menuRow1).setVisibility(View.VISIBLE);
//            findViewById(R.id.menuRow2).setVisibility(View.VISIBLE);
//        } else if (sM == SliderMode.CLOSED) {
//            slidingBar.setImageResource(R.drawable.bar_blank);
//            sliderMode = SliderMode.CLOSED;
//            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//        }
//    }
}
