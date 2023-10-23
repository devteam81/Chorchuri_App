package com.chorchuri.app.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chorchuri.app.R;
import com.chorchuri.app.listener.OnLoadMoreVideosListener;
import com.chorchuri.app.model.SubscriptionPlan;
import com.chorchuri.app.network.APIClient;
import com.chorchuri.app.network.APIInterface;
import com.chorchuri.app.ui.activity.PlansActivity;
import com.chorchuri.app.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Discounted_priceAdapter extends RecyclerView.Adapter<Discounted_priceAdapter.ViewHolder> {

    private final String TAG = PlanAdapter.class.getSimpleName();
    public static int position;

    private PlansActivity context;
    private ArrayList<SubscriptionPlan> discountList;
    private LayoutInflater inflater;
    private OnLoadMoreVideosListener listener;
    APIInterface apiInterface;
    public static int selectedposition = 0;
    public static SubscriptionPlan discountplan;

    public Discounted_priceAdapter(PlansActivity plansActivity, OnLoadMoreVideosListener listener, ArrayList<SubscriptionPlan> discountplans) {
        this.context = plansActivity;
        this.discountList = discountplans;
        this.listener = listener;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Discounted_priceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewHolder, int viewType) {
        UiUtils.log(TAG, "Position: " + viewType);
        View view = inflater.inflate(R.layout.discounted_price, viewHolder, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Discounted_priceAdapter.ViewHolder viewHolder, int i) {
        Animation scale = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.scale_zoomin_zoomout);
        viewHolder.llDcMonth.setText(discountList.get(i).getTitle());
        viewHolder.dcMonth.setText(String.valueOf(discountList.get(i).getMonths()));
        viewHolder.nonDc_price.setText(String.valueOf(discountList.get(i).getAmount()));
        viewHolder.nonDc_price.setPaintFlags(viewHolder.nonDc_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.dcPrice.setText(String.valueOf(discountList.get(i).getPercentage()));
        viewHolder.dcPercentage.setText(String.valueOf(discountList.get(i).getDiscount()));
        viewHolder.discount_Anim.startAnimation(scale);
        viewHolder.dcRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedposition = i;
                PlansActivity.plan = discountList.get(i);
                notifyDataSetChanged();
                context.showAgePicker();
            }
        });

    }

    public void showLoading() {
        if (listener != null)
            listener.onLoadMore(discountList.size());
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dcMonth)
        TextView dcMonth;
        @BindView(R.id.llDcMonth)
        TextView llDcMonth;
        @BindView(R.id.dcOriginalPrice_Currency)
        TextView dcOriginalPrice_Currency;
        @BindView(R.id.discountPrice_Currency)
        TextView discountPrice_Currency;
        @BindView(R.id.dcPercentage)
        TextView dcPercentage;
        @BindView(R.id.nonDc_price)
        TextView nonDc_price;
        @BindView(R.id.dcPrice)
        TextView dcPrice;
        @BindView(R.id.dcRenew)
        Button dcRenew;
        @BindView(R.id.discount_Layout)
        RelativeLayout discount_Layout;
        @BindView(R.id.discount_Anim)
        RelativeLayout discount_Anim;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
