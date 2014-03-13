package be.kdg.groepa.dtos;

import be.kdg.groepa.model.PlaceTime;

/**
 * Created by Pieter-Jan on 12-3-14.
 */
public class PlaceTimeDTO {

    private PlaceDTO place;
    private String time;

    public PlaceTimeDTO(PlaceTime pt) {
        time = pt.getTime().toString();
        place = new PlaceDTO(pt.getPlace());
    }

    public PlaceDTO getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }
}
