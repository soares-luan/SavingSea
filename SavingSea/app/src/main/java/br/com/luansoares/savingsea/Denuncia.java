package br.com.luansoares.savingsea;

/**
 * Created by luan on 20/01/2017.
 */

public class Denuncia {
    String tipo,descricao;
    Double latitude,longitude;

    public Denuncia(){

    }
    public Denuncia(String tipo, String descricao, Double latitude, Double longitude) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
