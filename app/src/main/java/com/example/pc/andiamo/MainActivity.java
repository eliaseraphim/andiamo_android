package com.example.pc.andiamo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import static com.example.pc.andiamo.Constants.AUTHORIZATION_HEADER;
import static com.example.pc.andiamo.Constants.PLACE_ORDER_EP;
import static com.example.pc.andiamo.Constants.REGISTER_EP;
import static com.example.pc.andiamo.Constants.LOGIN_EP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        PizzaMenuFragment.AddtoCart, DessertDrinkMenuFragment.AddtoCart,
        SandwichMenuFragment.AddtoCart, LoginRegister.comms, CartFragment.CartComm, CartItemFragment.CartItemComm{

    TextView txtHome, txtCart, txtTracker, txtMenu;
    ImageButton btnAccount;
    String currentFragment;

    private int lastMenuChoice = 0; // 0 = pizza ; 1 = subs ; 2 = desserts/drinks
    int masterCart[] = new int[29];
    String userSpecialRequests = "";

    boolean loggedIn;
    String name, email, fullAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        setListeners();
        fragmentHome();
        setUserToNull();
    }

    //--------------------------------Start Initialization Functions--------------------------------

    //  initializeUI()
    //      Binds the TextView variables txtHome, txtMenu, txtCart, txtTracker, and
    //          btnAccount to the appropriate UI element
    private void initializeUI() {
        txtHome = (TextView) findViewById(R.id.txt_home);
        txtCart = (TextView) findViewById(R.id.txt_cart);
        txtTracker = (TextView) findViewById(R.id.txt_delivery_tracker);
        btnAccount = (ImageButton) findViewById(R.id.btn_account);
        txtMenu = (TextView) findViewById(R.id.txt_menu);
    }

    //  setListeners()
    //      Sets onClickListeners to each of the variables txtHome, txtMenu, txtCart,
    //          txtTracker, and btnAccount
    private void setListeners() {
        txtHome.setOnClickListener(this);
        txtCart.setOnClickListener(this);
        txtTracker.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        txtMenu.setOnClickListener(this);
    }

    //  setUserToNull
    //      sets the user to null, upon starting the app
    private void setUserToNull() {
        loggedIn = false;
        name = null;
        email = null;
        fullAddress = null;
    }

    //---------------------------------End Initialization Functions---------------------------------

    //-------------------------------Start Fragment Switch Functions-------------------------------

    private void fragmentHome() {
        Log.d("my_ fragmentHome", "Entered Fragment Home");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_container, homeFragment);
        fragmentTransaction.commit();

        currentFragment = "HOME";
    }

    private void fragmentMenu() {
        Log.d("my_ fragmentMenu", "Entered Fragment Menu");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PizzaMenuFragment menuFragment = new PizzaMenuFragment();
        fragmentTransaction.replace(R.id.fragment_container, menuFragment);
        fragmentTransaction.commit();

        currentFragment = "MENU";
    }

    private void fragmentTracker() {
        startActivity(new Intent(MainActivity.this, TrackerPopup.class));
        Log.d("my_ fragmentTracker", "Entered Fragment Tracker");
        currentFragment = "TRACKER";




        /*
        Log.d("my_ fragmentTracker", "Entered Fragment Tracker");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TrackerFragment trackerFragment = new TrackerFragment();
        fragmentTransaction.replace(R.id.fragment_container, trackerFragment);
        fragmentTransaction.commit();

        currentFragment = "TRACKER";
        */
        /*
        Log.d("my_ fragmentTracker", "Entered Fragment Tracker");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TrackerFragment trackerFragment = new TrackerFragment();
        fragmentTransaction.replace(R.id.fragment_container, trackerFragment);
        fragmentTransaction.commit();

        currentFragment = "TRACKER";
        */
    }

    private void fragmentDessertDrink() {
        Log.d("my_ fragmentDnD", "Entered Fragment Desserts & Drinks");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DessertDrinkMenuFragment dessertFragment = new DessertDrinkMenuFragment();
        fragmentTransaction.replace(R.id.fragment_container, dessertFragment);
        fragmentTransaction.commit();

        currentFragment = "DESSERT";
    }

    private void fragmentPizza() {
        Log.d("my_ fragmentPizza", "Entered Fragment Pizza");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PizzaMenuFragment pizzaFragment = new PizzaMenuFragment();
        fragmentTransaction.replace(R.id.fragment_container, pizzaFragment, "pizza");
        fragmentTransaction.commit();

        currentFragment = "PIZZA";
    }

    private void fragmentSubs(){
        Log.d("my_ fragmentSubs", "Entered Fragment Subs");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SandwichMenuFragment subsFragment = new SandwichMenuFragment();
        fragmentTransaction.replace(R.id.fragment_container, subsFragment, "subs");
        fragmentTransaction.commit();

        currentFragment = "SUBS";
    }

    //--------------------------------End Fragment Switch Functions--------------------------------

    //--------------------------------Start Login/Register Functions--------------------------------

    //loginRegister()
    //  create a dialogFragment that showcases the LoginRegister Dialog
    //
    //  NEED TO IMPLEMENT: Pass a bundle that holds the user information if they are or are not
    //      logged in --> https://stackoverflow.com/questions/42042248/how-to-pass-data-from-activity-to-dialogfragment
    private void loginRegister() {
        if(!loggedIn) {
            FragmentManager fragmentManager = getFragmentManager();
            LoginRegister loginRegisterDialog = new LoginRegister();

            Bundle bundle = new Bundle();
            bundle.putBoolean("LOGGED_IN", loggedIn);
            loginRegisterDialog.setArguments(bundle);

            loginRegisterDialog.show(fragmentManager, "login_register");
        }
        else {
            FragmentManager fragmentManager = getFragmentManager();
            LoginRegister loginRegisterDialog = new LoginRegister();

            Bundle bundle = new Bundle();
            bundle.putBoolean("LOGGED_IN", loggedIn);
            bundle.putString("NAME", name);
            bundle.putString("EMAIL", email);
            bundle.putString("ADDRESS", fullAddress);

            loginRegisterDialog.setArguments(bundle);

            loginRegisterDialog.show(fragmentManager, "LOGIN_REGISTER");
        }
    }

    //---------------------------------End Login/Register Functions---------------------------------

    //-------------------------------Start onClick Listener Functions-------------------------------

    //  onClick
    //      Implementation of View.onClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_home:
                if(!currentFragment.equals("HOME")) {
                    fragmentHome();
                }
                break;
            case R.id.txt_cart:
                if(!currentFragment.equals("CART")) {
                    CartFragment cart;
                    try {
                        FragmentManager fragMan = getFragmentManager();
                        cart = new CartFragment();
                        Bundle bundle = new Bundle();
                        // pass what's in the cart
                        bundle.putIntArray("CART", masterCart);
                        bundle.putString("SPECIAL", userSpecialRequests);
                        bundle.putFloat("TOTAL", calculateTotal());
                        cart.setArguments(bundle);
                        cart.show(fragMan, "cart_frag");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.txt_delivery_tracker:
                if(!currentFragment.equals("TRACKER")) {
                    fragmentTracker();
                }
                break;
            case R.id.btn_account:
                loginRegister();
                break;
            case R.id.txt_menu:
                runPopupMenu();
                break;
        }
    }

    //--------------------------------End onClick Listener Functions--------------------------------

    public void runPopupMenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, txtMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_choices, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.pizzaChoice:
                        fragmentPizza();
                        lastMenuChoice = 0;
                        break;
                    case R.id.subChoice:
                        fragmentSubs();
                        lastMenuChoice = 1;
                        break;
                    case R.id.dndChoice:
                        fragmentDessertDrink();
                        lastMenuChoice = 2;
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    @Override
    public void getQuantities(int[] cart, int offset, String requests) {
        if (cart.length > 0) {
            // do a little bit of cleaning up here -- if the new "request" is blank, don't add it,
            // and make sure we trim any extra lines/spaces the user may have added after their request
            userSpecialRequests += (requests.trim().equals("") ? "" : (requests.trim() + "\n"));
            Log.d("requests", userSpecialRequests);
            for(int i=0;i<cart.length;i++){
                masterCart[i+offset] += cart[i];
                Log.d("cart", Constants.MenuItem.values()[i + offset].getName() + " " + Integer.toString(cart[i]));
            }
        }
    }

    @Override
    public void loginMethod(String username_, String password_) {
        Log.d("LOGINMETHOD", "IN\n\nUsername: " + username_ + "\nPassword: " + password_);

        email = "Email: " + username_;

        //PERFORM LOGIN

        Login login = new Login(username_, password_);
        login.execute();
    }

    @Override
    public void registerMethod(String firstName_, String lastName_, String street_, String apt_,
                               String city_, String state_, String zip_, String email_,
                               String password_) {
        name = "Name: " + firstName_ + " " + lastName_;
        email = "Email: " + email_;
        fullAddress = "Address: " + street_ + ", " + apt_ + "\n\t" + city_ + ", " + state_ + " " + zip_;

        Log.d("REGISTERMETHOD", "IN\n\n" + name + "\n" + email + "\nPassword: "
                + password_ + "\n" + fullAddress);

        //PERFORM REGISTER

        Register register;
        if(apt_.length() != 0) {
            register = new Register(firstName_, lastName_, email_, password_, street_, city_, state_, zip_, apt_);
        }
        else {
            register = new Register(firstName_, lastName_, email_, password_, street_, city_, state_, zip_);
        }
        register.execute();
    }


    //---------------------------------WEB ASYNC SERVICES---------------------------------
    /**
     * To use this, simply call: new Register(fname, lname, email, password, street_address, city, state, zip_code).execute();
     * or if the user has a line number: new Register(fname, lname, email, password, street_address, city, state, zip_code, line_number).execute()
     */
    private class Register extends AsyncTask<Void, Void, Boolean>
    {
        String fname, lname, email, password, street_address, city, state, zip_code, line_number;
        JSONObject responseData;

        Register(String fname, String lname, String email, String password, String street_address, String city, String state, String zip_code) {
            this.fname = fname;
            this.lname = lname;
            this.email = email;
            this.password = password;
            this.street_address = street_address;
            this.city = city;
            this.state = state;
            this.zip_code = zip_code;
        }

        Register(String fname, String lname, String email, String password, String street_address, String city, String state, String zip_code, String line_number) {
            this.fname = fname;
            this.lname = lname;
            this.email = email;
            this.password = password;
            this.street_address = street_address;
            this.city = city;
            this.state = state;
            this.zip_code = zip_code;
            this.line_number = line_number;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI from here
            //do your long running http tasks here,you don't want to pass argument and u can access the parent class' variable url over here

            try {
                // building json...
                JSONObject data = new JSONObject();
                data.put("fname", fname);
                data.put("lname", lname);
                data.put("email", email);
                data.put("password", password);
                data.put("street_address", street_address);
                data.put("city", city);
                data.put("state", state);
                data.put("zip_code", zip_code);

                // optional parameter
                if (line_number != null)
                    data.put("line_number", line_number);

                // building post request
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, data.toString());
                Request request = new Request.Builder()
                        .url(REGISTER_EP)
                        .post(body)
                        .addHeader("AUTHORIZATION", AUTHORIZATION_HEADER)
                        .addHeader("Content-Type", "application/json")
                        .build();

                // parsing response...
                Response response = client.newCall(request).execute();
                if (response.body() == null) return false;
                String strResponse = response.body().string();
                final int code = response.code();
                responseData = new JSONObject(strResponse);

                Log.d("webtag", strResponse);

                if (code == 200) {
                    return true;
                }

                return false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Toast.makeText(getApplicationContext(), "Successfully registered.", Toast.LENGTH_SHORT).show();
                loggedIn = true;

            } else {
                try {
                    // something went wrong
                    String data = (String) responseData.get("data");
                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class PlaceOrder extends AsyncTask<Void, Void, Boolean>
    {

        JSONObject jsonObject;
        int orderID = -1;
        float orderTotal = calculateTotal();

        PlaceOrder() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI from here
            //do your long running http tasks here,you don't want to pass argument and u can access the parent class' variable url over here

            OkHttpClient client = new OkHttpClient();
            // just using 0s for now since we're not grabbing user location
            String query = "latitude=" + 0 + "&longitude=" + 0 + "&total_cost=" + orderTotal;

            // building get request
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, query);
            Request request = new Request.Builder()
                    .url(PLACE_ORDER_EP)
                    .post(body)
                    .addHeader("AUTHORIZATION", AUTHORIZATION_HEADER)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            // parsing response
            try {
                Response response = client.newCall(request).execute();
                if (response.body() == null) return false;
                String strResponse = response.body().string();
                final int code = response.code();

                jsonObject = new JSONObject(strResponse);
                Log.d("JSONObject", jsonObject.toString());

                Log.d("webtag", strResponse);

                if (code == 200) {
                    if(Integer.parseInt(jsonObject.getString("status")) == 0) {
                        return false;
                    }
                    else {
                        orderID = jsonObject.getJSONObject("data").getInt("order_number");
                        return true;
                    }
                }
                else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("JSONException", e.toString());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            LayoutInflater checkoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View checkoutLayout = checkoutInflater.inflate(R.layout.checkout_window,
                    (ViewGroup) findViewById(R.id.checkout_shell));
            TextView textTotal = (TextView) checkoutLayout.findViewById(R.id.order_total_text);
            TextView orderNum = (TextView)  checkoutLayout.findViewById(R.id.order_number_text);
            String totalString = "Your total is $" + String.format("%.2f", orderTotal) + ".";
            String orderString = "Your order number is " + orderID + ". Thank you for ordering with Andiamo!";
            textTotal.setText(totalString);
            orderNum.setText(orderString);
            PopupWindow checkoutWindow = new PopupWindow(checkoutLayout, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            checkoutWindow.showAtLocation(checkoutLayout, Gravity.CENTER, 0, 0);
            for (int i = 0; i < masterCart.length; i++)
                masterCart[i] = 0;
        }
    }

    /**
     * To use this, simply call: new Login(email, password).execute();
     */
    private class Login extends AsyncTask<Void, Void, Boolean>
    {
        String email;
        String password;
        JSONObject jsonObject;

        Login(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI from here
            //do your long running http tasks here,you don't want to pass argument and u can access the parent class' variable url over here

            OkHttpClient client = new OkHttpClient();
            String query = "email=" + email + "&password=" + password;

            // building get request
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, query);
            Request request = new Request.Builder()
                    .url(LOGIN_EP)
                    .post(body)
                    .addHeader("AUTHORIZATION", AUTHORIZATION_HEADER)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            // parsing response
            try {
                Response response = client.newCall(request).execute();
                if (response.body() == null) return false;
                String strResponse = response.body().string();
                final int code = response.code();

                jsonObject = new JSONObject(strResponse);
                Log.d("JSONObject", jsonObject.toString());

                Log.d("webtag", strResponse);

                if (code == 200) {
                    if(Integer.parseInt(jsonObject.getString("status")) == 0) {
                        return false;
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("JSONException", e.toString());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Toast.makeText(getApplicationContext(), "Logged in.", Toast.LENGTH_SHORT).show();
                setData(jsonObject);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid email/password.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setData(JSONObject jsonObject) {
        try {
            JSONObject temp = jsonObject.getJSONObject("data");

            name = "Name: " + temp.getString("firstname") + " " + temp.getString("lastname");
            if(String.valueOf(temp.get("line_number")).matches("null")) {
                fullAddress = "Address: " + temp.getString("street_address") + ", "
                        + "\n\t\t\t" + temp.getString("city") + ", "
                        + temp.getString("state") + " " + temp.getString("zip_code");
            }
            else {
                fullAddress = "Address: " + temp.getString("street_address") + ", "
                        + temp.getString("line_number") + "\n\t\t\t"
                        + temp.getString("city") + ", " + temp.getString("state")
                        + " " + temp.getString("zip_code");
            }
            loggedIn = true;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // CART INTERFACE METHODS
    public void returnToMenu(){
        if(lastMenuChoice == 0) fragmentPizza();
        else if (lastMenuChoice == 1) fragmentSubs();
        else if (lastMenuChoice == 2) fragmentDessertDrink();
        else fragmentHome();
    }
    public boolean handleCheckout(){
        if(!loggedIn) {
            loginRegister();
            return false;
        }
        else {
            // return true if we proceeded to checkout, so we know to close the cart fragment
            PlaceOrder newOrder = new PlaceOrder();
            newOrder.execute();
            return true;
        }
    }
    public void updateSpecialRequests(String newText){
        userSpecialRequests = newText;
    }

    public void updateCartItem(int index, int newVal){
        // if increment is set, increment the item count at index; otherwise must be a decrement
        // if we decrement, make sure to check if we're at zero already
        masterCart[index] = newVal;
    }
    public void removeCartItem(int index){
        masterCart[index] = 0;
    }

    // utility method to calculate total cost of everything in the cart
    public float calculateTotal(){
        float sum = 0;
        for (int i = 0; i < masterCart.length; i++){
            sum += Constants.MenuItem.values()[i].getPrice() * masterCart[i];
        }
        return sum;
    }


}
