package hu.ait.android.weatherinfoapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import hu.ait.android.weatherinfoapplication.adapter.CityAdapter;
import hu.ait.android.weatherinfoapplication.data.City;
import hu.ait.android.weatherinfoapplication.data.WeatherResult;
import hu.ait.android.weatherinfoapplication.network.CityAPI;
import hu.ait.android.weatherinfoapplication.touch.CitiesTouchHelperCallback;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CityAdapter cityAdapter;
    private CityAPI cityAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showAddCityDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cityAPI = retrofit.create(CityAPI.class);

        ((MainApplication) getApplication()).openRealm();

        RealmResults<City> allCities = getRealm().where(City.class).findAll();
        City cityArray[] = new City[allCities.size()];
        List<City> cityResult = new ArrayList<City>(Arrays.asList(allCities.toArray(cityArray)));

        cityAdapter = new CityAdapter(cityResult, this, ((MainApplication) getApplication()).getRealmCities());

        RecyclerView recyclerViewPlaces = (RecyclerView) findViewById(R.id.recyclerViewCities);

        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlaces.setAdapter(cityAdapter);

        CitiesTouchHelperCallback touchHelperCallback = new CitiesTouchHelperCallback(cityAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerViewPlaces);
    }

    private void showAddCityDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.dialog_title);

        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setHint(R.string.dialog_hint);

        builder.setView(input);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Call<WeatherResult> call =
                        cityAPI.getWeather(input.getText().toString(),
                                getString(R.string.metric),
                                getString(R.string.api_key));


                call.enqueue(new Callback<WeatherResult>() {

                    @Override
                    public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {

                        if (response.isSuccessful()) {

                            getRealm().beginTransaction();

                            City newCity = getRealm().createObject(City.class, UUID.randomUUID().toString());

                            Date date = new Date();
                            date.setTime((long) response.body().getDt() * 1000);
                            newCity.setTime(date.toString());

                            newCity.setCityName(input.getText().toString());
                            newCity.setHumidity(response.body().getMain().getHumidity());
                            newCity.setIcon(response.body().getWeather().get(0).getIcon() + getString(R.string.peengee));
                            newCity.setDescription(response.body().getWeather().get(0).getDescription());
                            newCity.setTemp(response.body().getMain().getTemp());
                            newCity.setWind_speed(response.body().getWind().getSpeed());
                            newCity.setCountry_code(response.body().getSys().getCountry());
                            newCity.setPickUpDate(new Date(System.currentTimeMillis()));

                            getRealm().commitTransaction();

                            cityAdapter.addCity(newCity);

                        } else {

                            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();

                            try {
                                System.out.println(response.errorBody().string());

                            } catch (IOException e) {
                                e.getStackTrace();

                            } finally {
                                System.out.println(call.request().url());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.request_failed, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void setCityContents(City city) {

    }

    public Realm getRealm() {
        return ((MainApplication) getApplication()).getRealmCities();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_new) {
            showAddCityDialog();

        } else if (id == R.id.nav_about) {
            Toast.makeText(getApplicationContext(), R.string.about_msg, Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deleteCity(City city) {
        getRealm().beginTransaction();
        city.deleteFromRealm();
        getRealm().commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication) getApplication()).closeRealm();
    }
}
