package pe.edu.upc.travelmatch.iam.features;

import com.intuit.karate.junit5.Karate;

class RolesRunner {
  @Karate.Test
  Karate testRoles() {
    return Karate.run("roles").relativeTo(getClass());
  }
}
