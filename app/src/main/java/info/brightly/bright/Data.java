package info.brightly.bright;

/**
 * Holds all of the item data that is used across the classes.
 *
 * @author Christopher Hyland (chyl9109)
 * @author Justin Ng (jn3141)
 */

public class Data {

    // These are the base restaurant names, and their associated pictures
    public static String[] restaurantNames = {"Reuben Hills", "Opera Bar", "Azure Cafe", "Urban List", "Toby's Estate"};
    public static int[] restaurantPictures = {R.drawable.reuben_hill, R.drawable.manning_bar, R.drawable.poolside_cafe, R.drawable.urban_list_cafe, R.drawable.tobys_estate};

    // These are the base food names, and their associated prices and pictures; also the add on defaults
    public static String[] foodNames = {"Burger", "Latte", "Fruit Salad", "Pancakes", "Wings"};
    public static String[] foodPrices = {"$10", "$3", "$6", "$10", "$9"};
    public static int[] foodPictures = {R.drawable.burger, R.drawable.coffee, R.drawable.fruit_salad, R.drawable.pancakes, R.drawable.wings};

    // Starter names, and their associated prices and pictures
    public static String[] starter = {"Salad", "Soup", "Stew", "Spring Roll", "Wings"};
    public static String[] starterPrices = {"$8", "$6", "$9", "$7", "$9"};
    public static int[] startPictures = {R.drawable.salad, R.drawable.soup, R.drawable.stew, R.drawable.spring_rolls, R.drawable.wings};

    // Side names, and their associated prices and pictures; also the add on defaults
    public static String[] side = {"Vegetables", "Pasta", "Eggplant", "Mushrooms", "Olives"};
    public static String[] sidePrices = {"$6", "$7", "$6", "$7", "$3"};
    public static int[] sidePictures = {R.drawable.vegetables, R.drawable.pasta, R.drawable.eggplant, R.drawable.mushroom, R.drawable.olives};

    public static String[] sideAddOns = {"Side Add-on A", "Side Add-on B", "Side Add-on C"};
    public static String[] sideAddOnsPrices = {"$0.10", "$0.20", "$0.30"};
    public static String[] sideAddOnsExtra = {"Side Add-on D", "Side Add-on E", "Side Add-on F"};
    public static String[] sideAddOnsPricesExtra = {"$0.40", "$0.50", "$0.60"};

    // Side names, and their associated prices and pictures; also the add on defaults
    public static String[] share = {"Hummus", "Garlic Bread", "Pizza", "Chips", "Chicken"};
    public static String[] sharePrices = {"$5", "$7", "$8", "$4", "$20"};
    public static int[] sharePictures = {R.drawable.hummus, R.drawable.garlic_bread, R.drawable.pizza, R.drawable.chips, R.drawable.chicken};

    public static String[] shareAddOns = {"Share Add-on A", "Share Add-on B", "Share Add-on C"};
    public static String[] shareAddOnsPrices = {"$0.10", "$0.20", "$0.30"};
    public static String[] shareAddOnsExtra = {"Share Add-on D", "Share Add-on E", "Share Add-on F"};
    public static String[] shareAddOnsPricesExtra = {"$0.40", "$0.50", "$0.60"};

    // Drink names, and their associated prices and pictures; also the add on defaults
    public static String[] drinks = {"Espresso", "Latte", "Capuccino", "Smoothie", "Tea"};
    public static String[] drinksPrices = {"$2", "$3", "$3", "$7", "$3.50"};
    public static int[] drinksPictures = {R.drawable.espresso, R.drawable.coffee, R.drawable.capuccino, R.drawable.smoothie, R.drawable.tea};

    public static String[] drinksAddOns = {"Drink Add-on A", "Drink Add-on B", "Drink Add-on C"};
    public static String[] drinksAddOnsPrices = {"$0.10", "$0.20", "$0.30"};
    public static String[] drinksAddOnsExtra = {"Drink Add-on D", "Drink Add-on E", "Drink Add-on F"};
    public static String[] drinksAddOnsPricesExtra = {"$0.40", "$0.50", "$0.60"};

    // Main names, and their associated prices and pictures; also the add on defaults
    public static String[] mains = {"Katsu don", "Burger", "Tacos", "Wings", "Sandwich"};
    public static String[] mainsPrices = {"$8", "$15", "$7", "$11", "$5"};
    public static int[] mainsPictures = {R.drawable.katsudon, R.drawable.burger, R.drawable.tacos, R.drawable.wings, R.drawable.sandwich};

    public static String[] mainsAddOns = {"Main Add-on A", "Main Add-on B", "Main Add-on C"};
    public static String[] mainsAddOnsPrices = {"$0.10", "$0.20", "$0.30"};
    public static String[] mainsAddOnsExtra = {"Main Add-on D", "Main Add-on E", "Main Add-on F"};
    public static String[] mainsAddOnsPricesExtra = {"$0.40", "$0.50", "$0.60"};

    // Dessert names, and their associated prices and pictures; also the add on defaults
    public static String[] desserts = {"Gelato", "Pancakes", "Fruit Salad", "Cheesecake", "Ice cream"};
    public static String[] dessertsPrices = {"$3", "$10", "$5", "$5", "$3"};
    public static int[] dessertPictures = {R.drawable.gelato, R.drawable.pancakes, R.drawable.fruit_salad, R.drawable.cheesecake, R.drawable.ice_cream};

    public static String[] dessertsAddOns = {"Dessert Add-on A", "Dessert Add-on B", "Dessert Add-on C"};
    public static String[] dessertsAddOnsPrices = {"$0.10", "$0.20", "$0.30"};
    public static String[] dessertsAddOnsExtra = {"Dessert Add-on D", "Dessert Add-on E", "Dessert Add-on F"};
    public static String[] dessertsAddOnsPricesExtra = {"$0.40", "$0.50", "$0.60"};
}
