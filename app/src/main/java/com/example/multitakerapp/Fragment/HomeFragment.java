package com.example.multitakerapp.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.multitakerapp.Adopter.SliderViewPagerAdapter;
import com.example.multitakerapp.MainActivity;
import com.example.multitakerapp.R;
import com.example.multitakerapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    ProgressDialog progress;

    private int currentPage = 0;
    private Handler handler;
    private int[] sliderImageId = new int[]{
            R.drawable.hostel, R.drawable.chief, R.drawable.hostel_1, R.drawable.chief_1,
    };
    private static final long SLIDER_TIMER = 2000; // change slider interval
    private boolean isCountDownTimerActive = false; // let the timer start if and only if it has completed previous task
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (!isCountDownTimerActive) {
                automateSlider();
            }

            handler.postDelayed(runnable, 1000);
            // our runnable should keep running for every 1000 milliseconds (1 seconds)
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.sliderImageVp.setAdapter(new SliderViewPagerAdapter(getActivity(), sliderImageId));
        ///// progress dialogue
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Please Wait");
        progress.setCancelable(true);

        ////// page listener
        binding.sliderImageVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentPage = 0;
                } else if (position == 1) {
                    currentPage = 1;
                } else if (position == 2) {
                    currentPage = 2;
                } else {
                    currentPage = 3;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
        runnable.run();

        return binding.getRoot();
    }

    private void automateSlider() {
        isCountDownTimerActive = true;
        new CountDownTimer(SLIDER_TIMER, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                int nextSlider = currentPage + 1;
                int size = sliderImageId.length;
                ////////////This is used for get the length of slider array list/////////////
                if (size == 0) {
                    size = 4;
                } else {

                    if (nextSlider == size) {
                        nextSlider = 0; // if it's last Image, let it go to the first image
                    }

                    binding.sliderImageVp.setCurrentItem(nextSlider);
                    isCountDownTimerActive = false;
                }
            }
        }.start();
    }
}