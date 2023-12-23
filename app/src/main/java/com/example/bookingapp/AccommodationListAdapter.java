package com.example.bookingapp;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/*
 * Adapteri unutar Android-a sluze da prikazu unapred nedefinisanu kolicinu podataka
 * pristigle sa interneta ili ucitane iz baze ili filesystem-a uredjaja.
 * Da bi napravili adapter treba da napraivmo klasu, koja nasledjuje neki od postojecih adaptera.
 * Za potrebe ovih vezbi koristicemo ArrayAdapter koji kao izvor podataka iskoristi listu ili niz.
 * Nasledjivanjem bilo kog adaptera, dobicemo
 * nekolko metoda koje moramo da referinisemo da bi adapter ispravno radio.
 * */
public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private ArrayList<Accommodation> aAccommodations;

    public AccommodationListAdapter(Context context, ArrayList<Accommodation> accommodations){
        super(context, R.layout.accommodation_card, accommodations);
        aAccommodations = accommodations;

    }
    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return aAccommodations.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Accommodation getItem(int position) {
        return aAccommodations.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra. Naravno mozemo iskoristiti i
     * jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene poziciuje (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Accommodation accommodation = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_card,
                    parent, false);
        }
        LinearLayout productCard = convertView.findViewById(R.id.product_card_item);
        ImageView imageView = convertView.findViewById(R.id.product_image);
        TextView productTitle = convertView.findViewById(R.id.product_title);
        TextView productDescription = convertView.findViewById(R.id.product_description);

        if(accommodation != null){
            imageView.setImageResource(accommodation.getImage());
            productTitle.setText(accommodation.getTitle());
            productDescription.setText(accommodation.getDescription());
            productCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + accommodation.getTitle() + ", id: " +
                        accommodation.getId().toString());
//                Toast.makeText(getContext(), "Clicked: " + product.getTitle()  +
//                        ", id: " + product.getId().toString(), Toast.LENGTH_SHORT).show();

                // Uncomment the next line to start a new activity
                Intent intent = new Intent(getContext(), AccommodationsActivity.class);

                // If you want to pass data to the new activity, you can use intent.putExtra
                // For example, passing the product ID:
                // intent.putExtra("product_id", product.getId());

                // Uncomment the next line to start the new activity
                getContext().startActivity(intent);
            });
        }

        return convertView;
    }
}
