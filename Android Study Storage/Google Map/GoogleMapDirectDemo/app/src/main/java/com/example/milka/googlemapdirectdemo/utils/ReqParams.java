package com.example.milka.googlemapdirectdemo.utils;

import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by Milka on 2017/8/18.
 *
 *Google Direction API请求参数
 *
 * getParamsString()获取所有请求参数字符串
 * getRequestParams()获取必填参数串
 *
 *
 * 使用实例：
 * String url = DIRECTION_API_URL
                + new ReqParams(ORIGIN_POSITION, DESTINATION_POSITION, GOOGLE_API_KEY)
                .setLanguage(LANGUAGE_TYPE)
                .setMode(MODE)
                .getParamsString();
 */

public class ReqParams {

    private final String TAG = "ReqParams";

    /*必填参数*/
    private StringBuilder origin = new StringBuilder();
    private StringBuilder destination = new StringBuilder();
    private StringBuilder key = new StringBuilder();

    private final static String ORIGIN_KEY = "origin=";
    private final static String DESTINATION_KEY = "&destination=";
    private final static String KEY_KEY = "&key=";
/**
 * 构造参数
 *
 * @param origin    原始地址描述
 * @param destination 目的地址描述
 * @param key 开发者密钥，通过https://developers.google.com/maps/documentation/directions/intro#UnitSystems获取
 * */
    public ReqParams(@Nullable String origin, @Nullable String destination, @Nullable String key) {
        this.origin.delete(0, this.origin.length());
        this.destination.delete(0, this.destination.length());
        this.key.delete(0, this.key.length());

        this.origin.append(origin);
        this.destination.append(destination);
        this.key.append(key);
    }

    /*获取必填参数字符串*/
    public String getRequestParams(){
        return (
                ORIGIN_KEY + origin
                        + DESTINATION_KEY + destination
                        + KEY_KEY + key
        );
    }

    /*可选参数*/
    private StringBuilder mode = new StringBuilder();
    private StringBuilder wayPoints  = new StringBuilder();
    private StringBuilder alterNatives = new StringBuilder();
    private StringBuilder avoid  = new StringBuilder();
    private StringBuilder language = new StringBuilder();
    private StringBuilder units = new StringBuilder();
    private StringBuilder region = new StringBuilder();
    private StringBuilder arrivalTime = new StringBuilder();
    private StringBuilder departureTime = new StringBuilder();
    private StringBuilder trafficModel = new StringBuilder();
    private StringBuilder transitMode = new StringBuilder();
    private StringBuilder transitRoutingPreference = new StringBuilder();

    private final static String MODE_KEY = "&mode=";
    private final static String WAY_POINTS_KEY = "&waypoints=";
    private final static String ALTER_NATIVES_KEY = "&alternatives=";
    private final static String AVOID_KEY = "&avoid=";
    private final static String LANGUAGE_KEY = "&language=";
    private final static String UNITS_KEY = "&units=";
    private final static String REGION_KEY = "&region=";
    private final static String ARRIVAL_TIME_KEY = "&arrival_time=";
    private final static String DEPARTURE_TIME_KEY = "&departure_time=";
    private final static String TRAFFIC_MODEL_KEY = "&traffic_model=";
    private final static String TRANSIT_MODE_KEY = "&transit_mode=";
    private final static String TRANSIT_ROUTING_PREFERENCE_KEY = "&transit_routing_preference=";

    /*获取完整的请求参数字符串*/
    public String getParamsString(){
        return getRequestParams()+ getOptionalParams();
    }

    /*获取可选参数的请求参数字符串*/
    private String getOptionalParams(){
        String params = MODE_KEY + getMode()
                + WAY_POINTS_KEY + getWayPoints()
                + ALTER_NATIVES_KEY + getAlterNatives()
                + AVOID_KEY + getAvoid()
                + LANGUAGE_KEY + getLanguage()
                + UNITS_KEY + getUnits()
                + REGION_KEY + getRegion()
                + ARRIVAL_TIME_KEY + getArrivalTime()
                + DEPARTURE_TIME_KEY + getDepartureTime()
                + TRAFFIC_MODEL_KEY + getTrafficModel()
                + TRANSIT_MODE_KEY + getTransitMode()
                + TRANSIT_ROUTING_PREFERENCE_KEY + getTransitRoutingPreference();
        return params;
    }



    public String getMode() {
        return mode.toString();
    }

    public ReqParams setMode(String mode) {
        deleteAllAndSetStringBuffer(this.mode, mode);
        return this;
    }

    public String getWayPoints() {
        return wayPoints.toString();
    }

    public ReqParams setWayPoints(String wayPoints) {
       deleteAllAndSetStringBuffer(this.wayPoints, wayPoints);
        return this;
    }

    public String getAlterNatives() {
        return alterNatives.toString();
    }

    public ReqParams setAlterNatives(String alterNatives) {
       deleteAllAndSetStringBuffer(this.alterNatives, alterNatives);
        return this;
    }

    public String getAvoid() {
        return avoid.toString();
    }

    public ReqParams setAvoid(String avoid) {
      deleteAllAndSetStringBuffer(this.avoid, avoid);
        return this;
    }

    public String getLanguage() {
        return language.toString();
    }

    public ReqParams setLanguage(String language) {
       deleteAllAndSetStringBuffer(this.language, language);
        return this;
    }

    public String getUnits() {
        return units.toString();
    }

    public ReqParams setUnits(String units) {
       deleteAllAndSetStringBuffer(this.units, units);
        return this;
    }

    public String getRegion() {
        return region.toString();
    }

    public ReqParams setRegion(String region) {
       deleteAllAndSetStringBuffer(this.region, region);
        return this;
    }

    public String getArrivalTime() {
        return arrivalTime.toString();
    }

    public ReqParams setArrivalTime(String arrivalTime) {
        deleteAllAndSetStringBuffer(this.arrivalTime, arrivalTime);
        return this;
    }

    public String getDepartureTime() {
        return departureTime.toString();
    }

    public ReqParams setDepartureTime(String departureTime) {
        deleteAllAndSetStringBuffer(this.departureTime, departureTime);
        return this;
    }

    public String getTrafficModel() {
        return trafficModel.toString();
    }

    public ReqParams setTrafficModel(String trafficModel) {
        deleteAllAndSetStringBuffer(this.trafficModel, trafficModel);
        return this;
    }

    public String getTransitMode() {
        return transitMode.toString();
    }

    public ReqParams setTransitMode(String transitMode) {
        deleteAllAndSetStringBuffer(this.transitMode, transitMode);
        return this;
    }

    public String getTransitRoutingPreference() {
        return transitRoutingPreference.toString();
    }

    public ReqParams setTransitRoutingPreference(String transitRoutingPreference) {
        deleteAllAndSetStringBuffer(this.transitRoutingPreference, transitRoutingPreference);
        return this;
    }

    /*StringBuilder内容重置工具*/
    private void deleteAllAndSetStringBuffer(StringBuilder target, String data){
        target.delete(0, target.length());
        target.append(data);
    }

}
