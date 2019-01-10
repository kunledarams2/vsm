package com.kunledarams.alejoversionspackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kunledarams.alejoversionspackage.Model.BasicRegistration;
import com.kunledarams.alejoversionspackage.PreviewHolder.CameraPreview;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;

@SuppressWarnings("deprecation")
public class CaptureUmage extends AppCompatActivity {

    private Camera mCamer;
    private CameraPreview cameraPreview;
    Context mContxt;
    boolean frontCamera = false;
    private int CameraId = 0;
    private final static String TAG = "MakePhotoActivity";
    private ImageView imageCapturedImage;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private BasicRegistration basicRegistration;
    private String name;
    private String BasicStringId;
    private Uri uri = null;
    private ArrayList<String> myBasicList;
    private Bitmap bitmap = null;
    private Bitmap uriCompressed;
    private TextView textView;
    private Service service;
    private ProgressDialog dialog;
    private Button button, CaptureButton;
    private FrameLayout preview;
    private BasicRegistration BR;
    private MobileServiceTable<BasicRegistration> MSTRETABLE;
    private MobileServiceClient mClient;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String StorageURL = "";
    private static final String StorageCntainer = "alejoimage";
    private static final String BlobConnectiionString = "DefaultEndpointsProtocol=https;AccountName=alejoimage;AccountKey=sgHlrJjY7mJm63DqCmE/XOcHhlbOOKln7U/ynwxwFbXSv5EpSGh1FLmHg8Hd9bik5/Yqlttnl8Ohnnpx0PRSCA==;EndpointSuffix=core.windows.net";
    private CloudBlobClient blobClient;
    private CloudStorageAccount storageAccount;
    private CloudBlobContainer container;
    private StorageReference firebaseStorage;
    private   Uri imageUri=null;
    byte[] bytes;
    Bitmap compressedImage=null;
    byte[] thump_byte=null;
    LinearLayout myProgLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_umage);

        hasCameraHardware(this);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        imageCapturedImage = (ImageView) findViewById(R.id.imageCaptured);
        this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        textView = (TextView) findViewById(R.id.timer);
        button = (Button) findViewById(R.id.butNet);
        CaptureButton = (Button) findViewById(R.id.capImage);
        myProgLayout=(LinearLayout)findViewById(R.id.progLayout);
        myProgLayout.setVisibility(View.GONE);
        dialog = new ProgressDialog(this);

        mCamer = getCameraInstance();
        cameraPreview = new CameraPreview(this, mCamer);
        preview.addView(cameraPreview);

        basicRegistration = new BasicRegistration();
        myBasicList = (ArrayList<String>) getIntent().getSerializableExtra("basicList");

        mContxt = this;
        service = new Service();


        try {
            storageAccount = CloudStorageAccount.parse(BlobConnectiionString);
            blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(StorageCntainer);
            container.createIfNotExists();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }


        try {
            mClient = new MobileServiceClient("https://emalejo.azurewebsites.net", this);
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            MSTRETABLE = mClient.getTable(BasicRegistration.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        BR = new BasicRegistration();

//        FirebaseStorage mstorage = FirebaseStorage.getInstance();
       // firebaseStorage=mstorage.getReferenceFromUrl("gs://onlineselect-da239.appspot.com");
    }

    public static Camera getCameraInstance() {

        Camera c =null;

        try {

            int numofCamera= Camera.getNumberOfCameras();
            for(int i= 0 ; i< numofCamera; i++){
                Camera.CameraInfo info=new Camera.CameraInfo();
                Camera.getCameraInfo(i,info);
                if(info.facing==Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    c = Camera.open(i);
                }

            }
        }catch (Exception e){

        }
        return c;
    }

  /*  public void onResume(){
        super.onResume();

        if(!hasCameraHardware(this)){
            Toast.makeText(this,"Your doesnot have camera",Toast.LENGTH_LONG).show();
            finish();
        }else {
            if(mCamer!=null){

                int numofCamera= Camera.getNumberOfCameras();
                for(int i= 0 ; i< numofCamera; i++){
                    Camera.CameraInfo info=new Camera.CameraInfo();
                    Camera.getCameraInfo(i,info);
                    if(info.facing==Camera.CameraInfo.CAMERA_FACING_FRONT){
                        mCamer=Camera.open(i);
                    }else {
                        break;
                       // mCamer=Camera.open();
                    }

                }

            }

        }


    }*/


    private  boolean hasCameraHardware(Context context){
         if( context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
             return  true;
         }else {
             return false;
         }
    }


    public void CaptureImage(View view) {
        final int[] counter = {0};
        if(isSDCardAvailable()){
           new CountDownTimer(10000,1000) {
                @Override
                public void onTick(long l) {
                    textView.setText(String.valueOf(counter[0] + "  Smile We are Taking Your Picture " ));
                    counter[0]++;
                }

                @Override
                public void onFinish() {

                    mCamer.takePicture(null,null, mPicture);

                }
            }.start();


        }

    }

    private int findFrontfacingCamera(){
         int cameraid = 0;

        // searching for front camera
        int numOfcamera= Camera.getNumberOfCameras();
        for(int i=0; i<numOfcamera; i++){
            Camera.CameraInfo cameraInfo= new Camera.CameraInfo();

            Camera.getCameraInfo(i,cameraInfo);

            if(cameraInfo.facing== Camera.CameraInfo.CAMERA_FACING_FRONT){
                CameraId=i;
                frontCamera=true;
                break;

            }
        }
        return cameraid;
    }
    @SuppressWarnings("VisibleForTests")

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {

              bitmap= BitmapFactory.decodeByteArray(data,0,data.length);

            ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
            bytes=outputStream.toByteArray();




           if(bitmap!=null){
                // imageCapturedImage.setImageBitmap(bitmap);

               File file;
                File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
               //Environment.getExternalStorageDirectory().toString();


                // create file to save the image;
                file= new File(path,(System.currentTimeMillis() + myBasicList.get(2) +".jpg"));

                try {
                    OutputStream stream= null;
                    stream= new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
                    stream.flush();
                    stream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // imageUri.fromFile(file);
                imageUri= Uri.parse(file.getAbsolutePath());
               try{
                    compressedImage= new Compressor(CaptureUmage.this)
                           .setMaxHeight(200)
                           .setMaxWidth(200)
                           .setQuality(50)
                           .compressToBitmap(file);
               }catch (Exception e){
                   e.printStackTrace();
               }
               ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
               compressedImage.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
               thump_byte =byteArrayOutputStream.toByteArray();

                //imageCapturedImage.setImageURI(imageUri);
                // String tv= bitString(bitmap);
               if(isOnline()){
                   myProgLayout.setVisibility(View.VISIBLE);


                   final StorageReference filePath =  firebaseStorage.child("AlejoImage").child(imageUri.getLastPathSegment());
                   filePath.putBytes(thump_byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Uri downloadUri = taskSnapshot.getDownloadUrl();

                           BR.setVistorImage(downloadUri.toString());
                           myBasicList.add(downloadUri.toString()); //7

                           Glide.with(CaptureUmage.this)
                                   .load(downloadUri)
                                   .diskCacheStrategy(DiskCacheStrategy.ALL)
                                   .placeholder(R.drawable.progress)
                                   .into(imageCapturedImage);

                           myProgLayout.setVisibility(View.GONE);
                           CaptureButton.setVisibility(View.GONE);
                           preview.setVisibility(View.GONE);
                           button.setVisibility(View.VISIBLE);
                           textView.setText("Nice Picture");
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(CaptureUmage.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
                       }
                   });

               }else {
                   Toast.makeText(CaptureUmage.this,"Please Check Your Internet",Toast.LENGTH_LONG).show();
               }

            }else {
                Toast.makeText(CaptureUmage.this, "Error occur",Toast.LENGTH_LONG).show();
            }

         /*   new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog= ProgressDialog.show(CaptureUmage.this,"Please Wait","",false,false);
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
                     bytes=outputStream.toByteArray();



                    return null;
                }

                @Override
                protected void onPostExecute(Void s) {
                    super.onPostExecute(s);

                    dialog.dismiss();
                }
            }.execute();*/


        }
    };

    private static String bitString(Bitmap bitmap){

        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
        byte[]byteArry= outputStream.toByteArray();
        return Base64.encodeToString(byteArry,Base64.DEFAULT);
    }
    // take picture and save to the folder

    @Override
    protected void onStop() {
        if(mCamer!=null){
            mCamer.release();
            mCamer=null;
        }
        super.onStop();
    }

    private boolean isSDCardAvailable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return  true;
        }
        return false;
    }


    public void NicePicture(View view) {

        if(myBasicList.size()>0){
            BR.setVistorGender(myBasicList.get(0));
            BR.setVisitorName(myBasicList.get(2));
            BR.setVisitorPhone(myBasicList.get(3));
            BR.setVisitorEmail(myBasicList.get(4));
            BR.setVisitorAddress(myBasicList.get(5));
            BR.setVisitorCompany(myBasicList.get(6));
        }
        if(isOnline()){
            AsyncTask<Void, Void, Void>task= new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    dialog=ProgressDialog.show(CaptureUmage.this,"Uploading Image", "Please Wait...It will Take Some Seconds",false,false);

                }

                @Override
                protected Void doInBackground(Void... voids) {

                    try {

                        MSTRETABLE.insert(BR).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                    dialog.dismiss();
                    cameraPreview.RefreshCamera(mCamer);
                    Intent okayPicture= new Intent(CaptureUmage.this, Signature.class);
                    okayPicture.putStringArrayListExtra("basicInfro",(ArrayList<String>)myBasicList);
                    //Toast.makeText(getApplicationContext(),imageString,Toast.LENGTH_LONG).show();
                    startActivity(okayPicture);

                    finish();

                }

            };
            service.runAsyncTask(task);
        }else {
            Toast.makeText(CaptureUmage.this,"Check Your Internet",Toast.LENGTH_LONG).show();
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


}
