package com.example.bookingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.bookingapp.Accommodation;
import com.example.bookingapp.AccommodationsPageViewModel;
import com.example.bookingapp.R;
import com.example.bookingapp.databinding.FragmentAccommodationsPageBinding;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class AccommodationsPageFragment extends Fragment {

//    public static ArrayList<AccommodationSummaryResponse> accommodations = new ArrayList<AccommodationSummaryResponse>();
//    private AccommodationsPageViewModel productsViewModel;
//    private FragmentAccommodationsPageBinding binding;
//
//    public static AccommodationsPageFragment newInstance() {
//        return new AccommodationsPageFragment();
//    }
//
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        productsViewModel = new ViewModelProvider(this).get(AccommodationsPageViewModel.class);
//
//        binding = FragmentAccommodationsPageBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        //prepareProductList(accommodations);
//        productsViewModel.fetchAccommodations();
//
////        Button btnSearch = root.findViewById(R.id.btnSearch);
////        btnSearch.setOnClickListener(v -> {
////            if (isAdded()) { // Check if the fragment is added to the activity
////                // Instantiate and display the search fragment
////                AccommodationSearchFragment searchFragment = new AccommodationSearchFragment();
////                FragmentManager fragmentManager = getParentFragmentManager();
////                if (fragmentManager != null) {
////                    FragmentTransaction transaction = fragmentManager.beginTransaction();
////                    transaction.replace(R.id.fragment_nav_content_main, searchFragment); // Replace 'container' with your actual container ID
////                    transaction.addToBackStack("accommodation_page_fragment"); // Add to back stack to allow back navigation
////                    transaction.commit();
////                } else {
////                    Log.e("YourFragment", "Fragment manager is null");
////                }
////            } else {
////                Log.e("YourFragment", "Fragment is not added to activity");
////            }
////        });
//
//        Button btnFilters = binding.btnFilters;
//        btnFilters.setOnClickListener(v -> {
//            Log.i("BookingApp", "Bottom Sheet Dialog");
//            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
//            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
//            bottomSheetDialog.setContentView(dialogView);
//            bottomSheetDialog.show();
//        });
//
//        FragmentTransition.to(AccommodationsListFragment.newInstance(productsViewModel.getAccommodations()), getActivity(), false, R.id.scroll_products_list);
//
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//    // Dummy data currently not in use
//    private void prepareProductList(ArrayList<Accommodation> accommodations){
//        accommodations.add(new Accommodation(1L, "Accommodation 1", "Description 1", R.drawable.accommodation1));
//        accommodations.add(new Accommodation(2L, "Accommodation 2", "Description 2", R.drawable.accommodation2));
//        accommodations.add(new Accommodation(3L, "Accommodation 3", "Description 3", R.drawable.accommodation1));
//        accommodations.add(new Accommodation(4L, "Accommodation 4", "Description 4", R.drawable.accommodation2));
//    }
}