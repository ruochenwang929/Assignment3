package com.example.assignment3.weather;

//https://api.openweathermap.org/data/2.5/weather?lat=31.230391&lon=121.473701&appid=3bdb45244c9b2d4e6d4fbb5a5f176963

import retrofit2.Call;
import retrofit2.http.GET;

//turn HTTP API into a Java interface.
public interface WeatherApiInterface {
    //@GET means get data
    @GET("weather?lat=31.230391&lon=121.473701&appid=3bdb45244c9b2d4e6d4fbb5a5f176963")
    Call<Root> getWeather();
}
