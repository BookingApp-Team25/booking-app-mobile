package com.example.bookingapp.dto;

import java.util.Collection;

public class ReservationSummaryCollectionResponse {
    private Collection<ReservationRequest> summaries;
    private long totalNumberOfSummaries;

    public ReservationSummaryCollectionResponse() {
    }

    public ReservationSummaryCollectionResponse(Collection<ReservationRequest> summaries, long totalNumberOfSummaries) {
        this.summaries = summaries;
        this.totalNumberOfSummaries = totalNumberOfSummaries;
    }

    public Collection<ReservationRequest> getSummaries() {
        return summaries;
    }

    public void setSummaries(Collection<ReservationRequest> summaries) {
        this.summaries = summaries;
    }

    public long getTotalNumberOfSummaries() {
        return totalNumberOfSummaries;
    }

    public void setTotalNumberOfSummaries(long totalNumberOfSummaries) {
        this.totalNumberOfSummaries = totalNumberOfSummaries;
    }
}

