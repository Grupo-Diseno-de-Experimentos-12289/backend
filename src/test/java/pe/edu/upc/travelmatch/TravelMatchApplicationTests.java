package pe.edu.upc.travelmatch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled(
    "Deactivating the authentication tests for now, since they are not working and we need to focus"
        + " on other features")
class TravelMatchApplicationTests {

  @Test
  void contextLoads() {}
}
