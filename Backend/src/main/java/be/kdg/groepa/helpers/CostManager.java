package be.kdg.groepa.helpers;

import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.List;


/**
 * Created by Pieter-Jan on 26-2-14.
 */
public class CostManager {
    private static final String USER_AGENT = "Mozilla/5.0";

    public static double calculateCost(PlaceTime p, PlaceTime t, Car c) // Calculate cost from one point to another
    {
        return c.getConsumption() / 100 * ((double)getDistanceBetweenPoints(p, t) / 1000);
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
            rtval[i] = calculateCost(temp.get(i), temp.get(i+1), r.getCar());
        }
        rtval[r.getPlaceTimes().size()-2] = calculateCost(temp.get(r.getPlaceTimes().size()-2), temp.get(r.getPlaceTimes().size()-1), r.getCar());
        return rtval;
    }

    public static int getDistanceBetweenPoints(PlaceTime p, PlaceTime t)
    {
        int temp = googleMapsHttpCall(p, t);
        if (temp == -1)
        {
            System.out.println("ERROR IN GETDISTANCEBETWEENPOINTS");
        }
        return temp;
    }

    public static double getTotalDistance(Route r) {
        return 0.00;
    }

    public static int googleMapsHttpCall(PlaceTime p, PlaceTime t)
    {
        try {
            String urlParameters = "origins=" + p.getPlace().getName().replace(" ", "+") + "&destinations=" + t.getPlace().getName().replace(" ", "+") + "&sensor=false";
            String url = "http://maps.googleapis.com/maps/api/distancematrix/json?" + urlParameters;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("GET");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject json = new JSONObject(response.toString());
            int distance = 0;
            if (!json.getString("status").equals("OK"))
            {
                System.out.println("ERROR PARSING JSON FILE @ HTTP REQUEST - googleMapsHttpCall()");
            }
            else
            {
                distance = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getInt("value");
            }
            return distance;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
