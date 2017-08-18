package com.example.milka.mapdirect.utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milka on 2017/8/18.
 * 通过json获取路径链表，每个链表的节点为一个地址
 * List<String> list = new ParseJson(s).getRoutesAt(0);
 */

public class ParseJson {

    private final String TAG = "ParseJson";

    private DirectInfo directInfo;

    public ParseJson(String jsonString){
        if (jsonString == null){
            return;
        }
        Gson gson = new Gson();
        directInfo = gson.fromJson(jsonString, DirectInfo.class);
    }
/**
 * 获取DirectInfo.routes的第position（从0开始）个元素的legs
 * */
    private List<DirectInfo.Route.Leg> getLegsAt(int position){
        List<DirectInfo.Route.Leg> legs = new ArrayList<>();
        legs.addAll(directInfo.routes.get(position).legs);
        return legs;
    };

    public List<LatLng> getRouteAt(int routeIndex){
         List<LatLng> routes = new ArrayList<>();
        if (routeIndex < 0){
            return routes;
        }
        List<DirectInfo.Route.Leg> legs = getLegsAt(0);
        for (int i = 0; i <= routeIndex; i ++){
            if (i >= legs.size()){
                Log.e(TAG, "越界");
                return routes;
            }
            List<DirectInfo.Route.Leg.Step> steps = legs.get(i).steps;
            for (int j = 0; j < steps.size(); j ++){
                DirectInfo.Route.Leg.Step.LatLng startLatLng = steps.get(j).start_location;
                DirectInfo.Route.Leg.Step.LatLng endLatLng = steps.get(j).end_location;
//
//                Log.i(TAG, "start:    " + startLatLng.lat + "," + startLatLng.lng);
//                Log.i(TAG, "end  :    " + endLatLng.lat + "," + endLatLng.lng);
//                routes.add(new LatLng(startLatLng.lat, startLatLng.lng));
//                routes.add(new LatLng(endLatLng.lat, endLatLng.lng));
                routes.addAll(decodePolyLine(steps.get(j).polyline.points));
            }
        }
        return routes;
    }
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
