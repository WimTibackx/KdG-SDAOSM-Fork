package be.kdg.groepa.dtos;

import org.threeten.bp.LocalTime;

/**
 * Created by delltvgateway on 3/6/14.
 */
public class PassageDTO {
    private int id;
    private int seqnr;
    private String address;
    private String time;
    private double lat;
    private double lng;

    public PassageDTO() {}

    public PassageDTO(int id,int seqnr, String address, LocalTime time, double lat, double lng) {
        this.id = id;
        this.seqnr = seqnr;
        this.address = address;
        this.time = time.toString();
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeqnr() {
        return seqnr;
    }

    public void setSeqnr(int seqnr) {
        this.seqnr = seqnr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time.toString();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}