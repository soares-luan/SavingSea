package br.com.luansoares.savingsea;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luan on 17/01/2017.
 */

public class Mapa extends Fragment implements OnMapReadyCallback, LocationListener {


    private static final int PERMISSION_ACCESS_FINE_LOCATION = 123;
    private View myView;
    private GoogleMap mMap;
    private PolygonOptions polygonOptions;
    private Polygon polygon;
    private Boolean gps_enabled = false;
    private Location gps_loc = null;
    private Boolean temMarcador = false;
    private LatLng locAtual;
    private Boolean acessoGPS = false;
    private LocationManager locationManager;
    private FragmentManager fragmentManager;
    private ProgressDialog progressDialog;
    private FloatingActionButton fab;
    private Button btnCancelar, btnAddRest;
    private RelativeLayout ln;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mapa");

        fragmentManager = getFragmentManager();
        if (myView != null) {
            ViewGroup parent = (ViewGroup) myView.getParent();
            if (parent != null)
                parent.removeView(myView);
        }

        try {
            myView = inflater.inflate(R.layout.mapa, container, false);

            fab = (FloatingActionButton) myView.findViewById(R.id.fabAddMarker);
            ln = (RelativeLayout) myView.findViewById(R.id.linearCadRest);
            btnCancelar = (Button) myView.findViewById(R.id.btnCancelar);
            btnAddRest = (Button) myView.findViewById(R.id.btnAddRest);
            polygonOptions = new PolygonOptions();

        } catch (InflateException e) {
        /* map is already there, just return view as it is */
            Log.e("Erro Mapa", e.getMessage());
        }


        progressDialog = new ProgressDialog(getActivity());

        return myView;
    }

    private MapFragment getMapFragment() {
        FragmentManager fm = null;


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fm = getFragmentManager();
        } else {
            fm = getChildFragmentManager();
        }

        return (MapFragment) fm.findFragmentById(R.id.map);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment mapFragment = getMapFragment();
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    acessoGPS = true;
                    locAtual = new LatLng(gps_loc.getLatitude(), gps_loc.getLongitude());

                } else {
                    locAtual = new LatLng(-21.962066, -48.037398);
                }

                montaMapa();
                return;
            }

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if (Build.VERSION.SDK_INT > 19) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_ACCESS_FINE_LOCATION);
                }


                return;
            }
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }
        if (gps_loc == null) {
            locAtual = new LatLng(-23.5142777, -46.6419798);
        } else {
            acessoGPS = true;
            locAtual = new LatLng(gps_loc.getLatitude(), gps_loc.getLongitude());
        }

        montaMapa();
    }

    private void montaMapa() {
        List<Denuncia> denuncias = new ArrayList<>();
        Denuncia denuncia = new Denuncia("poluicao","Despejo de lixo orgânico",-23.516589, -46.653596);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Esgoto a céu aberto",-23.671359, -46.727480);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca Ilegal",-23.759687, -46.632860);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Evento Bonus",-23.735120, -46.455525);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","População jogando lixo no rio",-23.51291802775549, -46.65099620819092);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pescadores com rede",-23.527634886699154, -46.63876533508301);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Lixo hospitalar sendo jogando na nascente",-23.52007991328393, -46.67584419250488);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pescadores avistados",-23.506542834950032, -46.67841911315918);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Esgoto a céu aberto",-23.499144076254908, -46.65112495422363);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca em local proibido",-23.548880923858746, -46.6834831237793);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água",-23.55769308756573, -46.63026809692383);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal",-23.496310399071604, -46.711978912353516);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água",-23.501662849264328, -46.750431060791016);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal",-23.555175386794573, -46.74030303955078);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","População jogando lixo no rio",-23.547621995102734, -46.54735565185547);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pescadores com rede",-23.48969824871362, -46.57756805419922);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Lixo hospitalar sendo jogando na nascente",-23.40654580499728, -46.60194396972656);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pescadores avistados",-23.48969824871362, -46.47422790527344);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Esgoto a céu aberto",-23.40654580499728, -46.43302917480469);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca em local proibido",-23.649556122147732, -46.742706298828125);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água",-23.621878148536513, -46.536712646484375);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal",-23.50386673615084, -46.6124153137207);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Lixo hospitalar sendo jogando na nascente",-23.52244088905899, -46.59456253051758);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pescadores avistados",-23.51992251339335, -46.717472076416016);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Esgoto a céu aberto",-23.52338526751212, -46.7936897277832);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca em local proibido",-23.51693187971685, -46.82124137878418);
        denuncias.add(denuncia);

        denuncia = new Denuncia("bonus","Evento Bonus",-23.524093546905437, -46.63597583770752);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Evento Bonus",-23.51339025198365, -46.656060218811035);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Evento Bonus",-23.528185753213577, -46.66464328765869);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Evento Bonus",-23.490485426862573, -46.59001350402832);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Evento Bonus",-23.523227871573784, -46.61215782165527);
        denuncias.add(denuncia);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geraMarcador(mMap.getCameraPosition().target);
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locAtual, 16));

        setaMarcadores(denuncias, mMap);
    }

    private void setaMarcadores(List<Denuncia> denuncias, final GoogleMap mMap) {
        int i = 0;

        for (Denuncia denuncia : denuncias) {
            int icone = 0;
            switch (denuncia.getTipo()){
                case "pesca":
                    icone = R.drawable.ic_fish;
                    break;
                case "poluicao":
                    icone = R.drawable.ic_delete;
                    break;
                case "bonus":
                    icone = R.drawable.ic_star_rate_black_18dp;
                    break;
            }

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(denuncia.getLatitude(),denuncia.getLongitude()))
                    .title(denuncia.getDescricao())
                    .icon(BitmapDescriptorFactory.fromResource(icone))
            );
            if(denuncia.getTipo() == "bonus"){
                marker.setTag("bonus");
            }
            i++;
        }

        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                if(marker.getTag() != null){
                    //é o bonus, logo vai para detalhes do bonus
                    addDenuncia(marker,"bonus");
                }else{
                    fragmentManager.beginTransaction().replace(R.id.content_main,new DenunciaDetalhe()).addToBackStack("denuncias_detalhes").commit();
                }

            }
        });

    }

    public void geraMarcador(LatLng latLng){
        if(!temMarcador){
            fab.setVisibility(View.GONE);
            temMarcador = true;

            final Marker localizacao = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Denunciar!"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localizacao.getPosition(), 18));
            Toast.makeText(getActivity(), "Clique no marcador para move-lo ou salvar localização!", Toast.LENGTH_LONG).show();
            localizacao.setTag("NovoMarcador");
            //quando o mapa se mover, o marcador move para a posição junto.
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    localizacao.setPosition(mMap.getCameraPosition().target);
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    localizacao.remove();
                    temMarcador = false;
                    //volto com o botão de adicionar marcador
                    fab.setVisibility(View.VISIBLE);
                    //sumo com os botões debaixo
                    ln.setVisibility(View.GONE);
                }
            });

            btnAddRest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDenuncia(localizacao,"");
                }
            });
            ln.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(getActivity(),"Já existe um marcador para adicionar", Toast.LENGTH_LONG).show();
        }
    }







    public void addDenuncia(final Marker marker,String tipo){
        if(tipo == "bonus"){
            fragmentManager.beginTransaction().replace(R.id.content_main,new DenunciaBonus()).addToBackStack("bonus").commit();
        }else {
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Evite suspensão da conta, somente denuncie com informações verdadeiras!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //se eu clicar em minha Localização, eu adiciono um novo local lá.
                            Bundle bundle = new Bundle();
                            locAtual = marker.getPosition();
                            Double lati = locAtual.latitude;
                            Double longi = locAtual.longitude;
                            bundle.putDouble("latitude", lati);
                            bundle.putDouble("longitude", longi);


                            DenunciaFragment denuncia = new DenunciaFragment();
                            marker.remove();
                            temMarcador = false;
                            mMap.setOnCameraMoveListener(null);

                            fab.setVisibility(View.VISIBLE);
                            ln.setVisibility(View.GONE);

                            denuncia.setArguments(bundle);
                            fragmentManager.beginTransaction().replace(R.id.content_main, denuncia).addToBackStack("nova_denuncia").commit();
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude=location.getLongitude();

        LatLng loc = new LatLng(latitude, longitude);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
