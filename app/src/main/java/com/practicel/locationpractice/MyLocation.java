package com.practicel.locationpractice;

public class MyLocation {

private int accuracy,altitude,bearing,bearingAccuracyDegrees,speed,speedAccuracyMetersPerSecond,verticalAccuracyMeters;
private boolean complete,fromMockProvider;
private String provider;
private long time, elapsedRealtimeNanos;
private long latitude,longitude;


    public MyLocation() {
    }


    public MyLocation(int accuracy, int altitude, int bearing, int bearingAccuracyDegrees, int speed, int speedAccuracyMetersPerSecond, int verticalAccuracyMeters, boolean complete, boolean fromMockProvider, String provider, long time, long elapsedRealtimeNanos, long latitude, long longitude) {
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.bearing = bearing;
        this.bearingAccuracyDegrees = bearingAccuracyDegrees;
        this.speed = speed;
        this.speedAccuracyMetersPerSecond = speedAccuracyMetersPerSecond;
        this.verticalAccuracyMeters = verticalAccuracyMeters;
        this.complete = complete;
        this.fromMockProvider = fromMockProvider;
        this.provider = provider;
        this.time = time;
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }

    public int getBearingAccuracyDegrees() {
        return bearingAccuracyDegrees;
    }

    public void setBearingAccuracyDegrees(int bearingAccuracyDegrees) {
        this.bearingAccuracyDegrees = bearingAccuracyDegrees;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeedAccuracyMetersPerSecond() {
        return speedAccuracyMetersPerSecond;
    }

    public void setSpeedAccuracyMetersPerSecond(int speedAccuracyMetersPerSecond) {
        this.speedAccuracyMetersPerSecond = speedAccuracyMetersPerSecond;
    }

    public int getVerticalAccuracyMeters() {
        return verticalAccuracyMeters;
    }

    public void setVerticalAccuracyMeters(int verticalAccuracyMeters) {
        this.verticalAccuracyMeters = verticalAccuracyMeters;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isFromMockProvider() {
        return fromMockProvider;
    }

    public void setFromMockProvider(boolean fromMockProvider) {
        this.fromMockProvider = fromMockProvider;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getElapsedRealtimeNanos() {
        return elapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(long elapsedRealtimeNanos) {
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
