package br.com.titomilton.popularmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerAdapterViewHolder> {

    private Trailer[] mTrailersData;

    private final TrailersAdapterOnClickHandler mClickHandler;

    public interface TrailersAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public TrailersAdapter(TrailersAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public void setTrailersData(Trailer[] trailersData) {
        mTrailersData = trailersData;
        notifyDataSetChanged();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTrailerNameTextView;
        public final Context mContext;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerNameTextView = view.findViewById(R.id.tv_trailer_name);
            mContext = view.getContext();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailersData[adapterPosition];
            mClickHandler.onClick(trailer);
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        Trailer trailer = mTrailersData[position];
        holder.mTrailerNameTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return (mTrailersData == null ? 0 : mTrailersData.length);
    }


}
