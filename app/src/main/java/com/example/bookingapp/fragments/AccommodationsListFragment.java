package com.example.bookingapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.bookingapp.Accommodation;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.databinding.FragmentAccommodationsListBinding;
import java.util.ArrayList;

public class AccommodationsListFragment extends ListFragment {
    private AccommodationListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Accommodation> mAccommodations;
    private FragmentAccommodationsListBinding binding;

    public static AccommodationsListFragment newInstance(ArrayList<Accommodation> accommodations){
        AccommodationsListFragment fragment = new AccommodationsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, accommodations);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = FragmentAccommodationsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Products List Fragment");
        if (getArguments() != null) {
            mAccommodations = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new AccommodationListAdapter(getActivity(), mAccommodations);
            setListAdapter(adapter);
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
        // Handle the click on item at 'position'
    }

}

