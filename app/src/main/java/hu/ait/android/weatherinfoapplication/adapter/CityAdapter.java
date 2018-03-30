package hu.ait.android.weatherinfoapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import hu.ait.android.weatherinfoapplication.CityActivity;
import hu.ait.android.weatherinfoapplication.MainActivity;
import hu.ait.android.weatherinfoapplication.R;
import hu.ait.android.weatherinfoapplication.data.City;
import io.realm.Realm;

/**
 * Created by johnc on 11/20/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvCity;
        public ImageView ivDelete;
        public TextView tvDesc;
        public LinearLayout rowLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.ivRowIcon);
            tvCity = (TextView) itemView.findViewById(R.id.tvCityName);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            rowLayout = (LinearLayout) itemView.findViewById(R.id.rowLayout);
        }
    }

    private List<City> cityList;
    private Context context;
    private int lastPosition = -1;
    private Realm realmCity;

    public CityAdapter(List<City> cityList, Context context, Realm realmCity) {
        this.cityList = cityList;
        this.context = context;
        this.realmCity = realmCity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_city, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final City city = cityList.get(position);

        final String icon_url = context.getString(R.string.image_url_base) + city.getIcon().toString();
        Glide.with(context).load(icon_url).into(viewHolder.ivIcon);

        viewHolder.tvCity.setText(city.getCityName().toString() + ", " + city.getCountry_code().toString());
        viewHolder.ivDelete.setImageResource(R.drawable.x);
        viewHolder.tvDesc.setText(city.getDescription());

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCity(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cityIntent;

                cityIntent = new Intent(context, CityActivity.class);
                cityIntent.putExtra(City.KEY_NAME, city.getCityName().toString());
                cityIntent.putExtra(City.KEY_HUMIDITY, Double.toString(city.getHumidity()));
                cityIntent.putExtra(City.KEY_TEMP, Double.toString(city.getTemp()));
                cityIntent.putExtra(City.KEY_WIND, Double.toString(city.getWind_speed()));
                cityIntent.putExtra(City.KEY_PRESSURE, Double.toString(city.getPressure()));
                cityIntent.putExtra(City.KEY_ICON, icon_url);
                cityIntent.putExtra(City.KEY_COUNTRY, city.getCountry_code().toString());
                cityIntent.putExtra(City.KEY_DESC, city.getDescription().toString());
                cityIntent.putExtra(City.KEY_DATE, city.getTime().toString());

                context.startActivity(cityIntent);
            }
        });

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void addCity(City newCity) {
        cityList.add(0, newCity);
        notifyItemInserted(0);
    }

    public void removeCity(int index) {
        ((MainActivity) context).deleteCity(cityList.get(index));
        cityList.remove(index);
        notifyItemRemoved(index);
    }

    public void swapCities(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(cityList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(cityList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
