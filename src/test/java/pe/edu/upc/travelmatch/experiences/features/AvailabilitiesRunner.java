package pe.edu.upc.travelmatch.experiences.features;

import com.intuit.karate.junit5.Karate;

public class AvailabilitiesRunner {

    @Karate.Test
    Karate testAvailabilities() {
        return Karate.run("availabilities").relativeTo(getClass());
    }
}
