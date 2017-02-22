package company.whitespace.smartifyandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.fragment.*;
import company.whitespace.smartifyandroid.model.Device;
import company.whitespace.smartifyandroid.model.Room;
import company.whitespace.smartifyandroid.networking.NetworkingAsyncTask;

public class MainActivity extends AppCompatActivity implements DevicesFragment.OnListFragmentInteractionListener, SensorsFragment.OnListFragmentInteractionListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    //private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    public FloatingActionButton fab;

    // urls to load navigation header background image
    // and profile image
    // private static final String urlProfileImg = "http://www.white-space.company/begum.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    public static final String TAG_DEVICES = "devices";
    public static final String TAG_SENSORS = "sensors";
    public static final String TAG_CONTROL_DEVICE = "control device";
    public static final String TAG_ADD_SCHEDULE = "add schedule";
    public static final String TAG_ADD_CONDITION = "add condition";
    public static final String TAG_MANAGE_GROUP = "manage group";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_DEVICES;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private String username, userEmail;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.networking_shared_preferences), Context.MODE_PRIVATE);

        username = sharedPreferences.getString("name", "FATAL") + " " + sharedPreferences.getString("surname", "ERROR");
        userEmail = sharedPreferences.getString("email", "FATAL@ERROR.com");
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        //imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DEVICES;
            loadHomeFragment();
        }

        findViewById(R.id.frame).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Log.i("DENEMEME",CURRENT_TAG);
                toggleFab();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null)
            progressDialog.dismiss();
        super.onDestroy();
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText(username);
        txtWebsite.setText(userEmail);

        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);

        // showing dot next to notifications label
        // navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                DevicesFragment devicesFragment = new DevicesFragment();
                return devicesFragment;
            case 1:
                SensorsFragment sensorsFragment = new SensorsFragment();
                return sensorsFragment;
            case 2:
                ControlDeviceFragment controlDeviceFragment = new ControlDeviceFragment();
                return controlDeviceFragment;
            case 3:
                AddScheduleFragment addScheduleFragment = new AddScheduleFragment();
                return addScheduleFragment;
            case 4:
                AddConditionFragment addConditionFragment = new AddConditionFragment();
                return addConditionFragment;
            case 5:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            case 6:
                ManageGroupFragment manageGroupFragment = new ManageGroupFragment();
                return manageGroupFragment;
            default:
                return new DevicesFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment;
                    case R.id.nav_devices:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_DEVICES;
                        break;
                    case R.id.nav_sensors:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_SENSORS;
                        break;
                    case R.id.nav_control_device:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_CONTROL_DEVICE;
                        break;
                    case R.id.nav_add_schedule:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_ADD_SCHEDULE;
                        break;
                    case R.id.nav_add_condition:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ADD_CONDITION;
                        break;
                    case R.id.nav_manage_group:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_MANAGE_GROUP;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_DEVICES;
                loadHomeFragment();
                return;
            }
        }

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            //super.onBackPressed();
            navItemIndex = 0;
            CURRENT_TAG = TAG_DEVICES;
            loadHomeFragment();
            return;
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            new LogoutAsyncTask().execute();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (CURRENT_TAG.equals(TAG_DEVICES))
            fab.show();
        else
            fab.hide();
    }

    // Devices
    @Override
    public void onListFragmentInteraction(Device device) {

    }

    // Sensors
    @Override
    public void onListFragmentInteraction(Room room) {

    }

    private class LogoutAsyncTask extends NetworkingAsyncTask {

        public LogoutAsyncTask() {
            super(MainActivity.this, "logout");
        }

        @Override
        public void onSessionFail() {
            onSuccess();
        }

        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Logging Out");
                    progressDialog.show();
                }
            });
            SharedPreferences.Editor editor = context.getSharedPreferences(
                    context.getString(R.string.networking_shared_preferences), Context.MODE_PRIVATE).edit();

            editor.putString("session", "");
            editor.putString("name", "");
            editor.putString("surname", "");
            editor.putString("email", "");
            editor.apply();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
