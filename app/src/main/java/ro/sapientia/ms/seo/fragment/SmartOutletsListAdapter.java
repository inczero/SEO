package ro.sapientia.ms.seo.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.model.SmartOutlet;

public class SmartOutletsListAdapter extends RecyclerView.Adapter<SmartOutletsListAdapter.SmartOutletViewHolder> {
    private List<SmartOutlet> mSmartOutlets;
    private ListItemClickListener mOnClickListener;

    SmartOutletsListAdapter(List<SmartOutlet> smartOutlets, ListItemClickListener listener) {
        mSmartOutlets = smartOutlets;
        mOnClickListener = listener;
    }

    @Override
    public void onBindViewHolder(SmartOutletsListAdapter.SmartOutletViewHolder holder, int position) {
        holder.name.setText(mSmartOutlets.get(position).getName());
        holder.identificationNumber.setText(mSmartOutlets.get(position).getIdentificationNumber());
        if (mSmartOutlets.get(position).getStatus()) {
            holder.status.setText(R.string.smart_outlet_status_turned_on);
        } else {
            holder.status.setText(R.string.smart_outlet_status_stand_by);
        }
        holder.productImage.setImageResource(R.drawable.ic_seo);
    }

    @NonNull
    @Override
    public SmartOutletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_smart_outlet_list_row, parent, false);
        return new SmartOutletViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mSmartOutlets.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class SmartOutletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView identificationNumber;
        private TextView status;
        private ImageView productImage;

        SmartOutletViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.smart_outlet_list_name_text_view);
            identificationNumber = v.findViewById(R.id.smart_outlet_list_id_text_view);
            status = v.findViewById(R.id.smart_outlet_list_status_text_view);
            productImage = v.findViewById(R.id.smart_outlet_list_image_view);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
