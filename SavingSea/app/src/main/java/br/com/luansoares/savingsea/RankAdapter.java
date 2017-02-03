package br.com.luansoares.savingsea;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luan on 20/01/2017.
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RecyclerViewHolder> {

        List<Rank> ranks;
        int posicao = 0;
        List<Drawable> imagens = new ArrayList<>();
        Context context;
        public RankAdapter(List<Rank> ranks,List<Drawable> imagens) {
            this.ranks = ranks;
            this.imagens = imagens;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_rank,parent,false);

            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(v);

            return recyclerViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            String nome = ranks.get(position).getNome();
            String pais = ranks.get(position).getPais();
            String pontos = ranks.get(position).getPontos();

            if(posicao == 5){
                posicao = 0;
                holder.mImg.setImageDrawable(imagens.get(posicao));
                posicao++;
            }else{
                holder.mImg.setImageDrawable(imagens.get(posicao));
                posicao++;
            }

            holder.mNome.setText(nome);
            holder.mPais.setText(pais);
            holder.mPonto.setText(pontos);
            holder.mPos.setText(((Integer)(position+1)).toString());

        }

        @Override
        public int getItemCount() {
            return ranks.size();
        }

        public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
            View myView;
            TextView mNome,mPais,mPonto,mPos;
            ImageView mImg;
            public RecyclerViewHolder(View itemView) {
                super(itemView);
                myView = itemView;
                mNome = (TextView)myView.findViewById(R.id.txtNome);
                mPais = (TextView)myView.findViewById(R.id.txtPais);
                mPonto = (TextView)myView.findViewById(R.id.txtPontos);
                mPos = (TextView)myView.findViewById(R.id.txtPos);
                mImg = (ImageView)myView.findViewById(R.id.imgRank);

            }
        }

}
