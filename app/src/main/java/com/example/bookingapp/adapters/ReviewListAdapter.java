package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.bookingapp.R;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.ReviewResponse;
import com.example.bookingapp.security.UserInfo;

import java.util.ArrayList;

public class ReviewListAdapter extends ArrayAdapter<ReviewResponse> {
    private ArrayList<ReviewResponse> reviews;
    private ReviewDeleteListener reviewDeleteListener;
    private ReviewReportListener reviewReportListener;
    private String hostUsername;
    public ReviewListAdapter(Context context, ArrayList<ReviewResponse> reviewResponses, ReviewDeleteListener reviewDeleteListener,ReviewReportListener reviewReportListener,String hostUsername){
        super(context, R.layout.review_card, reviewResponses);
        this.reviews=reviewResponses;
        this.reviewDeleteListener = reviewDeleteListener;
        this.reviewReportListener = reviewReportListener;
        this.hostUsername = hostUsername;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReviewResponse review = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_card, parent, false);
        }
        LinearLayout reviewCard = convertView.findViewById(R.id.review_card_item);
        TextView reviewerName = convertView.findViewById(R.id.reviewer_name);
        TextView reviewText = convertView.findViewById(R.id.review_text);
        TextView reviewDate = convertView.findViewById(R.id.review_date);
        TextView reviewRating = convertView.findViewById(R.id.review_rating);
        ImageButton deleteButton = convertView.findViewById(R.id.delete_button);
        ImageButton reportButton = convertView.findViewById(R.id.report_button);
        if (review != null) {
            reviewerName.setText(review.getGuestName());
            reviewText.setText(review.getComment());
            reviewDate.setText(review.getDate());
            reviewRating.setText(new StringBuilder().append(Double.toString(review.getRating())).append(" ★").toString());

            // Set the OnClickListener for the delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if the listener is not null and the reviewDeleteListener is implemented
                    if (reviewDeleteListener != null) {
                        // Call the onDeleteReview method of the listener
                        reviewDeleteListener.onDeleteReview(review);
                    }
                }
            });

            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if the listener is not null and the reviewDeleteListener is implemented
                    if (reviewReportListener != null) {
                        // Call the onDeleteReview method of the listener
                        reviewReportListener.onReportReview(review);
                    }
                }
            });

            // Show or hide delete button based on user
            if(UserInfo.getUsername().equals(review.getGuestUsername())|| UserInfo.getRole().equals("ROLE_Admin")){
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }

            if(UserInfo.getUsername().equals(this.hostUsername) && !review.isReported()){
                reportButton.setVisibility(View.VISIBLE);
            } else {
                reportButton.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public interface ReviewDeleteListener {
        void onDeleteReview(ReviewResponse review);
    }

    public interface ReviewReportListener {
        void onReportReview(ReviewResponse review);
    }
}
