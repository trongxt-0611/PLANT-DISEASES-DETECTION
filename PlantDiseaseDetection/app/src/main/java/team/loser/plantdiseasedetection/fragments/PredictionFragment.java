package team.loser.plantdiseasedetection.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.loser.plantdiseasedetection.Api.ApiServices;
import team.loser.plantdiseasedetection.Api.Constants;
import team.loser.plantdiseasedetection.R;
import team.loser.plantdiseasedetection.models.Disease;
import team.loser.plantdiseasedetection.utils.RealPathUtil;

public class PredictionFragment extends Fragment {
    public static final int CAMERA_ACTION_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 69;
    public static final String TAG = PredictionFragment.class.getName();
    private ImageButton btnTakePt,btnGallery;
    private Button btnPredict;
    private TextView tvDiseaseName, tvPercent;
    private ImageView imvDiseaseImg;
    private Uri mUri;
    private ProgressDialog mLoader;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData()!=null){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        mUri = uri;
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
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData()!=null){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        mUri = uri;
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imvDiseaseImg.setImageBitmap(imageBitmap);
                    }
                }
            }
    );

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
                if(mUri != null){
                    callApiSendImage();
                }
                else{
                    Toast.makeText(getContext(), "dasdasda",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnTakePt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission(CAMERA_ACTION_CODE);
            }
        });
    }
    private void callApiSendImage() {
        mLoader.show();
        String realPath = RealPathUtil.getRealPath(getActivity(), mUri);
        Log.e("callApi", realPath);
        File file = new File(realPath);
        RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part mulPartBodyImage = MultipartBody.Part.createFormData(Constants.KEY_IMAGE, file.getName(), requestBodyImage);
        ApiServices.apiServices.sendImage(mulPartBodyImage).enqueue(new Callback<Disease>() {
            @Override
            public void onResponse(Call<Disease> call, Response<Disease> response) {
                mLoader.dismiss();
                Disease diseaseInfo = response.body();
                if(diseaseInfo != null) {
                    tvDiseaseName.setText(diseaseInfo.getResponse_class());
                    tvPercent.setText(diseaseInfo.getResponse_confident());
                }
            }

            @Override
            public void onFailure(Call<Disease> call, Throwable t) {
                mLoader.dismiss();
                Toast.makeText(getActivity(), "Call api failed",Toast.LENGTH_LONG).show();
            }
        });
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

            case CAMERA_ACTION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                    openCamera();
                }
                else
                {
                    Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
                }
        }
//        if(requestCode == GALLERY_REQUEST_CODE){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                openGallery();
//            }
//        }
    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            mActivityResultLauncherCamera.launch(Intent.createChooser(intent,"Take a photo"));
        }
        else{
            Toast.makeText(getContext(), "can not open camera", Toast.LENGTH_LONG).show();
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
            case CAMERA_ACTION_CODE:
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    openCamera();
                    return;
                }
                if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    String [] permissions = {Manifest.permission.CAMERA};
                    ActivityCompat.requestPermissions(getActivity(), permissions, CAMERA_ACTION_CODE);
                }
                return;
        }

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select picture"));

    }

    private void setControls(View view) {
        //init Progress dialog
        mLoader = new ProgressDialog(getActivity());
        mLoader.setMessage("Please wait...");
        btnTakePt = view.findViewById(R.id.btn_take_photo);
        btnGallery = view.findViewById(R.id.btn_open_gallery);
        tvDiseaseName = view.findViewById(R.id.tv_disease_name);
        tvPercent = view.findViewById(R.id.tv_confident_percent);
        imvDiseaseImg = view.findViewById(R.id.img_disease);
        btnPredict = view.findViewById(R.id.btn_predict);
    }
}
