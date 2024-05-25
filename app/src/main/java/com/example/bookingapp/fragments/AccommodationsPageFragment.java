package com.example.bookingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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

    public static ArrayList<AccommodationSummaryResponse> accommodations = new ArrayList<AccommodationSummaryResponse>();
    private AccommodationsPageViewModel productsViewModel;
    private FragmentAccommodationsPageBinding binding;

    public static AccommodationsPageFragment newInstance() {
        return new AccommodationsPageFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        productsViewModel = new ViewModelProvider(this).get(AccommodationsPageViewModel.class);

        binding = FragmentAccommodationsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //prepareProductList(accommodations);
        productsViewModel.fetchAccommodations();

        SearchView searchView = binding.searchText;
        productsViewModel.getText().observe(getViewLifecycleOwner(), searchView::setQueryHint);

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            Log.i("BookingApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        Spinner spinner = binding.btnSort;
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sort_array));
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//                dialog.setMessage("Change the sort option?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Log.v("ShopApp", (String) parent.getItemAtPosition(position));
//                                ((TextView) parent.getChildAt(0)).setTextColor(Color.MAGENTA);
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert = dialog.create();
//                alert.show();
//            }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });

        FragmentTransition.to(AccommodationsListFragment.newInstance(productsViewModel.getAccommodations()), getActivity(), false, R.id.scroll_products_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Dummy data currently not in use
    private void prepareProductList(ArrayList<Accommodation> accommodations){
        accommodations.add(new Accommodation(1L, "Accommodation 1", "Description 1", R.drawable.accommodation1));
        accommodations.add(new Accommodation(2L, "Accommodation 2", "Description 2", R.drawable.accommodation2));
        accommodations.add(new Accommodation(3L, "Accommodation 3", "Description 3", R.drawable.accommodation1));
        accommodations.add(new Accommodation(4L, "Accommodation 4", "Description 4", R.drawable.accommodation2));
    }
}