package com.example.bookingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.HostMainActivity;
import com.example.bookingapp.adapters.FavouriteAccommodationsListAdapter;
import com.example.bookingapp.adapters.HostReservationListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.FragmentGuestFavouriteAccommodationsBinding;
import com.example.bookingapp.databinding.FragmentHostReservationsBinding;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.HostReservationCollectionResponse;
import com.example.bookingapp.dto.HostReservationResponse;
import com.example.bookingapp.security.UserInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestFavouriteAccommodationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestFavouriteAccommodationsFragment extends Fragment implements FavouriteAccommodationsListAdapter.RemoveFavouriteListener, FavouriteAccommodationsListAdapter.AccommodationDetailsListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView favouriteAccommodationsListView;
    private ArrayList<AccommodationSummaryResponse> accommodations;
    private FragmentGuestFavouriteAccommodationsBinding binding;
    private FavouriteAccommodationsListAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GuestFavouriteAccommodationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestFavouriteAccommodationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestFavouriteAccommodationsFragment newInstance(String param1, String param2) {
        GuestFavouriteAccommodationsFragment fragment = new GuestFavouriteAccommodationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGuestFavouriteAccommodationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favouriteAccommodationsListView = binding.guestFavouriteAccommodationsListView;
        accommodations = new ArrayList<>();

        Call<Collection<AccommodationSummaryResponse>> call = ClientUtils.accommodationService.getFavouriteAccommodations(UserInfo.getUsername(),UserInfo.getToken());
        call.enqueue(new Callback<Collection<AccommodationSummaryResponse>>() {
            @Override
            public void onResponse(Call<Collection<AccommodationSummaryResponse>> call, Response<Collection<AccommodationSummaryResponse>> response) {
                if(response.code()==200){
                    accommodations.clear();
                    accommodations.addAll(response.body());
                    adapter = new FavouriteAccommodationsListAdapter(getContext(),accommodations,GuestFavouriteAccommodationsFragment.this,GuestFavouriteAccommodationsFragment.this);
                    favouriteAccommodationsListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "SERVER ERROR LOADING FAVOURITES", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Collection<AccommodationSummaryResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "ERROR LOADING FAVOURITES", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickAccommodation(String accommodationId) {

    }

    @Override
    public void onRemoveAccommodation(AccommodationSummaryResponse accommodationSummaryResponse) {
        String accommodationId = accommodationSummaryResponse.getAccommodationId().toString();
        Call<Boolean> call = ClientUtils.accommodationService.removeFavouriteAccommodation(UserInfo.getUsername(),accommodationId, UserInfo.getToken());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200){
                    Toast.makeText(getActivity(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                    accommodations.removeIf(accommodation -> accommodation.getAccommodationId().equals(accommodationSummaryResponse.getAccommodationId()));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "SERVER ERROR REMOVING FAVOURITE", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), "ERROR REMOVING FAVOURITE", Toast.LENGTH_SHORT).show();
            }
        });
    }
}