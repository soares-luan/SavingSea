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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luan on 21/01/2017.
 */

public class MarinheiroFragment extends Fragment implements OnMapReadyCallback, LocationListener {


    private static final int PERMISSION_ACCESS_FINE_LOCATION = 123;
    private View myView;
    private GoogleMap mMap;
    private PolygonOptions polygonOptions;
    private Polygon polygon;
    private Boolean gps_enabled = false;
    private Location gps_loc = null;
    private Boolean temMarcador = false;
    private LatLng locAtual;
    private RelativeLayout legenda;
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
            legenda = (RelativeLayout)myView.findViewById(R.id.legendaMapa);
            legenda.setVisibility(View.GONE);
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
        Denuncia denuncia = new Denuncia("poluicao","Pesca ilegal 321 lixo orgânico",-23.516589, -46.653596);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 111",-23.671359, -46.727480);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Despejo de lixo na água 79345",-23.759687, -46.632860);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Pesca ilegal 69389",-23.735120, -46.455525);
        denuncias.add(denuncia);


        denuncia = new Denuncia("poluicao","Despejo de lixo na água 23763",-24.053488, -46.36206);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 81790 rede",-24.129363, -46.611404);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 77256",-23.920991799366764, -45.65643310546875);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 50796",-24.12670195868167, -45.77728271484375);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 50253",-24.3721173001113, -46.32110595703125);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 15099 local proibido",-24.50714328310284, -46.89239501953125);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 57550",-24.811667555461824, -46.75506591796875);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 29683",-24.75680831119268, -46.22222900390625);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 96698",-24.861518533567935, -45.63995361328125);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 43199",-24.50714328310284, -45.48065185546875);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 01479",-24.166802085303225, -44.54681396484375);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 19674 rede",-23.920991799366764, -44.82696533203125);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 18579",-25.24966438712087, -45.18951416015625);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 70876",-25.50278454875533, -45.78277587890625);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 97206",-24.661994379101547, -44.80499267578125);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 36347 local proibido",-23.190862576873606, -42.9180908203125);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 90667",-23.422928455065247, -43.8189697265625);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 46488",-23.815500848699653, -42.5006103515625);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 45372",-24.45715052418584, -41.5228271484375);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 88834",-24.866502526926897, -42.3577880859375);
        denuncias.add(denuncia);
        denuncia = new Denuncia("poluicao","Despejo de lixo na água 35022",-24.986058021167594, -43.3245849609375);
        denuncias.add(denuncia);
        denuncia = new Denuncia("pesca","Pesca ilegal 86952 local proibido",-25.878994400196202, -43.9178466796875);
        denuncias.add(denuncia);

        denuncia = new Denuncia("bonus","Despejo de lixo na água 85903",-26.382027976025352, -44.1265869140625);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Pesca ilegal 61236",-25.878994400196202, -47.5872802734375);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Despejo de lixo na água 44612",-27.244862521497268, -47.5543212890625);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Pesca ilegal 73281",-27.858503954841233, -46.3018798828125);
        denuncias.add(denuncia);
        denuncia = new Denuncia("bonus","Despejo de lixo na água 37943",-27.254629577800063, -44.2584228515625);
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

        polygonOptions.add(new LatLng(-23.862571, -45.781183));
        polygonOptions.add(new LatLng(-23.902442, -45.451776));
        polygonOptions.add(new LatLng(-24.099399, -45.693058));
        polygonOptions.strokeColor(Color.RED);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#66ff4444"));
        polygon = mMap.addPolygon(polygonOptions);

        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-23.864752, -45.781411));
        polygonOptions.add(new LatLng(-24.103967, -45.696992));
        polygonOptions.add(new LatLng(-23.968764, -46.157014));
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#6600FF00"));
        polygon = mMap.addPolygon(polygonOptions);


        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-23.039941, -44.383647));
        polygonOptions.add(new LatLng(-23.135339, -44.320772));
        polygonOptions.add(new LatLng(-23.059477, -44.276678));
        polygonOptions.strokeColor(Color.RED);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#66ff4444"));
        polygon = mMap.addPolygon(polygonOptions);

        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-23.568392777659398,-45.94748497009277));
        polygonOptions.add(new LatLng(-23.590733437434338,-45.95847129821777));
        polygonOptions.add(new LatLng(-23.598756267349245,-45.924482345581055));
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#6600FF00"));
        polygon = mMap.addPolygon(polygonOptions);

        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-23.358646434828184,-45.63617706298828));
        polygonOptions.add(new LatLng(-23.399614073261095,-45.647850036621094));
        polygonOptions.add(new LatLng(-23.411586836449985,-45.59154510498047));
        polygonOptions.strokeColor(Color.RED);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#66ff4444"));
        polygon = mMap.addPolygon(polygonOptions);

        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-23.79634000736371,-45.3925895690918));
        polygonOptions.add(new LatLng(-23.815814937298633,-45.41215896606445));
        polygonOptions.add(new LatLng(-23.833716730977766,-45.40220260620117));
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#6600FF00"));
        polygon = mMap.addPolygon(polygonOptions);


        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-23.735697754002516,-45.2552604675293));
        polygonOptions.add(new LatLng(-23.75738139078759,-45.262813568115234));
        polygonOptions.add(new LatLng(-23.78314565403936,-45.24839401245117));
        polygonOptions.strokeColor(Color.RED);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#66ff4444"));
        polygon = mMap.addPolygon(polygonOptions);

        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-23.611811980488632,-42.84873962402344));
        polygonOptions.add(new LatLng(-23.995035184576945,-42.95722961425781));
        polygonOptions.add(new LatLng(-24.066528197726853,-42.66883850097656));
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#6600FF00"));
        polygon = mMap.addPolygon(polygonOptions);


        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-24.94123829939631,-47.72735595703125));
        polygonOptions.add(new LatLng(-25.433353427832145,-47.77679443359375));
        polygonOptions.add(new LatLng(-25.468073997498134,-47.20550537109375));
        polygonOptions.strokeColor(Color.RED);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#66ff4444"));
        polygon = mMap.addPolygon(polygonOptions);

        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-25.234758470233718,-44.31610107421875));
        polygonOptions.add(new LatLng(-25.745477067368604,-44.72259521484375));
        polygonOptions.add(new LatLng(-26.559049984075532,-44.29412841796875));
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#6600FF00"));
        polygon = mMap.addPolygon(polygonOptions);


        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-25.859223554761382,-42.03643798828125));
        polygonOptions.add(new LatLng(-27.024877476307523,-42.51434326171875));
        polygonOptions.add(new LatLng(-27.366889032381298,-40.93231201171875));
        polygonOptions.strokeColor(Color.RED);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#66ff4444"));
        polygon = mMap.addPolygon(polygonOptions);

        polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-24.287026865376426,-46.50238037109375));
        polygonOptions.add(new LatLng(-24.88145330131796,-46.49139404296875));
        polygonOptions.add(new LatLng(-25.02090651422749,-45.91461181640625));
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth((float) 0.30);
        polygonOptions.fillColor(Color.parseColor("#6600FF00"));
        polygon = mMap.addPolygon(polygonOptions);






        setaMarcadores(denuncias, mMap);
    }

    private void setaMarcadores(List<Denuncia> denuncias, final GoogleMap mMap) {
        int i = 0;

        for (Denuncia denuncia : denuncias) {

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(denuncia.getLatitude(),denuncia.getLongitude()))
                    .title(denuncia.getDescricao())
            );
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
