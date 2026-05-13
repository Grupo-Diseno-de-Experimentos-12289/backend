package pe.edu.upc.travelmatch.experiences;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExperiencesRunner {
    @Test
    void testParallel() {
        Results results = Runner.path("classpath:pe/edu/upc/travelmatch/experiences")
                //.outputCucumberJson(true)
                .parallel(5);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
