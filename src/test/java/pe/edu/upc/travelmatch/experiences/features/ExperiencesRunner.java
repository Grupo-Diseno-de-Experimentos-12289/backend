package pe.edu.upc.travelmatch.experiences.features;

import com.intuit.karate.junit5.Karate;

class ExperiencesRunner {

  @Karate.Test
  Karate testExperiences() {
    return Karate.run("experiences").relativeTo(getClass());
  }
}
