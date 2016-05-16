package com.sfsu.network.api;

/**
 * Filters the resources based on the key-value provides for each Filter type.
 * Created by Pavitra on 5/13/2016.
 */
public class ResourceFilter {
    /**
     * Specifies the where clause to filter resource data based on Key-Value
     */
    public static final class Where {
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

    /**
     * Limits the count of GET call for resource from server
     */
    public static class Limit {

    }

    /**
     * Includes additional Models along with the current Model while fetching resource data from the server.
     */
    public static final class Include {
        private final int count;


        public Include(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "";
        }
    }

    /**
     * Specifies the Order in which the
     */
    public static final class Order {
        private final boolean DESC;


        public Order(boolean isDescending) {
            DESC = isDescending;
        }

        @Override
        public String toString() {
            String orderFilter = (DESC) ? "{\"order\": \"timestamp DESC\"}" : "{\"order\": \"timestamp ASC\"}";
            return orderFilter;
        }
    }

    /**
     *
     */
    public static final class CompositeFilter {

    }
}
