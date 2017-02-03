package br.com.luansoares.savingsea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;

/**
 * Created by luan on 20/01/2017.
 */

public class DenunciaBonus extends Fragment {
    View v;

    Button btnCad;
    ImageButton img1,img2;
    private static final int CODE1 = 1;
    private static final int CODE2 = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.denuncia_bonus,container,false);
        btnCad = (Button)v.findViewById(R.id.btnCadDenuncia);
        img1 = (ImageButton)v.findViewById(R.id.imageButton);
        img2 = (ImageButton)v.findViewById(R.id.imageButton2);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirImg(CODE1);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirImg(CODE2);
            }
        });

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                Toast.makeText(getActivity(), "Obrigado por participar, seus pontos serão creditados em breve", Toast.LENGTH_SHORT).show();
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return v;
    }

    public void subirImg(int code){
        Intent galery_intent = new Intent();
        galery_intent.setAction(Intent.ACTION_GET_CONTENT);
        galery_intent.setType("image/*");
        startActivityForResult(galery_intent,code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == CODE1 || requestCode == CODE2) && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            Intent intent = CropImage.activity(imageUri)
                    .getIntent(getActivity().getApplicationContext());
            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE+requestCode);
        }
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE+CODE1:
                    img1.setImageURI(pegaUri(data));
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE+CODE2:
                    img2.setImageURI(pegaUri(data));
                    break;
            }
        }

    }

    public Uri pegaUri(Intent data){
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        return result.getUri();
    }
}
