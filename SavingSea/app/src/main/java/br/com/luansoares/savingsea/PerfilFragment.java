package br.com.luansoares.savingsea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by luan on 17/01/2017.
 */

public class PerfilFragment extends Fragment {

    Button btnRank;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.perfil_fragment,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Meu Perfil");
        btnRank = (Button)v.findViewById(R.id.btnRank);

        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main,new RankFragment()).addToBackStack("rank").commit();
            }
        });

        return v;
    }

}
