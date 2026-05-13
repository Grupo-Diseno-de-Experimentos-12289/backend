package pe.edu.upc.travelmatch.iam.features;

import com.intuit.karate.junit5.Karate;

public class AuthenticationRunner {
    @Karate.Test
    Karate testAuthentication() {
        return Karate.run("authentication").relativeTo(getClass());
    }
}
