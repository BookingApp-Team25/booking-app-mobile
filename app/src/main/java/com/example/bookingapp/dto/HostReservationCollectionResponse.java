package com.example.bookingapp.dto;

import java.util.Collection;

public class HostReservationCollectionResponse {
    Collection<HostReservationResponse> hostReservationResponses;
    long totalNumberOfElements;

    public HostReservationCollectionResponse() {
    }

    public HostReservationCollectionResponse(Collection<HostReservationResponse> hostReservationResponses, long totalNumberOfElements) {
        this.hostReservationResponses = hostReservationResponses;
        this.totalNumberOfElements = totalNumberOfElements;
    }

    public Collection<HostReservationResponse> getHostReservationResponses() {
        return hostReservationResponses;
    }

    public long getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    public void setHostReservationResponses(Collection<HostReservationResponse> hostReservationResponses) {
        this.hostReservationResponses = hostReservationResponses;
    }

    public void setTotalNumberOfElements(long totalNumberOfElements) {
        this.totalNumberOfElements = totalNumberOfElements;
    }
}
