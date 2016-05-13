package com.sfsu.network.api;

/**
 * Filters the resources based on the
 * Created by Pavitra on 5/13/2016.
 */
public class ResourceFilter {
    /**
     * Specifies the where clause to filter resource data based on Key-Value
     */
    public static class Where {
        public final String key;
        public final String value;

        public Where(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{\"" + key + "\":\"" + value + "\"}";
        }
    }

    public static class Limit {

    }

    public static class Include {

    }

    public static class Order {

    }
}
