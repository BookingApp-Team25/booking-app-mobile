package com.example.bookingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.bookingapp.Accommodation;
import com.example.bookingapp.activities.AccommodationsActivity;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.databinding.FragmentAccommodationsListBinding;
import com.example.bookingapp.dto.AccommodationSummaryResponse;

import java.util.ArrayList;
import java.util.Collection;

//public class AccommodationsListFragment extends ListFragment {
//    private AccommodationListAdapter adapter;
//    private static final String ARG_PARAM = "param";
//    private ArrayList<AccommodationSummaryResponse> mAccommodations;
//    private FragmentAccommodationsListBinding binding;
//
////    public static AccommodationsListFragment newInstance(LiveData<Collection<AccommodationSummaryResponse>> accommodations){
////        AccommodationsListFragment fragment = new AccommodationsListFragment();
////        Bundle args = new Bundle();
////        args.putParcelableArrayList(ARG_PARAM, accommodations);
////        fragment.setArguments(args);
////        return fragment;
////    }
//    public static AccommodationsListFragment newInstance(LiveData<Collection<AccommodationSummaryResponse>> accommodationsLiveData) {
//        AccommodationsListFragment fragment = new AccommodationsListFragment();
//
//        accommodationsLiveData.observeForever(new Observer<Collection<AccommodationSummaryResponse>>() {
//            @Override
//            public void onChanged(Collection<AccommodationSummaryResponse> accommodations) {
//                ArrayList<AccommodationSummaryResponse> accommodationList = new ArrayList<>(accommodations);
//
//                Bundle args = new Bundle();
//                args.putParcelableArrayList(ARG_PARAM, accommodationList);
//                fragment.setArguments(args);
//
//                // Remove the observer to prevent memory leaks
//                accommodationsLiveData.removeObserver(this);
//            }
//        });
//
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.i("BookingApp", "onCreateView Accommodations List Fragment");
//        binding = FragmentAccommodationsListBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//        return root;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.i("BookingApp", "onCreate Accommodations List Fragment");
//        if (getArguments() != null) {
//            mAccommodations = getArguments().getParcelableArrayList(ARG_PARAM);
//            adapter = new AccommodationListAdapter(getActivity(), mAccommodations);
//            setListAdapter(adapter);
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//    @Override
//    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        // Handle the click on item at 'position'
//    }
//
//}

public class AccommodationsListFragment extends ListFragment {
    private AccommodationListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private FragmentAccommodationsListBinding binding;

    public static AccommodationsListFragment newInstance(LiveData<Collection<AccommodationSummaryResponse>> accommodationsLiveData) {
        AccommodationsListFragment fragment = new AccommodationsListFragment();

        accommodationsLiveData.observeForever(new Observer<Collection<AccommodationSummaryResponse>>() {
            @Override
            public void onChanged(Collection<AccommodationSummaryResponse> accommodations) {
                fragment.updateAccommodations(accommodations);
            }
        });

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccommodationsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void updateAccommodations(Collection<AccommodationSummaryResponse> accommodations) {
        if (adapter == null) {
            adapter = new AccommodationListAdapter(getActivity(), new ArrayList<>(accommodations));
            setListAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(accommodations);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        AccommodationSummaryResponse selectedAccommodation = adapter.getItem(position);
        if (selectedAccommodation != null) {
            Log.i("BookingApp", "Clicked: " + selectedAccommodation.getName());

            Intent intent = new Intent(getContext(), AccommodationsActivity.class);
            // Pass the entire AccommodationSummaryResponse object
            intent.putExtra("accommodationId", selectedAccommodation.getAccommodationId());
            getContext().startActivity(intent);
        }
    }
}


