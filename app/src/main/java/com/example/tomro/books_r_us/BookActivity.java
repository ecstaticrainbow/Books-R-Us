package com.example.tomro.books_r_us;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;


public class BookActivity extends AppCompatActivity {

    private String BookID;
    private BookDao mBookDao;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent = getIntent();
        BookID = intent.getStringExtra("BookID");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupDb();




    }

    public void changeBgColour(final Book book) {
        final ImageView bookCover = findViewById(R.id.bookCover);
        Glide.with(this)
                .asBitmap()
                .load(book.getBookImageUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        bookCover.setImageBitmap(resource);
                        Palette palette = Palette.from(resource).maximumColorCount(32).generate();

                        ConstraintLayout constraintLayout = findViewById(R.id.InfoContainer);
                        TextView price = findViewById(R.id.txtPrice);
                        TextView desc = findViewById(R.id.txtDesc);

                        Palette.Swatch vibrantSwatch = palette.getLightVibrantSwatch();
                        //int color = palette.getLightVibrantColor(0);
                        int color = vibrantSwatch.getRgb();

                        float[] hsv = new float[3];
                        Color.colorToHSV(color, hsv);
                        hsv[2] *= 0.8f;
                        int statuscolor = Color.HSVToColor(hsv);
                        //int statuscolor = palette.getVibrantColor(0);

                        constraintLayout.setBackgroundColor(color);
                        price.setBackgroundColor(color);

                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(statuscolor);
                            window.setNavigationBarColor(statuscolor);
                        }

                        Spannable title = new SpannableString(book.getBookTitle());
                        title.setSpan(new ForegroundColorSpan(vibrantSwatch.getTitleTextColor()), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        getSupportActionBar().setTitle(title);

                        Spannable subtitle = new SpannableString(book.getBookAuthor());
                        subtitle.setSpan(new ForegroundColorSpan(vibrantSwatch.getTitleTextColor()), 0, subtitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        getSupportActionBar().setSubtitle(subtitle);

                        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
                        upArrow.setColorFilter(vibrantSwatch.getTitleTextColor(), PorterDuff.Mode.MULTIPLY);
                        getSupportActionBar().setHomeAsUpIndicator(upArrow);

                        price.setTextColor(vibrantSwatch.getBodyTextColor());
                        desc.setTextColor(vibrantSwatch.getBodyTextColor());

                    }
                });
    }

    private void setupDb() {
        mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Books-R-Us").fallbackToDestructiveMigration().build();
        mBookDao = mDb.bookDao();

        new getDb().execute();
    }

    private class getDb extends AsyncTask<Void, Void, Book> {
        protected Book doInBackground(Void ... params) {

            Book book = mBookDao.findByID(Integer.parseInt(BookID));

           return book;
        }

        protected void onPostExecute(Book book) {



            TextView price = findViewById(R.id.txtPrice);
            TextView desc = findViewById(R.id.txtDesc);

            price.setText("£" + String.valueOf(book.getBookPrice()));
            desc.setText(book.getBookDesc());

            changeBgColour(book);

        }

    }
}
