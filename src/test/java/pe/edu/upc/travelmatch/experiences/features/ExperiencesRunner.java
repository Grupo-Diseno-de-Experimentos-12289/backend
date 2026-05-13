package pe.edu.upc.travelmatch.experiences.features;

import com.intuit.karate.junit5.Karate;

public class ExperiencesRunner {

  @Karate.Test
  Karate testExperiences() {
    return Karate.run("experiences").relativeTo(getClass());
  }
}
