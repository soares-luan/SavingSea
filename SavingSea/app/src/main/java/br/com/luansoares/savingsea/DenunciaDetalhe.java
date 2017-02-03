package br.com.luansoares.savingsea;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

/**
 * Created by luan on 17/01/2017.
 */

public class DenunciaDetalhe extends Fragment {
    View v;

    ImageSwitcher imageSwitcher;
    ImageView img1,img2,img3;
    Button btnMap,btnConfirm,btnReport;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.denuncia_detalhe,container,false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Detalhes da Denúncia");

        imageSwitcher = (ImageSwitcher) v.findViewById(R.id.imageSwitcher);
        btnConfirm = (Button)v.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Obrigado por contribuir com o meio ambiente!",Toast.LENGTH_LONG).show();
            }
        });

        btnReport = (Button)v.findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Reportar Problema")
                        .setMessage("Viu alguma irregularidade e deseja reportar?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Report realizado com sucesso!", Toast.LENGTH_LONG).show();
                            }

                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });


        btnMap = (Button)v.findViewById(R.id.btnMaps);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=BR-050, 8211 - Casa Verde, São Paulo, Brasil");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getActivity().getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new
                        ImageSwitcher.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT));


                return myView;
            }
        });

        img1 = (ImageView)v.findViewById(R.id.imgProd1);
        img2 = (ImageView)v.findViewById(R.id.imgProd2);
        img3 = (ImageView)v.findViewById(R.id.imgProd3);

        img1.setImageDrawable(getResources().getDrawable(R.drawable.polui1));
        img2.setImageDrawable(getResources().getDrawable(R.drawable.polui2));
        img3.setImageDrawable(getResources().getDrawable(R.drawable.polui3));

        setaImage(img1.getDrawable());

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setaImage(img1.getDrawable());
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setaImage(img2.getDrawable());
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setaImage(img3.getDrawable());
            }
        });


        return v;
    }

    public void setaImage(Drawable imagem){
        imageSwitcher.setImageDrawable(imagem);
    }
}
