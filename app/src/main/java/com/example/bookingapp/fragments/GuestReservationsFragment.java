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
import com.example.bookingapp.adapters.FavouriteAccommodationsListAdapter;
import com.example.bookingapp.adapters.GuestReservationAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.FragmentGuestFavouriteAccommodationsBinding;
import com.example.bookingapp.databinding.FragmentGuestReservationsBinding;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.ReservationRequest;
import com.example.bookingapp.dto.ReservationSummaryCollectionResponse;
import com.example.bookingapp.dto.ReviewRequest;
import com.example.bookingapp.dto.enums.ReservationStatus;
import com.example.bookingapp.security.UserInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestReservationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestReservationsFragment extends Fragment implements GuestReservationAdapter.CancelReservationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView guestReservationsListView;
    private ArrayList<ReservationRequest> reservations;
    private FragmentGuestReservationsBinding binding;
    private GuestReservationAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GuestReservationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestReservationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestReservationsFragment newInstance(String param1, String param2) {
        GuestReservationsFragment fragment = new GuestReservationsFragment();
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
        binding = FragmentGuestReservationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        guestReservationsListView = binding.guestReservationsListView;
        reservations = new ArrayList<>();

        Call<ReservationSummaryCollectionResponse> call = ClientUtils.accommodationService.getGuestReservations(UserInfo.getUsername(),UserInfo.getToken());
        call.enqueue(new Callback<ReservationSummaryCollectionResponse>() {
            @Override
            public void onResponse(Call<ReservationSummaryCollectionResponse> call, Response<ReservationSummaryCollectionResponse> response) {
                if(response.code()==200){
                    reservations.clear();
                    reservations.addAll(response.body().getSummaries());
                    adapter =new GuestReservationAdapter(getContext(),reservations, GuestReservationsFragment.this);
                    guestReservationsListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "SERVER ERROR LOADING RESERVATIONS", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ReservationSummaryCollectionResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "ERROR LOADING RESERVATIONS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCancel(String reservationId) {
        Call<Boolean> call = ClientUtils.reservationService.cancelReservation(reservationId,UserInfo.getToken());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200){
                    Toast.makeText(getActivity(), "Successfully cancelled reservation", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "SERVER ERROR CANCELING", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), "ERROR CANCELING", Toast.LENGTH_SHORT).show();
            }
        });
    }
}