package pe.edu.upc.travelmatch.geolocationv2.features;

import com.intuit.karate.junit5.Karate;

public class DestinationsRunner {
    @Karate.Test
    Karate testDestinations() {
        return Karate.run("destinations").relativeTo(getClass());
    }
}
