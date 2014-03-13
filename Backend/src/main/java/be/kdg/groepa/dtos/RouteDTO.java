package be.kdg.groepa.dtos;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 12-3-14.
 */
public class RouteDTO {

    private List<PlaceTimeDTO> placetimes = new ArrayList<>();
    private int routeId;
    private int cap;

    public RouteDTO(Route r)
    {
        this.routeId = r.getId();
        this.cap = r.getCapacity();
        for (PlaceTime pt : r.getAllPlaceTimes())
        {
            placetimes.add(new PlaceTimeDTO(pt));
        }
    }

    public List<PlaceTimeDTO> getPlacetimes() {
        return placetimes;
    }

    public int getRouteId() {
        return routeId;
    }

    public int getCap() {
        return cap;
    }
}
