package com.example.milka.googlemapdirectdemo.utils;

import java.util.List;

import javax.xml.datatype.Duration;

/**
 * Created by Milka on 2017/8/17.
 *
 * GSON 方法1
 * 解析Google Direction API 返回JSON数据解析用的类
 *
 * 请勿在此改动，或改动前建立副本
 */

public class DirectInfo1 {

    public List<GeoCodedWayPoint> geocoded_waypoints;
    public List<Route> routes;
    public String status;

    public static class GeoCodedWayPoint{
        public String geocoder_status;
        public String place_id;
        public List<String> types;
    }

    public static class Route{
        public Bound bounds;
        public String copyrights;
        public List<Leg> legs;
        public OverviewPolyline overview_polyline;
        public String summary;
        public List<String> warnings;
        public List<String> waypoint_order;
    }

    public static class OverviewPolyline{
        public String points;
    }

    public static class Leg{
        public Distance distance;
        public Duration duration;
        public String end_address;
        public LatLng end_location;
        public String start_address;
        public LatLng start_location;
        public List<Step> steps;
        public List<String> traffic_speed_entry;
        public List<String> via_waypoint;
    }

    public static class Step{
        public Distance distance;
        public Duration duration;
        public LatLng end_location;
        public String html_instructions;
        public String maneuver;
        public Polyline polyline;
        public LatLng start_location;
        public String travel_mode;
    }

    public static class Polyline{
        public String points;
    }

    public static class Distance{
        public String text;
        public String value;
    }
    public static class Duration{
        public String text;
        public String value;
    }


    public static class Bound{
        public LatLng northeast;
        public LatLng southwest;
    }


    public static class LatLng{
        public String lat;
        public String lng;
    }
}
