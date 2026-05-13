package pe.edu.upc.travelmatch.experiences.features;

import com.intuit.karate.junit5.Karate;

class CategoriesRunner {

  @Karate.Test
  Karate testCategories() {
    return Karate.run("categories").relativeTo(getClass());
  }
}
