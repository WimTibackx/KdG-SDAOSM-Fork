package be.kdg.groepa.helpers;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;

import java.util.List;

/**
 * Created by Pieter-Jan on 26-2-14.
 */
public class CostManager {

    public static double calculateCost(PlaceTime p, PlaceTime t) // Calculate cost for en entire route
    {
        // TODO: Add Google Maps functionality
        return 0.00;
    }

    public static double getRouteCost(Route r)
    {
        double sum = 0.00;
        for (double d : getRouteCosts(r))
        {
            sum += d;
        }
        return sum;
    }

    public static double[] getRouteCosts(Route r)
    {
        double[] rtval = new double[r.getPlaceTimes().size()-1];
        List<PlaceTime> temp = r.getAllPlaceTimes();
        for (int i = 0; i < rtval.length; i++)
        {
            rtval[i] = calculateCost(temp.get(i), temp.get(i+1));
        }
        rtval[r.getPlaceTimes().size()-2] = calculateCost(temp.get(r.getPlaceTimes().size()-2), temp.get(r.getPlaceTimes().size()-1));
        return rtval;
    }

    public static double getTotalDistance(Route r) {

        return 0.00;
    }
}
