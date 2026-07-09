package pe.edu.upc.travelmatch.profiles.application.internal.queryservices;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.upc.travelmatch.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories.DestinationRepository;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.ReviewRepository;

@SpringBootTest
class DatabaseSeederTest {

  @Autowired private ExperienceRepository experienceRepository;
  @Autowired private DestinationRepository destinationRepository;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private TicketTypeRepository ticketTypeRepository;
  @Autowired private AvailabilityRepository availabilityRepository;
  @Autowired private ReviewRepository reviewRepository;
  @Autowired private BookingRepository bookingRepository;

  @Test
  void seedRealDestinationsAndExperiences() {
    var cultura = categoryRepository.findByName(Categories.CULTURA).orElseThrow();
    var gastronomia = categoryRepository.findByName(Categories.GASTRONOMIA).orElseThrow();
    var naturaleza = categoryRepository.findByName(Categories.NATURALEZA).orElseThrow();
    var deporte = categoryRepository.findByName(Categories.DEPORTE).orElseThrow();
    var ticketGeneral = ticketTypeRepository.findByName(TicketTypes.TICKET_GENERAL).orElseThrow();
    var ticketVip = ticketTypeRepository.findByName(TicketTypes.TICKET_VIP).orElseThrow();

    var cuscoId = findOrCreateDestination(
        "Cusco", "Plaza de Armas s/n", "Cusco", "Cusco", "Cusco", "Peru");
    var limaId = findOrCreateDestination(
        "Lima Centro", "Jr. de la Union 300", "Cercado de Lima", "Lima", "Lima", "Peru");
    var arequipaId = findOrCreateDestination(
        "Arequipa", "Calle Santa Catalina 210", "Cercado", "Arequipa", "Arequipa", "Peru");

    var realTitles = List.of(
        "Machu Picchu Full Day",
        "Sacred Valley Explorer",
        "Cusco City Walking Tour",
        "Cusco Gastronomic Experience",
        "Rainbow Mountain Trek",
        "Humantay Lake Trek",
        "Lima Historic Center Tour",
        "Miraflores Food Tour",
        "Lima Bike Tour",
        "Pachacamac Ruins Tour",
        "Colca Canyon Trek",
        "Arequipa City Tour",
        "Arequipa Picanterias Tour"
    );

    // Clean up test data that are not real experiences
    for (var exp : experienceRepository.findAll()) {
      if (!realTitles.contains(exp.getTitle())) {
        var reviews = reviewRepository.findAllByExperienceId(new ExperienceId(exp.getId()));
        reviewRepository.deleteAll(reviews);

        var avails = availabilityRepository.findAll().stream()
            .filter(a -> a.getExperience().getId().equals(exp.getId()))
            .toList();

        for (var avail : avails) {
          var bookings = bookingRepository.findAll().stream()
              .filter(b -> b.getAvailabilityId().availabilityId().equals(avail.getId()))
              .toList();
          bookingRepository.deleteAll(bookings);
        }
        availabilityRepository.deleteAll(avails);
        experienceRepository.delete(exp);
      }
    }

    var defs = List.of(
        new ExpDef(
            "Machu Picchu Full Day",
            "Embark on an unforgettable journey to one of the Seven Wonders of the World. "
                + "Enjoy a scenic train ride through the Sacred Valley, followed by a fully guided tour "
                + "exploring the legendary Inca citadel ruins, temples, terraces, and panoramic viewpoints.",
            cuscoId, cultura, "10h", "Cusco Main Square", new BigDecimal("180.00"),
            List.of(
                new RevDef(1L, 5, "An absolute dream come true! The guide was very knowledgeable."),
                new RevDef(2L, 5, "Extremely well organized. The train views were breathtaking.")
            )
        ),
        new ExpDef(
            "Sacred Valley Explorer",
            "Discover the ancient heart of the Inca Empire. Traverse breathtaking Andean valley landscapes, "
                + "visit the vibrant Pisac artisan market, marvel at the steep agricultural terraces, "
                + "and tour the impressive stone fortress at Ollantaytambo with our expert historian guide.",
            cuscoId, cultura, "8h", "Hotel lobby pickup", new BigDecimal("95.00"),
            List.of(
                new RevDef(3L, 4, "A great way to see multiple ruins in a single day. Excellent buffet lunch."),
                new RevDef(4L, 5, "Highly recommend this tour before heading to Machu Picchu.")
            )
        ),
        new ExpDef(
            "Cusco City Walking Tour",
            "Unravel the rich colonial and pre-Hispanic tapestry of Cusco. Wander through narrow cobblestone "
                + "streets, admire imperial Inca stone walls, and visit historic landmarks like the majestic temple "
                + "of Qorikancha and the bohemian neighborhood of San Blas.",
            cuscoId, cultura, "3h", "Plaza de Armas fountain", new BigDecimal("35.00"),
            List.of(
                new RevDef(1L, 4, "A pleasant walking pace. The San Blas neighborhood is charming."),
                new RevDef(2L, 5, "Incredible history told by a very passionate local guide.")
            )
        ),
        new ExpDef(
            "Cusco Gastronomic Experience",
            "Tempt your palate with the rich culinary heritage of the Andes. Learn to prepare classic Peruvian "
                + "dishes like fresh ceviche, savory lomo saltado, and shake up the perfect pisco sour cocktail "
                + "using organic ingredients sourced directly from San Pedro Market.",
            cuscoId, gastronomia, "4h", "San Pedro Market entrance", new BigDecimal("65.00"),
            List.of(
                new RevDef(3L, 5, "Fabulous cooking class! The chef was fun and the food tasted amazing.")
            )
        ),
        new ExpDef(
            "Rainbow Mountain Trek",
            "Test your limits with a high-altitude expedition to the stunning striped peaks of Vinicunca. "
                + "Hike along dramatic valleys, see herds of native alpacas, and stand in awe of the surreal "
                + "natural minerals that dye this sacred mountain in a rainbow of pastel colors.",
            cuscoId, deporte, "12h", "Hotel lobby pickup", new BigDecimal("120.00"),
            List.of(
                new RevDef(4L, 5, "Challenging altitude but the view at the summit is worth every single step.")
            )
        ),
        new ExpDef(
            "Humantay Lake Trek",
            "Journey to a hidden turquoise gem tucked beneath the snow-capped Humantay Peak. "
                + "Trek through crisp alpine trails, breathe in the pure mountain air, and take iconic photos "
                + "of the crystal-clear glacial waters reflecting the dramatic Andean skies.",
            cuscoId, naturaleza, "10h", "Cusco bus terminal", new BigDecimal("85.00"),
            List.of(
                new RevDef(1L, 5, "Prettiest lake I have ever seen. Absolutely magical landscape.")
            )
        ),
        new ExpDef(
            "Lima Historic Center Tour",
            "Immerse yourself in the colonial grandeur of the 'City of the Kings'. Marvel at ornate wooden balconies, "
                + "walk beneath the cavernous stone arches of the Plaza Mayor, and descend into the fascinating "
                + "subterranean catacombs of San Francisco Basilica.",
            limaId, cultura, "4h", "Plaza San Martin", new BigDecimal("40.00"),
            List.of(
                new RevDef(2L, 4, "Catacombs tour was creepy but super cool. Nice historical walk.")
            )
        ),
        new ExpDef(
            "Miraflores Food Tour",
            "Savor the world's most acclaimed culinary capital. Walk through beautiful seaside neighborhoods, "
                + "tasting succulent street food, gourmet amazon ingredients, fresh seafood delicacies, "
                + "and gourmet artisanal desserts from Lima's trendiest award-winning bistros.",
            limaId, gastronomia, "3h", "Parque Kennedy", new BigDecimal("55.00"),
            List.of(
                new RevDef(3L, 5, "Lima food is unmatched. Best ceviche ever.")
            )
        ),
        new ExpDef(
            "Lima Bike Tour",
            "Feel the cool ocean breeze as you pedal along the magnificent cliffs of the Costa Verde greenway. "
                + "Explore the beautiful parks of Miraflores and cruise into the bohemian streets of Barranco "
                + "to admire colorful street murals, bridges, and artistic architecture.",
            limaId, deporte, "3h", "Larcomar mall entrance", new BigDecimal("30.00"),
            List.of(
                new RevDef(4L, 5, "Super fun bike ride. Barranco murals are really beautiful.")
            )
        ),
        new ExpDef(
            "Pachacamac Ruins Tour",
            "Step back in time at this sacred pre-Inca pilgrimage complex overlooking the Pacific Ocean. "
                + "Explore massive mud-brick adobe temples, palaces, and pyramid structures built by the ancient "
                + "Ychsma and Wari cultures centuries before the Incas arrived.",
            limaId, cultura, "5h", "Miraflores park", new BigDecimal("50.00"),
            List.of(
                new RevDef(1L, 4, "A great historical archaeological site very close to Lima.")
            )
        ),
        new ExpDef(
            "Colca Canyon Trek",
            "Venture into one of the deepest river canyons in the world. Traverse dry high-altitude plains, "
                + "soak in therapeutic volcanic hot springs, and stand at the Cruz del Condor viewpoint "
                + "to witness majestic Andean condors soaring gracefully on thermal winds.",
            arequipaId, naturaleza, "2 days", "Arequipa main plaza", new BigDecimal("150.00"),
            List.of(
                new RevDef(2L, 5, "Incredible landscape. Seeing the condors fly so close was unforgettable.")
            )
        ),
        new ExpDef(
            "Arequipa City Tour",
            "Stroll through the sparkling 'White City', constructed from pearlescent volcanic sillar stone. "
                + "Explore the vast, colorful walled cloisters of Santa Catalina Monastery, a miniature city "
                + "in itself, and admire majestic views of the Misti and Chachani volcanoes.",
            arequipaId, cultura, "4h", "Plaza de Armas", new BigDecimal("45.00"),
            List.of(
                new RevDef(3L, 5, "Santa Catalina monastery is absolutely stunning. Great city photos!")
            )
        ),
        new ExpDef(
            "Arequipa Picanterias Tour",
            "Delve into the spicy regional cuisine of southern Peru. Visit traditional 'picanteria' kitchens, "
                + "learning about the wood-fired clay stoves, and sample typical specialties like rocoto relleno, "
                + "creamy pastel de papa, and refreshing chicha de jora corn beer.",
            arequipaId, gastronomia, "3h", "Calle Santa Catalina", new BigDecimal("50.00"),
            List.of(
                new RevDef(4L, 5, "Spicy, authentic, and delicious. Real Peruvian country flavors.")
            )
        )
    );

    for (var def : defs) {
      Experience savedExp;
      var existingOpt = experienceRepository.findAll().stream()
          .filter(e -> e.getTitle().equals(def.title)
              && e.getDestinationId().value().equals(def.destinationId))
          .findFirst();

      if (existingOpt.isPresent()) {
        savedExp = existingOpt.get();
        if (savedExp.getDescription().length() < def.description.length()) {
          savedExp.updateInfo(
              def.title, def.description, savedExp.getCategory(),
              savedExp.getDestinationId(), savedExp.getDuration(),
              savedExp.getMeetingPoint()
          );
          experienceRepository.save(savedExp);
        }
      } else {
        var exp = new Experience(
            def.title, def.description, new AgencyId(1L),
            def.category, new DestinationId(def.destinationId),
            def.duration, def.meetingPoint);
        savedExp = experienceRepository.save(exp);

        var avail = new Availability(
            savedExp,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(90),
            50);
        avail.addTicketType(ticketGeneral, def.price, 30);
        avail.addTicketType(ticketVip, def.price.multiply(new BigDecimal("1.5")), 10);
        availabilityRepository.save(avail);
      }

      for (var revDef : def.reviews) {
        UserId userId = new UserId(revDef.userId);
        ExperienceId expId = new ExperienceId(savedExp.getId());
        boolean reviewExists = reviewRepository.findByUserIdAndExperienceId(userId, expId).isPresent();
        if (!reviewExists) {
          Review review = new Review(userId, expId, new Rating(revDef.rating), revDef.comment);
          reviewRepository.save(review);
        }
      }
    }

    var allExperiences = experienceRepository.findAll();
    System.out.println("=== SEEDED EXPERIENCES TOTAL: " + allExperiences.size() + " ===");
    allExperiences.forEach(e ->
        System.out.println("  ID=" + e.getId()
            + " | " + e.getTitle()
            + " | dest=" + e.getDestinationId().value()
            + " | reviewsCount=" + reviewRepository.findAllByExperienceId(new ExperienceId(e.getId())).size()));
  }

  private Long findOrCreateDestination(
      String name, String address, String district, String city, String state, String country) {
    return destinationRepository.findAll().stream()
        .filter(d -> d.getName().name().equals(name))
        .map(Destination::getId)
        .findFirst()
        .orElseGet(() -> {
          var cmd = new CreateDestinationCommand(name, address, district, city, state, country);
          var dest = new Destination(cmd);
          return destinationRepository.save(dest).getId();
        });
  }

  private record RevDef(Long userId, int rating, String comment) {}

  private record ExpDef(
      String title, String description, Long destinationId, Category category,
      String duration, String meetingPoint, BigDecimal price, List<RevDef> reviews) {}
}
