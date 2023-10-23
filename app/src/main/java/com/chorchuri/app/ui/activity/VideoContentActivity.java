package com.chorchuri.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import com.chorchuri.app.R;
import com.chorchuri.app.network.APIClient;
import com.chorchuri.app.network.APIInterface;
import com.chorchuri.app.ui.fragment.VideoContentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chorchuri.app.network.APIConstants.Params.CATEGORY;
import static com.chorchuri.app.network.APIConstants.Params.CATEGORY_ID;
import static com.chorchuri.app.network.APIConstants.Params.GENRE;
import static com.chorchuri.app.network.APIConstants.Params.GENRE_ID;
import static com.chorchuri.app.network.APIConstants.Params.SUB_CATEGORY;
import static com.chorchuri.app.network.APIConstants.Params.SUB_CATEGORY_ID;
import static com.chorchuri.app.network.APIConstants.Params.TITLE;
import static com.chorchuri.app.ui.activity.WebViewActivity.PAGE_TYPE;

public class VideoContentActivity extends BaseActivity {

    APIInterface apiInterface;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String pageType;
    int categoryId, subCategoryId, genreId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_content);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Intent caller = getIntent();
        if (caller != null) {
            pageType = caller.getStringExtra(PAGE_TYPE);
            //NO Break, because genre will surely
            switch (pageType) {
                case "GENERE":
                    genreId = caller.getIntExtra(GENRE_ID, -1);
                case "SUB_CATEGORY":
                    subCategoryId = caller.getIntExtra(SUB_CATEGORY_ID, -1);
                case "CATEGORY":
                    categoryId = caller.getIntExtra(CATEGORY_ID, -1);
            }
            String toolbarTitle = caller.getStringExtra(TITLE);
            toolbar.setTitle(toolbarTitle);
            setUpFragment();
        }
    }

    private void setUpFragment() {
        VideoContentFragment videoContentFragment = VideoContentFragment
                .getInstance(pageType, categoryId, subCategoryId, genreId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, videoContentFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
