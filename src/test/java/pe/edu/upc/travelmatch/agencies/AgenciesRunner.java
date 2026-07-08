package pe.edu.upc.travelmatch.agencies;

import com.intuit.karate.junit5.Karate;

class AgenciesRunner {

  @Karate.Test
  Karate testAgencies() {
    return Karate.run("agencies").relativeTo(getClass());
  }
}
