package com.example.bookingapp.fragments;

import java.time.LocalDate;

public interface HostReservationFragmentInterface {
    public void filterReservations(String name, LocalDate startDate, LocalDate endDate, boolean accepted, boolean waiting, boolean rejected);
}
