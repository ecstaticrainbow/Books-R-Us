package com.example.tomro.books_r_us;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link homeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Book> mDataSet = new ArrayList<>();
    private BookDao mBookDao;
    private AppDatabase mDb;


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


    }

    private void addAdapter(){
        mAdapter = new CardAdapter(mDataSet, buttonListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupDb() {
        mDb = Room.databaseBuilder(getContext(), AppDatabase.class, "database-name").fallbackToDestructiveMigration().build();
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




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int Id) {
        if (mListener != null) {
            mListener.onButtonPressed(Id);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onButtonPressed(int tag);
    }

    private Button.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            
            onButtonPressed((Integer)v.getTag());
        }
    };
}
