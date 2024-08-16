package com.example.bookingapp.clients;

import com.example.bookingapp.adapters.LocalDateAdapter;
import com.example.bookingapp.adapters.LocalDateAdapterSimple;
import com.example.bookingapp.adapters.LocalDateTimeAdapterSimple;
import com.example.bookingapp.entities.DatePeriod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {

    //EXAMPLE: http://192.168.43.73:8080/api/
    public static final String SERVICE_API_PATH = "http://192.168.100.3:8080/api/";

    /*
     * Ovo ce nam sluziti za debug, da vidimo da li zahtevi i odgovori idu
     * odnosno dolaze i kako izgeldaju.
     * */
    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    /*
     * Prvo je potrebno da definisemo retrofit instancu preko koje ce komunikacija ici
     * */
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapterSimple())// Register DatePeriod adapter
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterSimple())
            .create();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(test())
            .build();

    /*
     * Definisemo konkretnu instancu servisa na intnerntu sa kojim
     * vrsimo komunikaciju
     * */
    public static AuthService authService = retrofit.create(AuthService.class);
    public static UserService userService=retrofit.create(UserService.class);
    public static ReviewService reviewService = retrofit.create(ReviewService.class);
    public static AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public static ReservationService reservationService = retrofit.create(ReservationService.class);
    public static AccommodationUpdateService accommodationUpdateService = retrofit.create(AccommodationUpdateService.class);
    public static NotificationService notificationService = retrofit.create(NotificationService.class);
}
