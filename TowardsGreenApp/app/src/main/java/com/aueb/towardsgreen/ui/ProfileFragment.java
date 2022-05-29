package com.aueb.towardsgreen.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aueb.towardsgreen.R;

public class ProfileFragment extends Fragment {

    private ImageView profileImg;
    private ImageView qrImg;

    private TextView userId;
    private TextView role;
    private TextView points;
    private TextView badgeNum;

    private ListView badgeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImg = view.findViewById(R.id.profile_image);
        qrImg = view.findViewById(R.id.profile_qrCodeImage);
        userId = view.findViewById(R.id.profile_userId);
        role = view.findViewById(R.id.profile_userRole);
        points = view.findViewById(R.id.profile_points);
        badgeNum = view.findViewById(R.id.profile_number_of_badges);

    }

    public ImageView getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(ImageView profileImg) {
        this.profileImg = profileImg;
    }

    public ImageView getQrImg() {
        return qrImg;
    }

    public void setQrImg(ImageView qrImg) {
        this.qrImg = qrImg;
    }

    public TextView getUserId() {
        return userId;
    }

    public void setUserId(TextView userId) {
        this.userId = userId;
    }

    public TextView getRole() {
        return role;
    }

    public void setRole(TextView role) {
        this.role = role;
    }

    public TextView getPoints() {
        return points;
    }

    public void setPoints(TextView points) {
        this.points = points;
    }

    public TextView getBadgeNum() {
        return badgeNum;
    }

    public void setBadgeNum(TextView badgeNum) {
        this.badgeNum = badgeNum;
    }

    public ListView getBadgeList() {
        return badgeList;
    }

    public void setBadgeList(ListView badgeList) {
        this.badgeList = badgeList;
    }
}
