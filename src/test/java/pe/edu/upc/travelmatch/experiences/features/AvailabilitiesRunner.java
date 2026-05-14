package pe.edu.upc.travelmatch.experiences.features;

import com.intuit.karate.junit5.Karate;

class AvailabilitiesRunner {

  @Karate.Test
  Karate testAvailabilities() {
    return Karate.run("availabilities").relativeTo(getClass());
  }
}
