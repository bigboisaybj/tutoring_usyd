package info.brightly.bright;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is an extension of the PagerAdapter class within Android, and it controls the swiping
 * and the objects that are rendered for the mini set of cards in the main screen.
 *
 * @author Christopher Hyland (chyl9109)
 * @author Justin Ng (jn3141)
 * @invariant dishes, prices and images have to have te same number of items in each of them as they
 * correspond to one another (use them until mapping is implemented).
 * @invariant restaurants also has to match the number of items in dishes, prices and images
 * currently; this is not intended, and needs to be be altered later.
 */
public class MiniCardSwipeAdapter extends PagerAdapter {

    // The application context that TopSwipeAdapter runs within.
    private Context ctx;

    // The LayoutInflater object used to generate Layouts for the Views
    private LayoutInflater layoutInflater;

    // Arrays of items to be placed into the TopSwipeAdapter
    // The index of each array correspond to one another (i.e. images[0] is inked to restaurants[0]/
    // dishes[0]/prices[0]
    private int[] images = {R.drawable.burger, R.drawable.coffee, R.drawable.fruit_salad, R.drawable.pancakes, R.drawable.wings};
    private String[] dishes = {"Burger", "Latte", "Fruit Salad", "Pancakes", "Wings"};
    private String[] prices = {"$15", "$4", "$3", "$5", "$11"};


    public ArrayList getCards() {
        return cards;
    }

    private ArrayList cards = new ArrayList<View>();

    /**
     * Constructor for MiniCardSwipeAdapter
     *
     * @param ctx the application context that MiniCardSwipeAdapter runs within.
     */
    public MiniCardSwipeAdapter(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Sets the dishes array.
     * @param dishes the array to replace the MiniCardSwipeAdapter's dishes array
     */
    public void setDishes(String[] dishes) {
        this.dishes = dishes;
        return;
    }

    /**
     * Sets the prices array
     * @param prices the array to replace the MiniCardSwipeAdapter's prices array
     */
    public void setPrices(String[] prices) {
        this.prices = prices;
        return;
    }

    /**
     * Sets the images array
     * @param images the array to replace the MiniCardSwipeAdapter's prices array
     */
    public void setImages(int[] images) {
        this.images = images;
    }

    /**
     * Returns the number of Views for the MiniCardSwipeAdapter.
     * Seeing that the number of images and the number of Views is identical, just returns the
     * number of images.
     *
     * @return int the number of items in the MiniCardSwipeAdapter / imageResources array
     */
    @Override
    public int getCount() {
        return images.length;
    }

    /**
     * Just checks whether a View is from the object given
     *
     * @param view View to check for association with o
     * @param o    Object to check for association with View
     * @return boolean true if view is associated with o
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view == o);
    }

    /**
     * Creates the page for the given position.
     * Takes the position number, pulls items from the arrays matching that position, and creates a
     * view out of it.
     *
     * @param container the containing View in which the page will be shown.
     * @param position  the page position to be instantiated.
     * @return Object    the Object that represents the new page.
     */
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        // Inflate the layout.
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View item_view = layoutInflater.inflate(R.layout.mini_card, container, false);

        // Grab references to each of the elements inside of the inflated layout.
        ImageView imageView = (ImageView) item_view.findViewById(R.id.foodPictureMini);
        TextView textDish = (TextView) item_view.findViewById(R.id.dishMini);
        TextView textPrice = (TextView) item_view.findViewById(R.id.priceMini);

        // Set each of the elements with items from the array.
        imageView.setImageResource(images[position]);
        textDish.setText(dishes[position]);
        textPrice.setText(prices[position]);
        imageView.setColorFilter(Color.argb(15, 0, 0, 0));

        // add the View to the container.
        container.addView(item_view);
        cards.add(item_view);
        return item_view;
    }

    /**
     * Removes a View from the given position.
     *
     * @param container the containing View from which the page will be removed.
     * @param position  the View position to be removed.
     * @param object    the same object that was returned by instantiateItem(View, int).
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
