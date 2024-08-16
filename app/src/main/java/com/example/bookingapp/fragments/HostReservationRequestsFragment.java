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
import com.example.bookingapp.adapters.HostReservationListAdapter;
import com.example.bookingapp.adapters.HostReservationRequestsListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.FragmentHostReservationRequestsBinding;
import com.example.bookingapp.databinding.FragmentHostReservationsBinding;
import com.example.bookingapp.dto.HostReservationCollectionResponse;
import com.example.bookingapp.dto.HostReservationResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.security.UserInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostReservationRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostReservationRequestsFragment extends Fragment implements HostReservationRequestsListAdapter.AcceptReservationRequestListener, HostReservationRequestsListAdapter.RejectReservationRequestListener, HostReservationFragmentInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentHostReservationRequestsBinding binding;
    private HostReservationRequestsListAdapter adapter;
    private List<HostReservationResponse> reservationRequests;
    private ListView reservationRequestsListView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HostReservationRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HostReservationRequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HostReservationRequestsFragment newInstance(String param1, String param2) {
        HostReservationRequestsFragment fragment = new HostReservationRequestsFragment();
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
        binding = FragmentHostReservationRequestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservationRequestsListView = binding.hostReservationRequestsListView;

        Call<HostReservationCollectionResponse> call = ClientUtils.reservationService.getUnresolvedHostReservations(HostMainActivity.hostId,0,10, UserInfo.getToken());
        call.enqueue(new Callback<HostReservationCollectionResponse>() {
            @Override
            public void onResponse(Call<HostReservationCollectionResponse> call, Response<HostReservationCollectionResponse> response) {
                if(response.code()==200){
                    reservationRequests = (List<HostReservationResponse>) response.body().getHostReservationResponses();
                    adapter = new HostReservationRequestsListAdapter(getContext(),reservationRequests,HostReservationRequestsFragment.this,HostReservationRequestsFragment.this);
                    reservationRequestsListView.setAdapter(adapter);
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
    public void onAcceptReservationRequest(UUID reservationId) {
        Call<MessageResponse> call = ClientUtils.reservationService.resolveReservationRequest(reservationId,true, UserInfo.getToken());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if(response.code()==200){
                    Toast.makeText(getActivity(), "ACCEPTED RESERVATION", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "SERVER ERROR ACCEPT", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "ERROR ACCEPTING RESERVATION", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRejectReservationRequest(UUID reservationId) {
        Call<MessageResponse> call = ClientUtils.reservationService.resolveReservationRequest(reservationId,false, UserInfo.getToken());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if(response.code()==200){
                    Toast.makeText(getActivity(), "REJECTING RESERVATION", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "SERVER ERROR REJECT", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "ERROR REJECTING RESERVATION", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void filterReservations(String name, LocalDate startDate, LocalDate endDate, boolean accepted, boolean waiting, boolean rejected) {

    }
}