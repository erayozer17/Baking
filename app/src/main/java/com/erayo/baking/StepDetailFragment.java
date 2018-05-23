package com.erayo.baking;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erayo.baking.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    private final static String CURRENT_VIDEO_SECOND = "CURRENT_VIDEO_SECOND";
    private final static String IS_VIDEO_PLAYING = "IS_VIDEO_PLAYING";

    @BindView(R.id.player_view)
    SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    public static long mVideoPosition;
    public static boolean isAlreadyPlaying;

    @BindView(R.id.imageView)
    ImageView imageViewIfVideoNotProvided;

    @Nullable
    @BindView(R.id.step_long_description_tv)
    TextView tv;

    private Step step;

    public StepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mVideoPosition = savedInstanceState.getLong(CURRENT_VIDEO_SECOND);
            isAlreadyPlaying = savedInstanceState.getBoolean(IS_VIDEO_PLAYING);
        }
        return inflater.inflate(R.layout.fragment_step_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        step = getArguments().getParcelable("step");

        tv.setText(step.description);
        if (isLandscape() && !isVideoNotProvided()) {
            if (getActivity() != null) {
                hideSystemUI();
                float[] screenSize = getScreenSize(getActivity());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                params.width = (int) screenSize[0];
                params.height = (int) screenSize[1];
                params.bottomMargin = 0;
                params.topMargin = 0;
                params.leftMargin = 0;
                params.rightMargin = 0;
                params.setMarginStart(0);
                params.setMarginEnd(0);
                simpleExoPlayerView.setLayoutParams(params);
            }
        }

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(),
                "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        determineWhichWillBeVisible();

        if (!step.getVideoUrl().equals(""))
            initializePlayer();

    }

    private void determineWhichWillBeVisible() {
        if (isVideoNotProvided()) {
            simpleExoPlayerView.setVisibility(View.GONE);
            imageViewIfVideoNotProvided.setVisibility(View.VISIBLE);
            if (isLandscape()){
                imageViewIfVideoNotProvided.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            }
        } else {
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            imageViewIfVideoNotProvided.setVisibility(View.GONE);
            if (isLandscape()){
                tv.setVisibility(View.GONE);
            }
        }
    }

    private boolean isVideoNotProvided() {
        return step.getVideoUrl().equals("") && step.getThumbnailUrl().equals("");
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initializePlayer() {
        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        String url = step.getVideoUrl().equals("") ? step.getThumbnailUrl() : step.getVideoUrl();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);

        simpleExoPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayerView.hideController();
            }
        });

        if (mVideoPosition != 0) {
            player.seekTo(mVideoPosition);
            player.setPlayWhenReady(isAlreadyPlaying);
        } else {
            player.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            mVideoPosition = player.getCurrentPosition();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    private void hideSystemUI() {
        if (getActivity() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public static float[] getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        return new float[]{
                (float) displayMetrics.widthPixels,
                (float) displayMetrics.heightPixels,
                displayMetrics.density};
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            mVideoPosition = player.getCurrentPosition();
            isAlreadyPlaying = player.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURRENT_VIDEO_SECOND, mVideoPosition);
        outState.putBoolean(IS_VIDEO_PLAYING, isAlreadyPlaying);
    }
}
