package com.example.tomro.books_r_us;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
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


public class homeFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Book> mDataSet = new ArrayList<>();
    private BookDao mBookDao;
    private AppDatabase mDb;
    public static final String BookID = "com.example.myfirstapp.BookID";


    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment homeFragment.
     */
    public static homeFragment newInstance() {
        homeFragment fragment = new homeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupDb();
        setupRecycler(view);


        return view;
    }

    private void setupRecycler(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getContext(),2);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        // specify an adapter (see also next example)

        //mDataSet.add(new Book("Test","Test", "Test", 3.99, "Test"));
        //mDataSet.add(new Book("Test2","Test", "Test", 3.99, "Test"));
        //mDataSet.add(new Book("Test3","Test", "Test", 3.99, "Test"));
        //mDataSet.add(new Book("Test4","Test", "Test", 3.99, "Test"));
        //new insertBook().execute();
    }

    private void addAdapter(){
        mAdapter = new CardAdapter(mDataSet, buttonListener, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupDb() {
        mDb = Room.databaseBuilder(getContext(), AppDatabase.class, "Books-R-Us").fallbackToDestructiveMigration().build();
        mBookDao = mDb.bookDao();

        new getDb().execute();
    }

    private class getDb extends AsyncTask<Void, Void, List<Book>> {
        protected List<Book> doInBackground(Void ... params) {


            List<Book> bookList = mBookDao.getLatest();

            return bookList;
        }

        protected void onPostExecute(List<Book> result) {
            for (Book book: result) {
                mDataSet.add(book);

            }
            addAdapter();
        }
    }

    private class insertBook extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mBookDao.insertOne(new Book("Great Expectations","Charles Dickens", "Considered by many to be Dickens' finest novel, Great Expectations traces the growth of the book's narrator, Philip Pirrip (Pip), from a boy of shallow dreams to a man with depth of character. From its famous dramatic opening on the bleak Kentish marshes, the story abounds with some of Dickens' most memorable characters.", 3.99, "https://cdn2.penguin.com.au/covers/original/9780451531186.jpg"));
            return null;
        }
    }

    public void onButtonPressed(int tag) {
        Log.d("test",Integer.toString(tag));
        Toast.makeText(getContext(), Integer.toString(tag),Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getContext(), BookActivity.class);
        intent.putExtra("BookID", Integer.toString(tag));
        startActivity(intent);
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
}
