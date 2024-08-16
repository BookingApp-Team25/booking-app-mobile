package com.example.bookingapp.dto;

import java.util.Collection;

public class AccommodationSummaryCollectionResponse {
    Collection<AccommodationSummaryResponse> summaries;
    long totalNumberOfSummaries;

    public AccommodationSummaryCollectionResponse() {
    }

    public AccommodationSummaryCollectionResponse(Collection<AccommodationSummaryResponse> summaries, long numberOfSummaries) {
        this.summaries = summaries;
        this.totalNumberOfSummaries = numberOfSummaries;
    }

    public Collection<AccommodationSummaryResponse> getSummaries() {
        return summaries;
    }

    public long getTotalNumberOfSummaries() {
        return totalNumberOfSummaries;
    }

    public void setSummaries(Collection<AccommodationSummaryResponse> summaries) {
        this.summaries = summaries;
    }

    public void setTotalNumberOfSummaries(int numberOfSummaries) {
        this.totalNumberOfSummaries = numberOfSummaries;
    }
}
