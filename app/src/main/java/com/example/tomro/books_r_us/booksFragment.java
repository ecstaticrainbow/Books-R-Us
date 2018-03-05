package com.example.tomro.books_r_us;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class booksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Book> mDataSet = new ArrayList<>();
    private BookDao mBookDao;
    private AppDatabase mDb;

    public booksFragment() {
        // Required empty public constructor
    }

    public static booksFragment newInstance(String param1, String param2) {
        booksFragment fragment = new booksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_books, container, false);

        setupDb();
        setupRecycler(view);

        return view;
    }

    private void setupRecycler(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getContext(),2);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        // specify an adapter (see also next example)
    }

    private void setupDb() {
        mDb = Room.databaseBuilder(getContext(), AppDatabase.class, "Books-R-Us").fallbackToDestructiveMigration().build();
        mBookDao = mDb.bookDao();

        new booksFragment.getDb().execute();
    }

    private class getDb extends AsyncTask<Void, Void, List<Book>> {
        protected List<Book> doInBackground(Void ... params) {


            List<Book> bookList = mBookDao.getAll();

            return bookList;
        }

        protected void onPostExecute(List<Book> result) {
            for (Book book: result) {
                mDataSet.add(book);

            }
            addAdapter();
        }
    }

    private void addAdapter(){
        mAdapter = new CardAdapter(mDataSet, buttonListener, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private Button.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            onButtonPressed((Integer)v.getTag());
        }
    };

    public void onButtonPressed(int tag) {
        Log.d("test",Integer.toString(tag));
        Toast.makeText(getContext(), Integer.toString(tag),Toast.LENGTH_SHORT).show();
    }
}
