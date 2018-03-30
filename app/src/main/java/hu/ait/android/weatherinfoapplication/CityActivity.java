package hu.ait.android.weatherinfoapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.pavlospt.CircleView;

import hu.ait.android.weatherinfoapplication.data.City;
import hu.ait.android.weatherinfoapplication.network.CityAPI;

public class CityActivity extends AppCompatActivity {

    private CityAPI cityAPI;
    private TextView cityCountry;
    private TextView currentDate;
    private ImageView weatherImage;
    private CircleView circleTitle;
    private TextView windResult;
    private TextView humidityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        cityCountry = (TextView) findViewById(R.id.city_country);
        currentDate = (TextView) findViewById(R.id.current_date);
        weatherImage = (ImageView) findViewById(R.id.weather_icon);
        circleTitle = (CircleView) findViewById(R.id.weather_result);
        windResult = (TextView) findViewById(R.id.wind_result);
        humidityResult = (TextView) findViewById(R.id.humidity_result);

        if (getIntent().hasExtra(City.KEY_ICON)) {
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra(City.KEY_ICON)).into(weatherImage);

        }

        if (getIntent().hasExtra(City.KEY_NAME) && getIntent().hasExtra(City.KEY_COUNTRY)) {
            cityCountry.setText(getIntent().getStringExtra(City.KEY_NAME) + ", " + getIntent().getStringExtra(City.KEY_COUNTRY));
        }

        if (getIntent().hasExtra(City.KEY_DATE)) {
            currentDate.setText(getString(R.string.read_time) + getIntent().getStringExtra(City.KEY_DATE));
        }

        if (getIntent().hasExtra(City.KEY_HUMIDITY)) {
            humidityResult.setText(getIntent().getStringExtra(City.KEY_HUMIDITY) + getString(R.string.perct));
        }

        if (getIntent().hasExtra(City.KEY_TEMP)) {
            circleTitle.setTitleText(getIntent().getStringExtra(City.KEY_TEMP) + "\u2103");
            circleTitle.setSubtitleText(getIntent().getStringExtra(City.KEY_DESC));
        }

        if (getIntent().hasExtra(City.KEY_WIND)) {
            windResult.setText(getIntent().getStringExtra(City.KEY_WIND) + getString(R.string.kmph));
        }
    }
}
