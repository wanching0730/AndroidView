package com.example.milka.googlemapjsonreader.utils;

import android.util.Log;

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

    public List<String> getRoutesAt(int routeIndex){
        List<String> routes = new ArrayList<>();
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

                Log.i(TAG, "start:    " + startLatLng.lat + "," + startLatLng.lng);
                Log.i(TAG, "end  :    " + endLatLng.lat + "," + endLatLng.lng);
                routes.add(startLatLng.lat + ", " + startLatLng.lng);
                routes.add(endLatLng.lat + ", " + startLatLng.lng);
            }
        }
        return routes;
    }

}
