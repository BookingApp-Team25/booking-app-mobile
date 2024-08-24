package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bookingapp.R;
import com.example.bookingapp.dto.NotificationResponse;
import com.example.bookingapp.dto.UserReportResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ReportListAdapter extends ArrayAdapter<UserReportResponse> {
    List<UserReportResponse> userReports;
    BlockUserListener blockUserListener;

    public ReportListAdapter(Context context, List<UserReportResponse> userReports, BlockUserListener blockUserListener){
        super(context, R.layout.report_card, userReports);
        this.userReports = userReports;
        this.blockUserListener = blockUserListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
       UserReportResponse userReportResponse = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_card, parent, false);
        }


        ConstraintLayout reportLayout = convertView.findViewById(R.id.reportCard);
        TextView reportUsername = convertView.findViewById(R.id.reportCardUsernameTextView);
        TextView reportRole = convertView.findViewById(R.id.reportCardUserRoleTextView);
        EditText reportReason = convertView.findViewById(R.id.reportCardReasonEditText);
        FloatingActionButton blockButton = convertView.findViewById(R.id.reportCardBlockButton);
        if (userReportResponse != null) {
            reportUsername.setText(userReportResponse.getUsername());
            reportRole.setText(userReportResponse.getRole());
            reportReason.setText(userReportResponse.getReason());
            blockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(blockUserListener != null){
                        blockUserListener.onBlockUser(userReportResponse.getUserId());
                    }
                }
            });
        }

        return convertView;
    }
    public interface BlockUserListener {
        void onBlockUser(UUID userId);
    }
}