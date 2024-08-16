package com.example.bookingapp.clients;

import java.time.LocalDate;

public class DateManagementService {
    public boolean doPeriodsOverlap(LocalDate startDate1, LocalDate endDate1,LocalDate startDate2, LocalDate endDate2){
        return !(startDate1.isAfter(endDate2) || startDate2.isAfter(endDate1));
    }
}
