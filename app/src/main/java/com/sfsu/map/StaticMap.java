package com.sfsu.map;

import com.google.android.gms.maps.model.LatLng;
import com.sfsu.entities.Account;

/**
 * <p>
 * Handles creation of url for static google map which displays the polyline for the {@link Account}
 * travelled {@link com.sfsu.entities.Activities}.
 * </p>
 * <p>
 * The StaticMap uses Builder pattern to build a dynamic url for making a network request and getting a static google map
 * image with a specific polyline according to the points specified in the Path param.
 * </p>
 * <p>
 * https://developers.google.com/maps/documentation/static-maps/intro
 * http://maps.googleapis.com/maps/api/staticmap?sensor=false&maptype=roadmap&zoom=14&size=650x350&path=40.737102,-73.990318%7C40.749825,-73.987963%7C40.752946,-73.987384%7C40.755823,-73.986397
 * <p>
 * Created by Pavitra on 12/23/2015.
 */
public class StaticMap {
    /**
     * UrlBuilder to build url with locations.
     */
    public static class UrlBuilder {

        private final boolean SENSOR = false;
        private final String MAP_TYPE = "roadmap";
        private final String STATIC_MAP_BASE_URL = "http://maps.googleapis.com/maps/api/staticmap?";
        private final String AMPERSAND = "&";
        private final String COMMA = ",";
        private final String EQUALS = "=";
        private final String MULTIPLY = "x";
        private final String PIPE = "|";
        private StringBuilder mapUrlBuilder = new StringBuilder();
        private StringBuilder pathBuilder = new StringBuilder();
        private int image_width;
        private int image_height;
        private int image_zoom;
        private int counter;
        private double point_lat, point_long;
        private LatLng[] mlatLngs;


        /**
         * Initialize the Builder object with constants
         *
         * @return
         */
        public UrlBuilder init() {
            mapUrlBuilder.append(STATIC_MAP_BASE_URL)
                    .append("sensor").append(EQUALS).append(SENSOR).append(AMPERSAND)
                    .append("maptype").append(EQUALS).append(MAP_TYPE);
            return this;
        }

        /**
         * Zoom level of the village
         *
         * @param zoom
         * @return
         */
        public UrlBuilder zoom(int zoom) {
            this.image_zoom = zoom;
            mapUrlBuilder.append(AMPERSAND).append("zoom").append(EQUALS).append(image_zoom);
            return this;
        }

        /**
         * Size of the static image
         *
         * @param height
         * @param width
         * @return
         */
        public UrlBuilder size(int height, int width) {
            this.image_height = height;
            this.image_width = width;
            if (mapUrlBuilder != null) {
                mapUrlBuilder.append(AMPERSAND).append("size").append(EQUALS).append(image_height).append(MULTIPLY).append
                        (image_width);
            }
            return this;
        }

        /**
         * Plot all the lat longs along the user path to build trajectory on maps
         *
         * @param latitude
         * @param longitude
         * @return
         */
        public UrlBuilder path(double latitude, double longitude) {
            this.point_lat = latitude;
            this.point_long = longitude;
            if (counter == 0) {
                pathBuilder.append(AMPERSAND).append("path").append(EQUALS).append(point_lat).append(COMMA).append(point_long);
            } else if (counter > 0) {
                pathBuilder.append(PIPE).append(point_lat).append(COMMA).append(point_long);
            }
            counter++;
            return this;
        }

        /**
         * Takes an array of LatLng objects
         *
         * @param latLngs
         * @return
         */
        public UrlBuilder path(LatLng[] latLngs) {
            this.mlatLngs = latLngs;
            for (int i = 0; i < mlatLngs.length; i++) {
                if (counter == 0) {
                    pathBuilder.append(AMPERSAND).append("path").append(EQUALS).append(mlatLngs[i].latitude).append(COMMA).append
                            (mlatLngs[i].longitude);
                } else if (counter > 0) {
                    pathBuilder.append(PIPE).append(mlatLngs[i].latitude).append(COMMA).append(mlatLngs[i].longitude);
                }
            }
            return this;
        }

        /**
         * Finally build the Url String to call to Google Maps server
         *
         * @return
         */
        public String build() {
            return mapUrlBuilder.append(pathBuilder).toString();
        }
    }
}