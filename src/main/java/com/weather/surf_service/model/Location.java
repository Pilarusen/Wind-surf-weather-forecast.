package com.weather.surf_service.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Location {

    public static HashMap<String, Map<String, String>> locations = new HashMap<>();

    static {
        locations.put("JASTARNIA_POLAND", Collections.singletonMap("54.70", "18.67"));
        locations.put("BRIDGETOWN_BARBADOS", Collections.singletonMap("13.10", "-59.60"));
        locations.put("FORTALEZA_BRAZIL", Collections.singletonMap("-3.73", "-38.52"));
        locations.put("PISSOURI_CYPRUS", Collections.singletonMap("34.66", "32.70"));
        locations.put("LE_MORNE_MAURITIUS", Collections.singletonMap("-20.45", "57.32"));
    }
}
