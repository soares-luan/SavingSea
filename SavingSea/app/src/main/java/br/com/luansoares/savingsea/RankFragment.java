package br.com.luansoares.savingsea;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luan on 20/01/2017.
 */

public class RankFragment extends Fragment {
    View v;
    RecyclerView baseLista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.rank_fragment,container,false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Rank");

        baseLista = (RecyclerView)v.findViewById(R.id.base_lista);
        baseLista.setHasFixedSize(true);
        baseLista.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Rank> lista = new ArrayList<>();

        lista.add(new Rank("Josh","Aus","5644"));
        lista.add(new Rank("Marcos","Bra","4758"));
        lista.add(new Rank("Steve","Usa","4522"));
        lista.add(new Rank("Julio","Bra","3432"));
        lista.add(new Rank("Luan","Bra","3222"));
        lista.add(new Rank("Mariano","Bra","2344"));
        lista.add(new Rank("Michael","Bra","1234"));



        List<Drawable> imagens = new ArrayList<>();
        imagens.add(getActivity().getResources().getDrawable(R.drawable.img1));
        imagens.add(getActivity().getResources().getDrawable(R.drawable.img2));
        imagens.add(getActivity().getResources().getDrawable(R.drawable.img3));
        imagens.add(getActivity().getResources().getDrawable(R.drawable.img4));
        imagens.add(getActivity().getResources().getDrawable(R.drawable.img5));
        imagens.add(getActivity().getResources().getDrawable(R.drawable.img6));


        RankAdapter adapter = new RankAdapter(lista,imagens);
        baseLista.setAdapter(adapter);


        return v;
    }


}
