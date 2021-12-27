package com.psteam.foodlocationbusiness.models;


public class Results {
    private String formatted_address;

    private String[] types;

    private Geometry geometry;

    private Address_components[] address_components;

    private String place_id;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Address_components[] getAddress_components() {
        return address_components;
    }

    public void setAddress_components(Address_components[] address_components) {
        this.address_components = address_components;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    @Override
    public String toString() {
        return "ClassPojo [formatted_address = " + formatted_address + ", types = " + types + ", geometry = " + geometry + ", address_components = " + address_components + ", place_id = " + place_id + "]";
    }

    public class Geometry
    {
        private Viewport viewport;

        private Bounds bounds;

        private Location location;

        private String location_type;

        public Viewport getViewport ()
        {
            return viewport;
        }

        public void setViewport (Viewport viewport)
        {
            this.viewport = viewport;
        }

        public Bounds getBounds ()
        {
            return bounds;
        }

        public void setBounds (Bounds bounds)
        {
            this.bounds = bounds;
        }

        public Location getLocation ()
        {
            return location;
        }

        public void setLocation (Location location)
        {
            this.location = location;
        }

        public String getLocation_type ()
        {
            return location_type;
        }

        public void setLocation_type (String location_type)
        {
            this.location_type = location_type;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [viewport = "+viewport+", bounds = "+bounds+", location = "+location+", location_type = "+location_type+"]";
        }

        public class Viewport
        {
            private Southwest southwest;

            private Northeast northeast;

            public Southwest getSouthwest ()
            {
                return southwest;
            }

            public void setSouthwest (Southwest southwest)
            {
                this.southwest = southwest;
            }

            public Northeast getNortheast ()
            {
                return northeast;
            }

            public void setNortheast (Northeast northeast)
            {
                this.northeast = northeast;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [southwest = "+southwest+", northeast = "+northeast+"]";
            }
        }

        public class Southwest
        {
            private String lng;

            private String lat;

            public String getLng ()
            {
                return lng;
            }

            public void setLng (String lng)
            {
                this.lng = lng;
            }

            public String getLat ()
            {
                return lat;
            }

            public void setLat (String lat)
            {
                this.lat = lat;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
            }
        }


        public class Location
        {
            private String lng;

            private String lat;

            public String getLng ()
            {
                return lng;
            }

            public void setLng (String lng)
            {
                this.lng = lng;
            }

            public String getLat ()
            {
                return lat;
            }

            public void setLat (String lat)
            {
                this.lat = lat;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
            }
        }

        public class Bounds
        {
            private Southwest southwest;

            private Northeast northeast;

            public Southwest getSouthwest ()
            {
                return southwest;
            }

            public void setSouthwest (Southwest southwest)
            {
                this.southwest = southwest;
            }

            public Northeast getNortheast ()
            {
                return northeast;
            }

            public void setNortheast (Northeast northeast)
            {
                this.northeast = northeast;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [southwest = "+southwest+", northeast = "+northeast+"]";
            }

            public class Southwest
            {
                private String lng;

                private String lat;

                public String getLng ()
                {
                    return lng;
                }

                public void setLng (String lng)
                {
                    this.lng = lng;
                }

                public String getLat ()
                {
                    return lat;
                }

                public void setLat (String lat)
                {
                    this.lat = lat;
                }

                @Override
                public String toString()
                {
                    return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
                }
            }


        }
        public class Northeast
        {
            private String lng;

            private String lat;

            public String getLng ()
            {
                return lng;
            }

            public void setLng (String lng)
            {
                this.lng = lng;
            }

            public String getLat ()
            {
                return lat;
            }

            public void setLat (String lat)
            {
                this.lat = lat;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
            }
        }
    }

    public class Address_components
    {
        private String[] types;

        private String short_name;

        private String long_name;

        public String[] getTypes ()
        {
            return types;
        }

        public void setTypes (String[] types)
        {
            this.types = types;
        }

        public String getShort_name ()
        {
            return short_name;
        }

        public void setShort_name (String short_name)
        {
            this.short_name = short_name;
        }

        public String getLong_name ()
        {
            return long_name;
        }

        public void setLong_name (String long_name)
        {
            this.long_name = long_name;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [types = "+types+", short_name = "+short_name+", long_name = "+long_name+"]";
        }
    }


}
