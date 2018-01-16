package br.com.titomilton.popularmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewAdapterViewHolder> {

    private Review[] mReviewsData;

    public ReviewsAdapter() {

    }

    public void setReviewsData(Review[] reviewsData) {
        mReviewsData = reviewsData;
        notifyDataSetChanged();
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.review_list_item, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        Review review = mReviewsData[position];
        holder.mAuthorTextView.setText(review.getAuthor());
        holder.mContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return (mReviewsData == null ? 0 : mReviewsData.length);
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mAuthorTextView;
        public final TextView mContentTextView;
        public final Context mContext;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = view.findViewById(R.id.tv_review_author);
            mContentTextView = view.findViewById(R.id.tv_review_content);
            mContext = view.getContext();
        }

    }


}
