package team.loser.plantdiseasedetection.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.loser.plantdiseasedetection.Api.ApiServices;
import team.loser.plantdiseasedetection.Api.Constants;
import team.loser.plantdiseasedetection.HomeActivity;
import team.loser.plantdiseasedetection.R;
import team.loser.plantdiseasedetection.Sqlite.HistoryDatabase;
import team.loser.plantdiseasedetection.models.Disease;
import team.loser.plantdiseasedetection.models.DiseaseSolution;
import team.loser.plantdiseasedetection.utils.ImageUtil;
import team.loser.plantdiseasedetection.utils.RealPathUtil;

public class PredictionFragment extends Fragment {
    public static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 69;
    private static final int WRITE_EXTERNAL_REQUEST_CODE = 96;
    public static final String TAG = PredictionFragment.class.getName();
    private ImageButton btnTakePt,btnGallery;
    private Button btnPredict;
    private TextView tvDiseaseName, tvPercent;
    private ImageView imvDiseaseImg;
    private Uri mUri;
    private ProgressDialog mLoader;
    private File mFile;
    private String mCurrentPhotoPath = null;
    private LinearLayout layoutSolution;
    private String NameDisease = "";

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    mFile = null;
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData()!=null){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        mUri = uri;
                        String realPath = RealPathUtil.getRealPath(getActivity(), mUri);
                        mFile = new File(realPath);
                        try {
                            ContentResolver cr = getActivity().getContentResolver();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                            imvDiseaseImg.setImageBitmap(bitmap);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> mActivityResultLauncherCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    mFile = null;
                    if(result.getResultCode() == Activity.RESULT_OK ){
                            ImageUtil.galleryAddPic(getActivity(), mCurrentPhotoPath);
                            mFile = new File(mCurrentPhotoPath);
                            ImageUtil.setPic(imvDiseaseImg,mCurrentPhotoPath);
                    }
                }
            }
    );

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mActivityResultLauncherCamera.launch(takePictureIntent);
            }
        }
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select picture"));

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_prediction, container, false);
        setControls(view);
        setEvents();
        return view;
    }

    private void setEvents() {
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission(GALLERY_REQUEST_CODE);
            }
        });

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFile != null){
                    callApiSendImage();
                }
                else{
                    Toast.makeText(getContext(), "No image to predict",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnTakePt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission(CAMERA_REQUEST_CODE);
                onClickRequestPermission(WRITE_EXTERNAL_REQUEST_CODE);
            }
        });
    }
    private void callApiSolution() {
        mLoader.show();
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"), NameDisease);
        ApiServices.apiServices.getSolution(requestBodyName).enqueue(new Callback<DiseaseSolution>() {
            @Override
            public void onResponse(Call<DiseaseSolution> call, Response<DiseaseSolution> response) {
                mLoader.dismiss();
                DiseaseSolution diseaseInfo = response.body();
                if(diseaseInfo != null) {
//                    Toast.makeText(getContext(),"ddasdasda"+diseaseInfo.toString(), Toast.LENGTH_SHORT).show();
                    //TODO: intent => Solution Page
                    ((HomeActivity)getActivity()).ShowSolutionFragment(diseaseInfo);
                    }else {
                        if(layoutSolution.getChildCount() > 0){
                            layoutSolution.removeAllViews();
                        }
                    }

            }

            @Override
            public void onFailure(Call<DiseaseSolution> call, Throwable t) {
                mLoader.dismiss();
                Toast.makeText(getActivity(), "Call api failed",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void callApiSendImage() {
        mLoader.show();
        File file = mFile;
        RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part mulPartBodyImage = MultipartBody.Part.createFormData(Constants.KEY_IMAGE, file.getName(), requestBodyImage);
        ApiServices.apiServices.sendImage(mulPartBodyImage).enqueue(new Callback<Disease>() {
            @Override
            public void onResponse(Call<Disease> call, Response<Disease> response) {
                mLoader.dismiss();
                Disease diseaseInfo = response.body();
                if(diseaseInfo != null) {
                    String disease = diseaseInfo.getResponse_class();
                    NameDisease = disease;
                    tvDiseaseName.setText(disease);
                    String confident = diseaseInfo.getResponse_confident();
                    float c = Float.parseFloat(confident);
                    float roundDbl = (float) (Math.round(c*10000.0)/100.0);
                    tvPercent.setText(roundDbl + " %");
                    String timeStamp = diseaseInfo.getResponse_time();
                    //TODO: SQLite
                    Disease predicted = new Disease(disease, roundDbl+" %", timeStamp);
                    insertToPredictHistory(predicted);
                    if(!diseaseInfo.getResponse_class().toLowerCase(Locale.ROOT).contains("healthy")){
                        if(layoutSolution.getChildCount() == 0){
                            addButton();
                        }
                    }else {
                        if(layoutSolution.getChildCount() > 0){
                            layoutSolution.removeAllViews();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Disease> call, Throwable t) {
                mLoader.dismiss();
                Toast.makeText(getActivity(), "Call api failed",Toast.LENGTH_LONG).show();
            }
        });
    }
    //add to Sqlite
    private void insertToPredictHistory(Disease predicted) {
        HistoryDatabase.getInstance(getContext()).historyDAO().insertDisease(predicted);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }  else {
                    Toast.makeText(getContext(), "Can not open gallery", Toast.LENGTH_LONG).show();
                }
                return;

            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                    openCamera();
                }
                else
                {
                    Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void onClickRequestPermission(int requestCode) {
        switch (requestCode){
            case GALLERY_REQUEST_CODE:
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    openGallery();
                    return;
                }
                if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    openGallery();
                }
                else {
                    String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(getActivity(), permissions, GALLERY_REQUEST_CODE);
                }
                return;
            case CAMERA_REQUEST_CODE:
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    openCamera();
                    return;
                }
                if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    String [] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(getActivity(), permissions, CAMERA_REQUEST_CODE);
                }
                return;
        }

    }
    private void addButton(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Button btn_solution = new Button(getContext());
        btn_solution.setText(R.string.btn_solution);
        btn_solution.setId(R.id.btn_solution);
        btn_solution.setBackground(getActivity().getResources().getDrawable(R.drawable.custom_button));
        layoutSolution.addView(btn_solution,params);
        btn_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: solution
                if(mFile != null){
                    callApiSolution();

                }
                else {
                    Toast.makeText(getContext(), "No image to predict", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void setControls(View view) {
        //init Progress dialog
        mLoader = new ProgressDialog(getActivity());
        mLoader.setMessage("Please wait...");
        layoutSolution = view.findViewById(R.id.layout_solution);
        btnTakePt = view.findViewById(R.id.btn_take_photo);
        btnGallery = view.findViewById(R.id.btn_open_gallery);
        tvDiseaseName = view.findViewById(R.id.tv_disease_name);
        tvPercent = view.findViewById(R.id.tv_confident_percent);
        imvDiseaseImg = view.findViewById(R.id.img_disease);
        btnPredict = view.findViewById(R.id.btn_predict);
    }

}
