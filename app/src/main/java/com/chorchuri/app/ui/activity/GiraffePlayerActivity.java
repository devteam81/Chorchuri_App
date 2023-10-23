package com.chorchuri.app.ui.activity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.chorchuri.app.R;
import com.chorchuri.app.network.APIClient;
import com.chorchuri.app.network.APIConstants;
import com.chorchuri.app.network.APIInterface;
import com.chorchuri.app.util.NetworkUtils;
import com.chorchuri.app.util.UiUtils;
import com.chorchuri.app.util.sharedpref.PrefKeys;
import com.chorchuri.app.util.sharedpref.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.chorchuri.app.network.APIConstants.Constants.SUCCESS;

public class GiraffePlayerActivity /*extends BaseActivity implements PlayerListener*/ {

    /*public static final String VIDEO_URL = "videoUrl";
    public static final String VIDEO_SUBTITLE = "videoSubtitle";
    public static final String VIDEO_ID = "videoId";
    public static final String VIDEO_ELAPSED = "videoElapsed";

    public static String[] resolutionKeys;
    public static String[] resolutionUrls;

    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.resolutionBtn)
    View resolutionBtn;

    private GiraffePlayer player;
    private VideoView surface;
    private String videoUrl;
    private String videoSubtitle;
    private AlertDialog resolutionDialog;
    private int currentResolution = 0;
    private boolean isVideoEnded = false;
    private int videoId;
    private int videoElapsed;
    PrefUtils prefUtils;


    private boolean firstPlay = true;
    private boolean isEnteredPipMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_giraffe_player);
        ButterKnife.bind(this);
        prefUtils = PrefUtils.getInstance(this);

        if (getIntent() != null) {
            videoId = getIntent().getIntExtra(PlayerActivity.VIDEO_ID, 0);
            videoUrl = getIntent().getStringExtra(PlayerActivity.VIDEO_URL);
            videoElapsed = getIntent().getIntExtra(PlayerActivity.VIDEO_ELAPSED, 0);
            videoSubtitle = getIntent().getStringExtra(PlayerActivity.VIDEO_SUBTITLE);
        }

        prepareVideoPlayer();
        prepareResolutionDialog();

        //everything done, play the video
        loadVideo();

        //add button listener after load stated
        resolutionBtn.setVisibility(View.VISIBLE);
    }

    *//**
     * Resolution stuff
     *//*
    private void prepareResolutionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_resolution_text));
        builder.setSingleChoiceItems(resolutionKeys, currentResolution, (dialog, which) -> {
            storeCurrentPositionBeforeConfigChange();
            currentResolution = which;
            loadNewVideo(resolutionUrls[which]);
            dialog.dismiss();
        });
        resolutionDialog = builder.create();
    }

    private void prepareVideoPlayer() {
        surface = new VideoView(this, videoSubtitle);
        container.addView(surface);
        surface.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        surface.setPlayerListener(this);
    }

    @OnClick(R.id.resolutionBtn)
    protected void showResolutionDialog() {
        if (!resolutionDialog.isShowing())
            resolutionDialog.show();
    }

    private void seekToOldPositionAfterConfigChange() {
        if (videoElapsed != 0)
            player.seekTo(videoElapsed);
    }

    private void storeCurrentPositionBeforeConfigChange() {
        videoElapsed = (player.getCurrentPosition() / 1000);
    }


    private void loadNewVideo(String newUrl) {
        videoUrl = newUrl;
        loadVideo();
        seekToOldPositionAfterConfigChange();
    }

    private void loadVideo() {
        if (surface != null) {
            surface.setVideoPath(videoUrl).getPlayer()
                    .setDisplayModel(GiraffePlayer.DISPLAY_FULL_WINDOW)
                    .start();
            player = PlayerManager.getInstance().getCurrentPlayer();
            player.getVideoInfo().setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        }
        showBottomControl(false);
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                || !isInPictureInPictureMode()) {
            if (player != null)
                player.start();
        }
    }


    @Override
    protected void onPause() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                || !isInPictureInPictureMode()) {
            if (player != null) {
                continueWatchingStorePos(player.getCurrentPosition());
                player.pause();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.stop();
            PlayerManager.getInstance().releaseCurrent();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                || !isInPictureInPictureMode())) {
            if (player != null) {
                player.onConfigurationChanged(newConfig);
                showBottomControl(false);
            }
        }
    }

    protected void showBottomControl(boolean show) {
        findViewById(com.github.tcking.giraffeplayer2.R.id.app_video_bottom_box).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onUserLeaveHint() {
        onEnterPipMode();
    }

    private void onEnterPipMode() {
        if (PrefUtils.getInstance(this).getBoolanValue(PrefKeys.PIP_ENTER_WHEN_MINIMIZED, true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                enterPictureInPictureMode();
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        this.isEnteredPipMode = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            if (player != null)
                player.start();
            showBottomControl(false);
        }
    }

    //Player implementations
    @Override
    public void onPrepared(GiraffePlayer giraffePlayer) {
        if (firstPlay) {
            giraffePlayer.seekTo(videoElapsed * 1000);
            firstPlay = false;
        }
    }

    @Override
    public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {

    }

    @Override
    public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(GiraffePlayer giraffePlayer) {
        isVideoEnded = true;
        addVideoToHistory(videoId);
        onPPVCompleted(videoId);
        onBackPressed();
    }

    private void onPPVCompleted(int adminVideoId) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<String> call = apiInterface.ppvEnd(id, token, adminVideoId, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    @Override
    public void onSeekComplete(GiraffePlayer giraffePlayer) {

    }

    @Override
    public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
        if (NetworkUtils.isNetworkConnected(GiraffePlayerActivity.this)) {
            new AlertDialog.Builder(GiraffePlayerActivity.this, R.style.AppTheme_Dialog)
                    .setMessage(getString(R.string.cant_play_video))
                    .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        finish();
                    })
                    .create().show();
        } else {
            UiUtils.showShortToast(GiraffePlayerActivity.this, getString(R.string.check_network));
        }
        return true;
    }

    @Override
    public void onPause(GiraffePlayer giraffePlayer) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                || !isInPictureInPictureMode()) {
            if (isVideoEnded)
                continueWatchingEnded();
            else
                continueWatchingStorePos(giraffePlayer.getCurrentPosition());
        }
    }


    @Override
    public void onRelease(GiraffePlayer giraffePlayer) {

    }

    @Override
    public void onStart(GiraffePlayer giraffePlayer) {
        showBottomControl(true);
    }

    @Override
    public void onTargetStateChange(int oldState, int newState) {

    }

    @Override
    public void onCurrentStateChange(int oldState, int newState) {

    }

    @Override
    public void onDisplayModelChange(int oldModel, int newModel) {

    }

    @Override
    public void onPreparing(GiraffePlayer giraffePlayer) {

    }

    @Override
    public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {

    }

    private void continueWatchingStorePos(int millis) {
        videoElapsed = millis / 1000;
        if (PrefUtils.getInstance(this).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            Call<String> call = APIClient.getClient().create(APIInterface.class)
                    .continueWatchingStorePos(id, token, subProfileId,
                            videoId
                            , millis / 1000
                            , prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }
    }

    private void continueWatchingEnded() {
        if (PrefUtils.getInstance(this).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            Call<String> call = APIClient.getClient().create(APIInterface.class)
                    .continueWatchingEnd(id, token, subProfileId,
                            videoId
                            , prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }
    }

    private void addVideoToHistory(int adminVideoId) {
        if (PrefUtils.getInstance(this).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<String> call = apiInterface.addToHistory(id, token, subProfileId, adminVideoId, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    JSONObject addToHistoryResponse = null;
                    try {
                        addToHistoryResponse = new JSONObject(response.body());
                    } catch (JSONException e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                    if (addToHistoryResponse != null) {
                        if (addToHistoryResponse.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                            Timber.d("%s" + getString(R.string.added_history), adminVideoId);
                        } else {
                            Timber.d("%s" + getString(R.string.couldnot_added), adminVideoId);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    UiUtils.hideLoadingDialog();
                    NetworkUtils.onApiError(GiraffePlayerActivity.this);
                }
            });
        }
    }*/
}