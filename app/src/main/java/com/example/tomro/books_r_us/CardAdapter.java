package com.example.tomro.books_r_us;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EventListener;
import java.util.List;

/**
 * Created by tomro on 08/12/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Book> mDataset;
    private View.OnClickListener eventListener;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mCardView;
        public ViewHolder(View v) {
            super(v);
            mCardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(List<Book> myDataset, View.OnClickListener eventListener, Context context) {
        this.mDataset = myDataset;
        this.eventListener = eventListener;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView bookTitle = holder.mCardView.findViewById(R.id.bookTitle);
        bookTitle.setText(mDataset.get(position).getBookTitle());

        ImageView bookImage = holder.mCardView.findViewById(R.id.bookImage);
        Glide.with(context).load(mDataset.get(position).getBookImageUrl()).into(bookImage);

        ImageButton button = holder.mCardView.findViewById(R.id.btnSeeMore);
        button.setTag(mDataset.get(position).getUid());
        button.setOnClickListener(eventListener);


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
