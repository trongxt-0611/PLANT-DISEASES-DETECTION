package team.loser.plantdiseasedetection;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import team.loser.plantdiseasedetection.models.Disease;
import team.loser.plantdiseasedetection.utils.RealPathUtil;

public class HomeActivity extends AppCompatActivity {
    public static final int CAMERA_ACTION_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 69;
    public static final String TAG = MainActivity.class.getName();
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
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null) return;

                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imvDiseaseImg.setImageBitmap(bitmap);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        setControls();
        //init Progress dialog
        mLoader = new ProgressDialog(this);
        mLoader.setMessage("Please wait...");
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUri != null){
                    callApiSendImage();
                }
            }
        });
    }

    private void callApiSendImage() {
        mLoader.show();
        String realPath = RealPathUtil.getRealPath(this, mUri);
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
                Toast.makeText(HomeActivity.this, "Call api failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GALLERY_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else {
            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, GALLERY_REQUEST_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select picture"));
    }

    private void setControls() {
        btnTakePt = findViewById(R.id.btn_take_photo);
        btnGallery = findViewById(R.id.btn_open_gallery);
        tvDiseaseName = findViewById(R.id.tv_disease_name);
        tvPercent = findViewById(R.id.tv_confident_percent);
        imvDiseaseImg = findViewById(R.id.img_disease);
        btnPredict = findViewById(R.id.btn_predict);
    }
}