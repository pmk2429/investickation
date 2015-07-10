package investickations.com.sfsu.investickation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import investickations.com.sfsu.entities.AppConfig;


public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int ROW_TYPE = 1;
    private static Context context;
    private List<String> rows;

    protected static final int NAVDRAWER_ITEM_MAIN = 1;
    protected static final int NAVDRAWER_ITEM_ACTIVITIES = 2;
    protected static final int NAVDRAWER_ITEM_OBSERVATIONS = 3;
    protected static final int NAVDRAWER_ITEM_SETTINGS = 4;
    protected static final int NAVDRAWER_ITEM_GUIDE = 5;
    protected static final int NAVDRAWER_ITEM_SIGN_IN = 6;

    public DrawerAdapter(List<String> rows, Context passedContext) {
        this.rows = rows;
        context = passedContext;
    }

    public DrawerAdapter() {
        // needed
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            return new ViewHolder(view, viewType, context);
        } else if (viewType == ROW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
            return new ViewHolder(view, viewType, context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == ROW_TYPE) {
            String rowText = rows.get(position - 1);
            holder.textView.setText(rowText);
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        return ROW_TYPE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected int viewType;
        Context context;

        @InjectView(R.id.drawer_row_icon)
        ImageView imageView;
        @InjectView(R.id.drawer_row_text)
        TextView textView;

        public ViewHolder(View itemView, int viewType, Context context) {
            super(itemView);
            this.context = context;
            this.viewType = viewType;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            if (viewType == ROW_TYPE) {
                ButterKnife.inject(this, itemView);
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "The Item Clicked is: " + getPosition(), Toast.LENGTH_SHORT).show();
            DrawerAdapter.goToNavDrawerItem(getPosition());
            //DrawerAdapter.demoMethod();
        }
    }

    /*
    private static void demoMethod() {
        Toast.makeText(context, "The Item Clicked is in Demo ", Toast.LENGTH_SHORT).show();
        Log.d(AppConfig.LOGSTRING, "Method works");
    }*/

    // this method will handle the onClick event calls
    private static void goToNavDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case NAVDRAWER_ITEM_MAIN:
                intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
                break;

            case NAVDRAWER_ITEM_ACTIVITIES:
                intent = new Intent(context, UserActivityMainActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
                break;

            case NAVDRAWER_ITEM_OBSERVATIONS:
                intent = new Intent(context, ObservationMainActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
                break;

            case NAVDRAWER_ITEM_GUIDE:
                intent = new Intent(context, GuideMasterActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
                break;

            case NAVDRAWER_ITEM_SETTINGS:
                intent = new Intent(context, PersonalInfoActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
                break;
        }
    }
}