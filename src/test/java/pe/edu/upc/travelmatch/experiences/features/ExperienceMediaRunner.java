package pe.edu.upc.travelmatch.experiences.features;

import com.intuit.karate.junit5.Karate;

public class ExperienceMediaRunner {

    @Karate.Test
    Karate testExperienceMedia() {
        return Karate.run("experience-media").relativeTo(getClass());
    }
}
