package com.example.bookingapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookingapp.AccommodationSearchViewModel;
import com.example.bookingapp.R;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class AccommodationSearchFragment extends Fragment {

    private Spinner spinnerCities;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private NumberPicker numberPickerPeople;
    private Button buttonSearch;
    private AccommodationSearchViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_accommodation_search, container, false);

        // Initialize your views
        spinnerCities = root.findViewById(R.id.spinner_cities);
        editTextStartDate = root.findViewById(R.id.editText_start_date);
        editTextEndDate = root.findViewById(R.id.editText_end_date);
        numberPickerPeople = root.findViewById(R.id.numberPicker_people);
        buttonSearch = root.findViewById(R.id.button_search);

        // Set NumberPicker values
        numberPickerPeople.setMinValue(1);
        numberPickerPeople.setMaxValue(10);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(AccommodationSearchViewModel.class);

        // Set OnClickListener for the search button
        buttonSearch.setOnClickListener(v -> {
            Log.d("AccommodationSearchFragment - ButtonSearch", "Clicked the button!");

            String selectedCity = spinnerCities.getSelectedItem().toString();
            String startDate = editTextStartDate.getText().toString();
            String endDate = editTextEndDate.getText().toString();
            int numberOfPeople = numberPickerPeople.getValue();

            // Call ViewModel's search function with collected data
            viewModel.searchAccommodations(selectedCity, startDate, endDate, numberOfPeople);

            AccommodationsPageFragment pageFragment = AccommodationsPageFragment.newInstance(viewModel.getAccommodations());
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_accommodation_search, pageFragment);
            transaction.addToBackStack(null); // Add to back stack to allow back navigation
            transaction.commit();
            getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });

        return root;
    }
}
