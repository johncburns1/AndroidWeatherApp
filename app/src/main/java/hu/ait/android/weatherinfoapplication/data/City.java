package hu.ait.android.weatherinfoapplication.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by johnc on 11/20/2017.
 */

public class City extends RealmObject{

    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_HUMIDITY = "KEY_HUMIDITY";
    public static final String KEY_TEMP = "KEY_TEMP";
    public static final String KEY_WIND = "KEY_WIND";
    public static final String KEY_PRESSURE = "KEY_PRESSURE";
    public static final String KEY_ICON = "KEY_ICON";
    public static final String KEY_COUNTRY = "KEY_COUNTRY";
    public static final String KEY_DESC = "KEY_DESC";
    public static final String KEY_DATE = "KEY_DATE";

    @PrimaryKey
    private String cityID;

    private Date pickUpDate;

    private String cityName;
    private String description;
    private String country_code;
    private String icon;
    private String time;

    private double wind_speed;
    private int cloud_cov;

    private double temp;
    private double max_temp;
    private double min_temp;
    private double pressure;
    private double humidity;

    public City() {

    }

    public City(String cityID, Date pickUpDate, String cityName, String description, String country_code, String base, String icon, int weather, int wind_speed, int cloud_cov, int rain, int dt, double temp, double max_temp, double min_temp, double pressure, double humidity) {
        this.cityID = cityID;
        this.pickUpDate = pickUpDate;
        this.cityName = cityName;
        this.description = description;
        this.country_code = country_code;
        this.icon = icon;
        this.wind_speed = wind_speed;
        this.cloud_cov = cloud_cov;
        this.temp = temp;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(double max_temp) {
        this.max_temp = max_temp;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(double min_temp) {
        this.min_temp = min_temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public int getCloud_cov() {
        return cloud_cov;
    }

    public void setCloud_cov(int cloud_cov) {
        this.cloud_cov = cloud_cov;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
