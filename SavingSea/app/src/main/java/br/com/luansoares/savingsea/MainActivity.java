package br.com.luansoares.savingsea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity{

    FragmentManager fragmentManager;
    private static Boolean confirmaFechar = false;
    private LinearLayout ln;
    private static Boolean marinheiro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ln = (LinearLayout)findViewById(R.id.menu_bottom);

        if(savedInstanceState != null){
            CharSequence fragmentoFechado = savedInstanceState.getCharSequence("nomeFragmento");
            switch (fragmentoFechado.toString()){
                case "Mapa":
                    replaceFragment(new Mapa());
                    break;
                case "DenunciaDetalhe":
                    replaceFragment(new DenunciaDetalhe());
                    break;
                default:
                    replaceFragment(new PerfilFragment());
                    break;
            }

        }else{
            replaceFragment(new PerfilFragment());
        }


    }

    public static Boolean getMarinheiro() {
        return marinheiro;
    }

    @Override
    public void onBackPressed() {

        int qtd = fragmentManager.getBackStackEntryCount();

        if(qtd == 0){
            if(confirmaFechar == false){
                Toast.makeText(this,"Aperte novamente para sair",Toast.LENGTH_LONG).show();
                confirmaFechar = true;
            }else{
                super.onBackPressed();
            }
        }else{
            fragmentManager.popBackStackImmediate();
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        String nomeFragmento = fragmentManager.findFragmentById(R.id.content_main).getClass().getSimpleName();
        outState.putCharSequence("nomeFragmento", nomeFragmento);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_marinheiro) {
            marinheiro = true;
            ln.setVisibility(View.GONE);
            replaceFragment(new MarinheiroFragment());
        }else if(id == R.id.action_jogo){
            marinheiro = false;
            ln.setVisibility(View.VISIBLE);
            replaceFragment(new PerfilFragment());
        }

        return super.onOptionsItemSelected(item);
    }


    public void abreFragment(View v){
        int id = v.getId();
        int count = ln.getChildCount();
        ImageView btn;
        for(int i=0; i<count; i++) {
            if(ln.getChildAt(i).getId() == id){
               btn = (ImageView) ln.getChildAt(i);
                btn.setBackgroundResource(R.color.colorPrimary);

                if (id == R.id.nav_mapa) {
                    btn.setImageResource(R.drawable.ic_place_white_24dp);
                }else if (id == R.id.nav_perfil) {
                    btn.setImageResource(R.drawable.ic_person_white_24dp);
                }else if(id == R.id.nav_sobre){
                    btn.setImageResource(R.drawable.ic_info_white_24dp);
                }

            }else{
                btn = (ImageView) ln.getChildAt(i);
                btn.setBackgroundResource(R.drawable.botao_redondo);

                if (ln.getChildAt(i).getId() == R.id.nav_mapa) {
                    btn.setImageResource(R.drawable.ic_place_black_24dp);
                }else if (ln.getChildAt(i).getId() == R.id.nav_perfil) {
                    btn.setImageResource(R.drawable.ic_person_black_24dp);
                }else if(ln.getChildAt(i).getId() == R.id.nav_sobre){
                    btn.setImageResource(R.drawable.ic_info_black_24dp);
                }
            }
        }

        if (id == R.id.nav_mapa) {
            replaceFragment(new Mapa());
        }else if (id == R.id.nav_perfil) {
            replaceFragment(new PerfilFragment());
        }else if(id == R.id.nav_sobre){
            replaceFragment(new InformacoesFragment());

        }
    }



    public void replaceFragment(Fragment frag) {
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        if (fragmentManager != null){
            FragmentTransaction t = fragmentManager.beginTransaction();
            t.replace(R.id.content_main, frag).commitAllowingStateLoss();

        }
    }
}
