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
import com.example.bookingapp.activities.UpdateRequestsActivity;
import com.example.bookingapp.adapters.HostReservationListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.FragmentHostReservationsBinding;
import com.example.bookingapp.dto.HostReservationCollectionResponse;
import com.example.bookingapp.dto.HostReservationResponse;
import com.example.bookingapp.dto.enums.ReservationStatus;
import com.example.bookingapp.security.UserInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostReservationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostReservationsFragment extends Fragment implements HostReservationFragmentInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView reservationListView;
    private List<HostReservationResponse> hostReservations;
    private FragmentHostReservationsBinding binding;
    private HostReservationListAdapter adapter;
    public HostReservationsFragment() {
        // Required empty public constructor
    }
    public static HostReservationsFragment newInstance(String param1, String param2) {
        HostReservationsFragment fragment = new HostReservationsFragment();
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
        binding = FragmentHostReservationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservationListView = binding.hostReservationsListView;

        Call<HostReservationCollectionResponse> call = ClientUtils.reservationService.getHostReservations(HostMainActivity.hostId,0,10, UserInfo.getToken());
        call.enqueue(new Callback<HostReservationCollectionResponse>() {
            @Override
            public void onResponse(Call<HostReservationCollectionResponse> call, Response<HostReservationCollectionResponse> response) {
                if(response.code()==200){
                    hostReservations = (List<HostReservationResponse>) response.body().getHostReservationResponses();
                    adapter = new HostReservationListAdapter(getContext(),hostReservations);
                    reservationListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "SERVER ERROR LOADING RESERVATIONS", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HostReservationCollectionResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "ERROR LOADING RESERVATIONS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void filterReservations(String name, LocalDate startDate, LocalDate endDate, boolean accepted, boolean waiting, boolean rejected) {
        ArrayList<HostReservationResponse> filteredReservations = new ArrayList<>();
        for(HostReservationResponse reservation : hostReservations){
            if(!reservation.getAccommodationName().contains(name) && !name.equals("")){
                continue;
            }
            if(startDate != null && endDate != null){
                if(!containsPeriod(startDate,endDate,reservation.getReservedDate().getStartDate(),reservation.getReservedDate().getEndDate())){
                    continue;
                }
            }
            if((accepted && !(reservation.getReservationStatus() == ReservationStatus.ACCEPTED)) ||
                    (waiting && !(reservation.getReservationStatus() == ReservationStatus.WAITING_FOR_APPROVAL)) ||
                    (rejected && !(reservation.getReservationStatus() == ReservationStatus.REJECTED))){
                continue;
            }
            filteredReservations.add(reservation);
        }
        hostReservations.clear();
        hostReservations.addAll(filteredReservations);
        adapter.notifyDataSetChanged();
    }

    public static boolean containsPeriod(LocalDate period1Start, LocalDate period1End,
                                         LocalDate period2Start, LocalDate period2End) {
        return !period1Start.isAfter(period2Start) && !period1End.isBefore(period2End);
    }
}