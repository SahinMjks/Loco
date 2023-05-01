package com.example.loco_v1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class IntroScreenPageFragment extends Fragment {

    private static final String ARG_IMAGE_RES_ID = "image_res_id";
    private static final String ARG_TEXT = "text";

    private int mImageResId;
    private String mText;

    public static IntroScreenPageFragment newInstance(int imageResId, String text) {
        IntroScreenPageFragment fragment = new IntroScreenPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageResId = getArguments().getInt(ARG_IMAGE_RES_ID);
            mText = getArguments().getString(ARG_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_screen_page, container, false);
        ImageView imageViewIntroPage = view.findViewById(R.id.imageViewIntroPage);
        TextView textViewIntroPage = view.findViewById(R.id.textViewIntroPage);
        imageViewIntroPage.setImageResource(mImageResId);
        textViewIntroPage.setText(mText);
        return view;
    }
}
