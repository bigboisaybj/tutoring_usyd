package info.brightly.bright;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bartoszlipinski.flippablestackview.FlippableStackView;

import java.util.ArrayList;

/**
 * This class is an extension of the PagerAdapter class within Android, and it controls the swiping
 * and the objects that are rendered for the top set of cards in the main screen.
 *
 * @author Justin Ng (jn3141)
 */
public class StackSwipeAdapter extends PagerAdapter {

    private ArrayList<View> views = new ArrayList<View>();

    /**
     * Adds a View to the end of the views list.
     * The app should call this to add Views.
     *
     * @param v the View to be added
     * @return int the position of the new view.
     */
    public int addView(View v) {
        return addView(v, views.size());
    }

    /**
     * Adds a View to the views list at the specified position.
     * The app should call this to add Views.
     *
     * @param v the View to be added
     * @param position the index the View is to be added at
     * @return int the position of the new view.
     */
    public int addView(View v, int position) {
        views.add(position, v);
        return position;
    }

    /**
     * Removes a View.
     * The app should call this to remove Views.
     *
     * @param vP the FlippableStackView from which the View is going to be removed.
     * @param v the View to be removed.
     * @return int the position of the removed View.
     */
    public int removeView(ViewPager vP, View v) {
        return removeView(vP, views.indexOf(v));
    }

    /**
     * Removes a View at the specified position.
     * The app should call this to remove Views.
     *
     * @param vP the FlippableStackView from which the View is going to be removed.
     * @param position the index the View-to-be-removed is at
     * @return int the position of the removed View.
     */
    public int removeView(ViewPager vP, int position) {
        vP.setAdapter(null);
        views.remove(position);
        vP.setAdapter(this);
        return position;
    }

    /**
     * Removes all the Views.
     * The app should call this to remove all the Views.
     */
    public boolean removeAllViews(ViewPager vP) {
        vP.setAdapter(null);
        views.clear();
        vP.setAdapter(this);
        return true;
    }

    /**
     * Returns the View at the given position.
     * The app should call this to get Views.
     * @param index the position of the View in the list
     * @return View the View being retrieved.
     */
    public View getView(int index) {
        return views.get(index);
    }

    public ArrayList<View> getViews() {
        return views;
    }

    /**
     * Tells the ViewPager where the page should be displayed.
     *
     * @param object the page that is being handled by the ViewPager.
     * @return int the position of the item; POSITION_NONE if the page no longer exists
     */
    @Override
    public int getItemPosition(Object object) {
        int index = views.indexOf(object);
//        if (index == -1) {
//            return POSITION_NONE;
//        } else {
//            return index;
//        }
        System.out.println(index);
        return index;
    }

    /**
     * Returns the number of Views for the StackSwipeAdapter.
     * Seeing that the number of images and the number of Views is identical, just returns the
     * number of images.
     *
     * @return int the number of items in the StackSwipeAdapter / imageResources array
     */
    @Override
    public int getCount() {
        return views.size();
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
    public Object instantiateItem(ViewGroup container, final int position) {

        View v = views.get(position);
        container.addView(v);
        return v;
    }

    /**
     * Sets a whole ArrayList to be used as views
     * @param views the set of views to be used
     */
    public void setViews(ArrayList<View> views) {
        this.views = views;
    }

    /**
     * Removes a ViewPager from the given position.
     *
     * @param container the containing View from which the page will be removed.
     * @param position  the ViewPager position to be removed.
     * @param object    the same object that was returned by instantiateItem(View, int).
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
