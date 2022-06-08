package com.example.multitakerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.multitakerapp.Auth.AddHostelActivity;
import com.example.multitakerapp.Auth.AllHostelActivity;
import com.example.multitakerapp.Fragment.HomeFragment;
import com.example.multitakerapp.Utlis.Constants;
import com.example.multitakerapp.Utlis.MysharedPreferences;
import com.example.multitakerapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ActivityMainBinding binding;
    private Activity activity = MainActivity.this;
    private static int sub_menu = 0;
    ActionBarDrawerToggle toggle;
    static BottomNavigationView navView;
    Toolbar toolbar;
    View hView;
    String st_name, st_email, st_select_role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        st_select_role = MysharedPreferences.getInstance(activity).getSelectRole(Constants.SELECT_USER);

        View headerView = binding.navView.getHeaderView(0);
        TextView tv_name = headerView.findViewById(R.id.tv_customer_name);
        TextView tv_email = headerView.findViewById(R.id.customer_mail);
        tv_name.setText(st_name);
        tv_email.setText(st_email);

        Menu menu = binding.navView.getMenu();
        MenuItem allChief_MI = menu.findItem(R.id.nav_all_chief_data);
        MenuItem addChief_MI = menu.findItem(R.id.nav_add_chief_product);
        MenuItem allHostel_MI = menu.findItem(R.id.nav_all_hostel_data);
        MenuItem addHostel_MI = menu.findItem(R.id.nav_add_hostel);
        MenuItem nearestHostel_MI = menu.findItem(R.id.nav_nearest_hostel);


        if (st_select_role.contains(Constants.USER)) {
            allChief_MI.setVisible(true);
            allHostel_MI.setVisible(true);
            nearestHostel_MI.setVisible(true);

        } else if (st_select_role.contains(Constants.OWNER_HOSTEL)) {
            allHostel_MI.setVisible(true);
            addHostel_MI.setVisible(true);
            nearestHostel_MI.setVisible(true);

        } else if (st_select_role.contains(Constants.CHIEF)) {
            addChief_MI.setVisible(true);
            allChief_MI.setVisible(true);
            nearestHostel_MI.setVisible(true);

        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);

        //////////////l
        loadFragment(new HomeFragment());


    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            loadFragment(new HomeFragment());

        } else if (id == R.id.nav_all_chief_data) {

            /* loadFragment(new AllFarmersFragment());*/

        } else if (id == R.id.nav_add_chief_product) {

            /*Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);*/

        } else if (id == R.id.nav_all_hostel_data) {
            Intent intent = new Intent(activity, AllHostelActivity.class);
            startActivity(intent);
         /*   Intent intent = new Intent(MainActivity.this, AddServiceActivity.class);
            startActivity(intent);*/

        } else if (id == R.id.nav_add_hostel) {
            Intent intent = new Intent(activity, AddHostelActivity.class);
            startActivity(intent);
            /*  loadFragment(new HomeFragment());*/

        } else if (id == R.id.nav_nearest_hostel) {

            /* loadFragment(new MyProductsFragment());*/

        } else if (id == R.id.nav_about_us) {

            /*loadFragment(new MyServicesFragment());*/

        } else if (id == R.id.nav_contact_us) {

            /*loadFragment(new ProductFragment());*/

        } else if (id == R.id.nav_logout) {
            /* loadFragment(new ServicesFragment());*/
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}