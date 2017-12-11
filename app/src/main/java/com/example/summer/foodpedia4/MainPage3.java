package com.example.summer.foodpedia4;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


/**
 * Created by summer on 16/11/2017.
 */

public class MainPage3 extends Fragment implements Button.OnClickListener {
    final static int CAMERA_OUTPUT = 0;
    static final int REQUEST_TAKE_PHOTO = 11111;
    private ImageView mImageView;
    private ImageView mThumbnailImageView;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotosStorageReference;

    private Uri mdownloadUrl;
    private String murlstring;

    private String namestring;
    private String scorestring;
    private String pricestring;
    private String locationstring;
    private String typestring;

    private EditText inputname;
    private EditText inputscore;
    private EditText inputprice;
    private EditText inputlocation;
    private EditText inputtype;

    private boolean imagepass=false;
    private boolean textpass1=false;
    private boolean textpass2=false;
    private boolean textpass3=false;
    private boolean textpass4=false;
    private boolean textpass5=false;
    private Button firmupload;

    public MainPage3() {
        super();
    }

    public static MainPage3 newInstance(int sectionNumber) {
        MainPage3 fragment = new MainPage3();
        Bundle args = new Bundle();
        args.putInt("ARG_SECTION_NUMBER", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.mainpage3, container, false);
        mThumbnailImageView = (ImageView) rootView.findViewById(R.id.imageViewThumbnail);
        Button takePictureButton = (Button) rootView.findViewById(R.id.button);
        takePictureButton.setOnClickListener(this);

        mFirebaseStorage = FirebaseStorage.getInstance();
        mPhotosStorageReference = mFirebaseStorage.getReference().child("restaurant_photo");

        firmupload  = (Button) rootView.findViewById(R.id.firmupload);
        firmupload.setOnClickListener(new firmuploadlistener());

        //collect information of restaurant
        inputname = rootView.findViewById(R.id.inputname);
        inputscore = rootView.findViewById(R.id.inputscore);
        inputprice = rootView.findViewById(R.id.inputprice);
        inputlocation = rootView.findViewById(R.id.inputlocation);
        inputtype = rootView.findViewById(R.id.inputtype);


        firmupload.setEnabled(false);
        inputname.addTextChangedListener(new textchanger1());
        inputlocation.addTextChangedListener(new textchanger2());
        inputprice.addTextChangedListener(new textchanger3());
        inputscore.addTextChangedListener(new textchanger4());
        inputtype.addTextChangedListener(new textchanger5());

        return rootView;
    }


    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        CameraActivity activity = (CameraActivity) getActivity();
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imagepass= true;
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            //addphototoserver


            CameraActivity activity = (CameraActivity) getActivity();
            // Show the full sized image.
            setFullImageFromFilePath(activity.getCurrentPhotoPath(), mThumbnailImageView);
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Creates the image file to which the image must be saved.
     *
     * @return
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        CameraActivity activity = (CameraActivity) getActivity();
        activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    protected void addPhotoToGalleryandFirebase() {
        //add to gallery
        Toast.makeText(getActivity(), "正在上傳,請稍侯...",
                Toast.LENGTH_LONG).show();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        CameraActivity activity = (CameraActivity) getActivity();
        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);

        //upload image to firebase
        Uri selectedImageUri = mediaScanIntent.getData();
        StorageReference photoRef = mPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
        photoRef.putFile(selectedImageUri)
                .addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // When the image has successfully uploaded, we get its download URL
                        mdownloadUrl = taskSnapshot.getDownloadUrl();
                        murlstring = mdownloadUrl.toString();
                        Log.v("downloadurl", murlstring);


                        //upload url data to algolia
                        Client client = new Client("6NV5425YB8", "68b1b31b7917742ab0e6b118127ced02");
                        Index index = client.getIndex("restaurantlist");
                        try {
                            Toast.makeText(getActivity(), "感謝您!已成功上傳",
                                    Toast.LENGTH_LONG).show();
                            DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
                            dateFormatter.setLenient(false);
                            Date currentTime = Calendar.getInstance().getTime();
                            String s = dateFormatter.format(currentTime);
                            Long l =Long.parseLong(s);
                            Log.v("currenttime", l +"!");

                            namestring = inputname.getText().toString();
                            scorestring = inputscore.getText().toString();
                            pricestring = inputprice.getText().toString();
                            locationstring = inputlocation.getText().toString();


                            index.addObjectAsync(new JSONObject()
                                    .put("name", namestring)
                                    .put("price", pricestring)
                                    .put("score", scorestring)
                                    .put("type", typestring)
                                    .put("date", l)
                                    .put("location", locationstring)
                                    .put("image_path", murlstring ),null);

                            inputname.setText("");
                            inputscore.setText("");
                            inputprice.setText("");
                            inputlocation.setText("");
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(MainPage3.this).attach(MainPage3.this).commit();

                        } catch (JSONException e) {
                            Log.e("Care", "Error in pushing data", e);
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        dispatchTakePictureIntent();
    }

    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    public class firmuploadlistener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            firmupload.setClickable(false);
            addPhotoToGalleryandFirebase();
            Log.v("CARE","firmupload");
        }
    }

    public class textchanger1 implements TextWatcher{
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().trim().length()==0){
                textpass1 = false;
            }else{
                textpass1 = true;
            }
            if((imagepass==true)&&(textpass1==true)&&(textpass2==true)&&(textpass3==true)&&(textpass4==true)&&(textpass5==true)){
                firmupload.setEnabled(true);
                firmupload.setText("感謝你的資料!確認上傳");
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    public class textchanger2 implements TextWatcher{
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().trim().length()==0){
                textpass2 = false;
            }else{
                textpass2 = true;
            }
            if((imagepass==true)&&(textpass1==true)&&(textpass2==true)&&(textpass3==true)&&(textpass4==true)&&(textpass5==true)){
                firmupload.setEnabled(true);
                firmupload.setText("感謝你的資料!確認上傳");
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    public class textchanger3 implements TextWatcher{
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().trim().length()==0){
                textpass3 = false;
            }else{
                textpass3 = true;
            }
            if((imagepass==true)&&(textpass1==true)&&(textpass2==true)&&(textpass3==true)&&(textpass4==true)&&(textpass5==true)){
                firmupload.setEnabled(true);
                firmupload.setText("感謝你的資料!確認上傳");
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    public class textchanger4 implements TextWatcher{
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().trim().length()==0){
                textpass4 = false;
            }else{
                textpass4 = true;
            }
            if((imagepass==true)&&(textpass1==true)&&(textpass2==true)&&(textpass3==true)&&(textpass4==true)&&(textpass5==true)){
                firmupload.setEnabled(true);
                firmupload.setText("感謝你的資料!確認上傳");
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public class textchanger5 implements TextWatcher{
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().trim().length()==0){
                textpass5 = false;
            }else{
                textpass5 = true;
            }
            if((imagepass==true)&&(textpass1==true)&&(textpass2==true)&&(textpass3==true)&&(textpass4==true)&&(textpass5==true)){
                firmupload.setEnabled(true);
                firmupload.setText("感謝你的資料!確認上傳");
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }




}





