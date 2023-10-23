package com.chorchuri.app.ui.activity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/*import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;*/
import com.chorchuri.app.R;
import com.chorchuri.app.network.APIClient;
import com.chorchuri.app.network.APIConstants;
import com.chorchuri.app.network.APIInterface;
import com.chorchuri.app.util.NetworkUtils;
import com.chorchuri.app.util.UiUtils;
import com.chorchuri.app.util.sharedpref.PrefKeys;
import com.chorchuri.app.util.sharedpref.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.chorchuri.app.network.APIConstants.Constants.SUCCESS;
import static com.chorchuri.app.network.APIConstants.Params.DATA;
import static com.chorchuri.app.network.APIConstants.Params.ERROR_MESSAGE;

public class PlayerActivity extends BaseActivity {

    /*public static final String VIDEO_URL = "videoUrl";
    public static final String VIDEO_SUBTITLE = "videoSubtitle";
    public static final String VIDEO_ID = "videoId";
    public static final String VIDEO_ELAPSED = "videoElapsed";
    public static final String IS_TRAILER_VIDEO = "isTrailerVideo";

    public static String[] resolutionKeys;
    public static String[] resolutionUrls;

    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @BindView(R.id.jwplayer)
    JWPlayerView jwplayer;

    private String videoUrl;
    private String videoSubtitle;
    private int currentResolution = 0;
    private boolean isVideoEnded = false;
    private int videoId;
    private int videoElapsed;
    PrefUtils prefUtils;
    APIInterface apiInterface;
    boolean isPlayNextVideo;

    private boolean firstPlay = true;
    private boolean isEnteredPipMode;
    int nextVideoId;
    List<Caption> captionTracks = new ArrayList<>();
    List<MediaSource> mediaSources = new ArrayList<>();
    boolean isTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        prefUtils = PrefUtils.getInstance(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (getIntent() != null) {
            videoId = getIntent().getIntExtra(PlayerActivity.VIDEO_ID, 0);
            videoUrl = getIntent().getStringExtra(PlayerActivity.VIDEO_URL);
            videoElapsed = getIntent().getIntExtra(PlayerActivity.VIDEO_ELAPSED, 0);
            videoSubtitle = getIntent().getStringExtra(PlayerActivity.VIDEO_SUBTITLE);
            isTrailer = getIntent().getBooleanExtra(PlayerActivity.IS_TRAILER_VIDEO, false);
            isPlayNextVideo = false;
        }

        jwPlayerListeners();
        loadVideoData(videoId);

//        prepareResolutionDialog();
    }

    public void jwPlayerListeners() {
        jwplayer.addOnCompleteListener(completeEvent -> {
            isVideoEnded = true;
            addVideoToHistory(videoId);
            onPPVCompleted(videoId);
            isPlayNextVideo = true;
            loadVideoData(nextVideoId);
            onBackPressed();
        });

        jwplayer.addOnBeforePlayListener(beforePlayEvent -> {
            if (firstPlay) {
                jwplayer.seek(videoElapsed * 1000);
                firstPlay = false;
            }
        });

        jwplayer.addOnErrorListener(errorEvent -> {
            if (NetworkUtils.isNetworkConnected(PlayerActivity.this)) {
                new AlertDialog.Builder(PlayerActivity.this, R.style.AppTheme_Dialog)
                        .setMessage(getString(R.string.cant_play_video))
                        .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                            dialogInterface.cancel();
                            finish();
                        })
                        .create().show();
            } else {
                UiUtils.showShortToast(PlayerActivity.this, getString(R.string.check_network));
            }
        });

        jwplayer.addOnPauseListener(pauseEvent -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                    || !isInPictureInPictureMode()) {
                if (isVideoEnded)
                    continueWatchingEnded();
                else
                    continueWatchingStorePos((int) jwplayer.getPosition());
            }
        });
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
    }

    private void seekToOldPositionAfterConfigChange() {
        if (videoElapsed != 0)
            jwplayer.seek(videoElapsed);
    }

    private void storeCurrentPositionBeforeConfigChange() {
        videoElapsed = (int) (jwplayer.getPosition() / 1000);
    }

    private void loadNewVideo(String newUrl) {
        videoUrl = newUrl;
        seekToOldPositionAfterConfigChange();
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
            if (jwplayer != null)
                jwplayer.play();
        }
    }

    @Override
    protected void onPause() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                || !isInPictureInPictureMode()) {
            if (jwplayer != null) {
                continueWatchingStorePos((int) jwplayer.getPosition());
                jwplayer.pause();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (jwplayer != null) {
            jwplayer.stop();
            //PlayerManager.getInstance().releaseCurrent();
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
            if (jwplayer != null)
                jwplayer.play();
        }
    }

    private void onPPVCompleted(int adminVideoId) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<String> call = apiInterface.ppvEnd(prefUtils.getIntValue(PrefKeys.USER_ID, 0), prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""), adminVideoId, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void continueWatchingStorePos(int millis) {
        videoElapsed = millis / 1000;
        UiUtils.log(TAG, "continueWatchingStorePos: ");
        if (PrefUtils.getInstance(this).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            Call<String> call = APIClient.getClient().create(APIInterface.class)
                    .continueWatchingStorePos(prefUtils.getIntValue(PrefKeys.USER_ID, 0), prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""), prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0),
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
                    .continueWatchingEnd(prefUtils.getIntValue(PrefKeys.USER_ID, 0), prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""), prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0),
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
            Call<String> call = apiInterface.addToHistory(prefUtils.getIntValue(PrefKeys.USER_ID, 0), prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""), prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0), adminVideoId, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
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
                    NetworkUtils.onApiError(PlayerActivity.this);
                }
            });
        }
    }

    private void loadVideoData(int adminVideoId) {
        Call<String> call = apiInterface.getVideoData(prefUtils.getIntValue(PrefKeys.USER_ID, 1), prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""),
                prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0), adminVideoId, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject videoResponse = null;
                try {
                    videoResponse = new JSONObject(response.body());

                if (videoResponse != null) {
                    if (videoResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        JSONObject data = videoResponse.optJSONObject(DATA);
                        nextVideoId = data.optInt(APIConstants.Params.NEXT_VIDEO_ID);
                        videoUrl = data.optString(APIConstants.Params.VIDEO_URL);
                        videoId = data.optInt(APIConstants.Params.ADMIN_VIDEO_ID);
                        getPlayerJsonFile(data.optString(APIConstants.Params.PLAYER_JSON));

//                        if (isPlayNextVideo)
//                            loadVideo();
                    } else {
                        UiUtils.showShortToast(PlayerActivity.this, videoResponse.optString(ERROR_MESSAGE));
                        finish();
                    }
                }  }catch (JSONException e){
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                finish();
                NetworkUtils.onApiError(PlayerActivity.this);
            }
        });
    }

    private void getPlayerJsonFile(String jsonFile) {
        Call<String> call = apiInterface.getPlayerJson(jsonFile);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject videoResponse = null;
                try {
                    videoResponse = new JSONObject(response.body());


                if (videoResponse != null) {
                    JSONArray captionArray = videoResponse.optJSONArray(!isTrailer ? "main_video_subtitles" : "trailer_video_subtitles");
                    if (captionArray != null) {
                        for (int i = 0; i < captionArray.length(); i++) {
                            JSONObject captionObject = captionArray.optJSONObject(i);
                            Caption captionEn = new Caption.Builder()
                                    .file(captionObject.optString("subtitle"))
                                    .label(captionObject.optString("title"))
                                    .isdefault(true)
                                    .build();
                            captionTracks.add(captionEn);
                        }
                    }

                    JSONArray mainVideoResolutions = videoResponse.optJSONArray(!isTrailer ? "main_video_resolutions" : "trailer_video_resolutions");
                    for (int i = 0; i < mainVideoResolutions.length(); i++) {
                        JSONObject mediaObject = mainVideoResolutions.optJSONObject(i);
                        mediaSources.add(new MediaSource(mediaObject.optString("video"), mediaObject.optString("title"), mediaObject.optInt(APIConstants.Params.IS_DEFAULT) == 1, null, null));
                    }
                    loadVideo();
                } else {
                    finish();
                }
                } catch (JSONException e){
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }  catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                finish();
                NetworkUtils.onApiError(PlayerActivity.this);
            }
        });
    }

    private void loadVideo() {
        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .sources(mediaSources)
                .tracks(captionTracks)
                .build();

        List<PlaylistItem> playlist = new ArrayList<>();
        playlist.add(playlistItem);

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        jwplayer.setup(config);
    }*/
}
