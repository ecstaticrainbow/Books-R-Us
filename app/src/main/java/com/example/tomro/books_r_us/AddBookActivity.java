package com.example.tomro.books_r_us;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.w3c.dom.Text;

public class AddBookActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView bookCover;
    private String imageurl;
    private BookDao mBookDao;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookCover = findViewById(R.id.bookCover);
        bookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(AddBookActivity.this);
                View promptsView = li.inflate(R.layout.input_dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
                builder.setView(promptsView);
                final EditText userInput = promptsView.findViewById(R.id.editText);
                // Add the buttons
                builder.setNeutralButton("Choose From Phone", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, 1);
                    }
                });
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        imageurl = userInput.getText().toString();
                        loadImage();
                    }
                });
                // Set other dialog properties
                builder.setTitle("Choose Image Source");
                builder.setMessage("Choose where you would like to load the image from");
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_book:
                new addBook().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();

            imageurl = imgDecodableString;
            loadImage();
        }
    }

    public void loadImage() {
        //Glide.with(this).load(imageurl).into(bookCover);
        Glide.with(this)
                .asBitmap()
                .load(imageurl)
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

                        Drawable pricebg = price.getBackground();
                        pricebg.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                        price.setBackground(pricebg);

                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(statuscolor);
                            window.setNavigationBarColor(statuscolor);
                        }

                        Spannable title = new SpannableString(getSupportActionBar().getTitle());
                        title.setSpan(new ForegroundColorSpan(vibrantSwatch.getTitleTextColor()), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        getSupportActionBar().setTitle(title);

                        //Spannable subtitle = new SpannableString(getSupportActionBar().getSubtitle());
                        //subtitle.setSpan(new ForegroundColorSpan(vibrantSwatch.getTitleTextColor()), 0, subtitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        //getSupportActionBar().setSubtitle(subtitle);

                        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
                        upArrow.setColorFilter(vibrantSwatch.getTitleTextColor(), PorterDuff.Mode.MULTIPLY);
                        getSupportActionBar().setHomeAsUpIndicator(upArrow);

                        price.setTextColor(vibrantSwatch.getBodyTextColor());
                        desc.setTextColor(vibrantSwatch.getBodyTextColor());
                        desc.setMovementMethod(new ScrollingMovementMethod());

                    }
                });
    }

    private class addBook extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Books-R-Us").fallbackToDestructiveMigration().build();
            mBookDao = mDb.bookDao();

            EditText desc = findViewById(R.id.txtDesc);
            EditText price = findViewById(R.id.txtPrice);
            mBookDao.insertOne(new Book("Test","Test",desc.getText().toString(), Double.parseDouble(price.getText().toString()), imageurl));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
            super.onPostExecute(aVoid);
        }
    }


}
