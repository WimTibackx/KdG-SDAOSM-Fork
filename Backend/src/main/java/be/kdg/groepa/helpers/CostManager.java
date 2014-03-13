package be.kdg.groepa.helpers;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;

/**
 * Created by Pieter-Jan on 26-2-14.
 */
public class CostManager {

    public static double calculateCost(PlaceTime p, PlaceTime t) // Calculate cost for en entire route
    {
        // GoogleMaps.zoekDieShit(p, t);    // TODO: Add Google Maps functionality
        return 0.00;
    }

    public static double getTotalDistance(Route r)
    {
        return 10.00;  // TODO: Add Google Maps functionality
    }

    public static double calculateCost(Route r)
    {
        return 10.00;
    }

    public static double getTrajectCost(Traject t)  // Calculate the cost for a specific traject within a route
    {
        return 0.00;
    }
}
