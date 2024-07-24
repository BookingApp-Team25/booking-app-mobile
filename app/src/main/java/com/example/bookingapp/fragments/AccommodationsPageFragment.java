//package com.example.bookingapp.fragments;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.widget.SearchView;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.fragment.app.ListFragment;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Spinner;
//
//import com.example.bookingapp.Accommodation;
//import com.example.bookingapp.AccommodationsPageViewModel;
//import com.example.bookingapp.R;
//import com.example.bookingapp.activities.AccommodationsActivity;
//import com.example.bookingapp.adapters.AccommodationListAdapter;
//import com.example.bookingapp.databinding.FragmentAccommodationsPageBinding;
//import com.example.bookingapp.dto.AccommodationSummaryResponse;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class AccommodationsPageFragment extends ListFragment {
//    private static final String TAG = "AccommodationsFragment";
//    private AccommodationListAdapter adapter;
//    private FragmentAccommodationsPageBinding binding;
//    private LiveData<Collection<AccommodationSummaryResponse>> accommodations;
//    private AccommodationsPageViewModel accommodationsViewModel;
//
//    public static AccommodationsPageFragment newInstance(LiveData<Collection<AccommodationSummaryResponse>> accommodationsLiveData) {
//        AccommodationsPageFragment fragment = new AccommodationsPageFragment();
//        fragment.accommodations = accommodationsLiveData;
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentAccommodationsPageBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        accommodationsViewModel = new ViewModelProvider(this).get(AccommodationsPageViewModel.class);
//        accommodationsViewModel.fetchAccommodations();
//
//        setupButtons(root);
//        setupList();
//
//        if (accommodations != null) {
//            accommodations.observe(getViewLifecycleOwner(), accommodationsList -> {
//                if (accommodationsList != null && !accommodationsList.isEmpty()) {
//                    updateList(new ArrayList<>(accommodationsList));
//                }
//            });
//        }
//
//        return root;
//    }
//
//    private void setupButtons(View root) {
//        Button btnSearch = root.findViewById(R.id.btnSearch);
//        btnSearch.setOnClickListener(v -> {
//            if (isAdded()) {
//                AccommodationSearchFragment searchFragment = new AccommodationSearchFragment();
//                FragmentManager fragmentManager = getParentFragmentManager();
//                if (fragmentManager != null) {
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    transaction.replace(R.id.fragment_accommodation_page, searchFragment);
//                    transaction.addToBackStack("accommodation_page_fragment");
//                    transaction.commit();
//                } else {
//                    Log.e(TAG, "Fragment manager is null");
//                }
//            } else {
//                Log.e(TAG, "Fragment is not added to activity");
//            }
//        });
//
//        Button btnFilters = root.findViewById(R.id.btnFilters);
//        btnFilters.setOnClickListener(v -> {
//            Log.i(TAG, "Bottom Sheet Dialog");
//            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
//            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
//            bottomSheetDialog.setContentView(dialogView);
//            bottomSheetDialog.show();
//        });
//    }
//
//    private void setupList() {
//        adapter = new AccommodationListAdapter(getActivity(), new ArrayList<>());
//        setListAdapter(adapter);
//
//        getListView().setOnItemClickListener((parent, view, position, id) -> {
//            AccommodationSummaryResponse selectedAccommodation = adapter.getItem(position);
//            if (selectedAccommodation != null) {
//                Log.i(TAG, "Clicked: " + selectedAccommodation.getName());
//                Intent intent = new Intent(getContext(), AccommodationsActivity.class);
//                intent.putExtra("accommodationId", selectedAccommodation.getAccommodationId());
//                getContext().startActivity(intent);
//            }
//        });
//    }
//
//    private void updateList(List<AccommodationSummaryResponse> accommodations) {
//        if (adapter != null) {
//            adapter.clear();
//            adapter.addAll(accommodations);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//        accommodations = null;
//    }
//}

package com.example.bookingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import com.example.bookingapp.R;
import com.example.bookingapp.activities.AccommodationsActivity;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.AccommodationsPageViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccommodationsPageFragment extends Fragment {

    private AccommodationsPageViewModel accommodationsViewModel;
    private AccommodationListAdapter adapter;

    public static AccommodationsPageFragment newInstance(Bundle bundle) {
        AccommodationsPageFragment fragment = new AccommodationsPageFragment();
        if (bundle != null && bundle.containsKey("filteredData")) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_accommodations_page, container, false);

        accommodationsViewModel = new ViewModelProvider(this).get(AccommodationsPageViewModel.class);

        Button btnSearch = root.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> showSearchFragment());

        Button btnFilters = root.findViewById(R.id.btnFilters);
        btnFilters.setOnClickListener(v -> showBottomSheetDialog());

        setupListView(root);

        // Check if we received any filtered data
        if (getArguments() != null && getArguments().containsKey("filteredData")) {
            ArrayList<AccommodationSummaryResponse> filteredData = getArguments().getParcelableArrayList("filteredData");
            if (filteredData != null) {
                adapter.updateList(filteredData);
            }
        } else {
            // If no filtered data is received, observe the ViewModel
            accommodationsViewModel.fetchAccommodations();
            observeAccommodations();
        }

        return root;
    }

    private void setupListView(View root) {
        ListView listView = root.findViewById(android.R.id.list);
        adapter = new AccommodationListAdapter(getActivity(), new ArrayList<>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AccommodationSummaryResponse selectedAccommodation = adapter.getItem(position);
            if (selectedAccommodation != null) {
                Log.i("AccommodationsFragment", "Clicked: " + selectedAccommodation.getName());
                Log.i("AccommodationsFragment", "Clicked: " + selectedAccommodation.getAccommodationId());
                Intent intent = new Intent(getContext(), AccommodationsActivity.class);
                intent.putExtra("accommodationId", selectedAccommodation.getAccommodationId());
                getContext().startActivity(intent);
            }
        });
    }

    private void showSearchFragment() {
        // Handle showing search fragment
        if (isAdded()) {
            AccommodationSearchFragment searchFragment = new AccommodationSearchFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_accommodation_page, searchFragment);
                transaction.addToBackStack("accommodation_page_fragment");
                transaction.commit();
            } else {
                Log.e("AccommodationsFragment", "Fragment manager is null");
            }
        } else {
            Log.e("AccommodationsFragment", "Fragment is not added to activity");
        }
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.FullScreenBottomSheetDialog);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
    }

    private void observeAccommodations() {
        LiveData<Collection<AccommodationSummaryResponse>> accommodationsLiveData = accommodationsViewModel.getAccommodations();
        accommodationsLiveData.observe(getViewLifecycleOwner(), accommodationsList -> {
            if (accommodationsList != null && !accommodationsList.isEmpty()) {
                adapter.updateList(new ArrayList<>(accommodationsList));
            }
        });
    }
}
