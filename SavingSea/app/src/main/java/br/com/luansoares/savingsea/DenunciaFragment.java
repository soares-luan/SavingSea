package br.com.luansoares.savingsea;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

/**
 * Created by luan on 17/01/2017.
 */
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Arrays;

import br.com.luansoares.savingsea.R;
public class DenunciaFragment extends Fragment {
    private static final int CODE1 = 1;
    private static final int CODE2 = 2;
    ImageButton img1,img2;
    LinearLayout imagens;
    EditText lat,lng;
    Button btnCad;
    View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.denuncia_fragment,container,false);
        img1 = (ImageButton)v.findViewById(R.id.imageButton);
        img2 = (ImageButton)v.findViewById(R.id.imageButton2);
        imagens = (LinearLayout)v.findViewById(R.id.linearImagens);
        lat = (EditText)v.findViewById(R.id.edtLat);
        lng = (EditText)v.findViewById(R.id.edtLong);
        btnCad = (Button)v.findViewById(R.id.btnCadDenuncia);
        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Denúncia Registrada com Sucesso!",Toast.LENGTH_LONG).show();
                getActivity().getFragmentManager().popBackStack();
            }
        });

        Bundle bundle = this.getArguments();
        if(bundle != null){
            Double latitude = bundle.getDouble("latitude");
            Double longitude = bundle.getDouble("longitude");
            lat.setText(latitude.toString());
            lng.setText(longitude.toString());
            if(((MainActivity)getActivity()).getMarinheiro()){
                imagens.setVisibility(View.GONE);
            }else{
                lat.setEnabled(false);
                lng.setEnabled(false);
            }

        };

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
