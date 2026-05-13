package pe.edu.upc.travelmatch.experiences.features;

import com.intuit.karate.junit5.Karate;

public class TicketTypesRunner {

  @Karate.Test
  Karate testTicketTypes() {
    return Karate.run("ticket-types").relativeTo(getClass());
  }
}
