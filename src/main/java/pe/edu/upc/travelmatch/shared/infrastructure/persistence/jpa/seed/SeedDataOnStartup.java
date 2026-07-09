package pe.edu.upc.travelmatch.shared.infrastructure.persistence.jpa.seed;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories.DestinationRepository;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.ReviewRepository;

@Service
public class SeedDataOnStartup {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeedDataOnStartup.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DestinationRepository destinationRepository;
    private final CategoryRepository categoryRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final AgencyRepository agencyRepository;
    private final ExperienceRepository experienceRepository;
    private final ReviewRepository reviewRepository;

    public SeedDataOnStartup(
            UserRepository userRepository,
            RoleRepository roleRepository,
            DestinationRepository destinationRepository,
            CategoryRepository categoryRepository,
            TicketTypeRepository ticketTypeRepository,
            AgencyRepository agencyRepository,
            ExperienceRepository experienceRepository,
            ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.destinationRepository = destinationRepository;
        this.categoryRepository = categoryRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.agencyRepository = agencyRepository;
        this.experienceRepository = experienceRepository;
        this.reviewRepository = reviewRepository;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        if (userRepository.count() > 0) {
            LOGGER.info("Seed data already exists, skipping.");
            return;
        }

        LOGGER.info("Seeding demo data...");
        seedRoles();
        seedCategories();
        seedTicketTypes();
        var users = seedUsers();
        var destinations = seedDestinations();
        var agency = seedAgency(users);
        seedExperiences(users, destinations, agency);
        LOGGER.info("Demo data seeding complete.");
    }

    private void seedRoles() {
        for (var role : List.of(Roles.ROLE_ADMIN, Roles.ROLE_AGENCY_STAFF, Roles.ROLE_TOURIST)) {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(role));
            }
        }
    }

    private void seedCategories() {
        for (var cat : List.of(Categories.CULTURA, Categories.GASTRONOMIA, Categories.NATURALEZA, Categories.DEPORTE)) {
            if (categoryRepository.findByName(cat).isEmpty()) {
                categoryRepository.save(new Category(cat));
            }
        }
    }

    private void seedTicketTypes() {
        for (var tt : List.of(TicketTypes.TICKET_GENERAL, TicketTypes.TICKET_VIP)) {
            if (ticketTypeRepository.findByName(tt).isEmpty()) {
                ticketTypeRepository.save(new TicketType(tt));
            }
        }
    }

    private List<User> seedUsers() {
        var encoder = new BCryptPasswordEncoder();
        var touristRole = roleRepository.findByName(Roles.ROLE_TOURIST).orElseThrow();
        var agencyRole = roleRepository.findByName(Roles.ROLE_AGENCY_STAFF).orElseThrow();
        var adminRole = roleRepository.findByName(Roles.ROLE_ADMIN).orElseThrow();

        var u1 = new User("tourist@travelmatch.com", encoder.encode("password123"), "Carlos", "Lopez", "+51 999 111 222", List.of(touristRole));
        u1.verifyEmail();
        var tourist = userRepository.save(u1);

        var u2 = new User("corporate@travelmatch.com", encoder.encode("password123"), "Maria", "Garcia", "+51 999 222 000", List.of(touristRole));
        u2.verifyEmail();
        var corporate = userRepository.save(u2);

        var u3 = new User("staff@agency.com", encoder.encode("password123"), "Ana", "Torres", "+51 1 999 444 555", List.of(agencyRole));
        u3.verifyEmail();
        var staff = userRepository.save(u3);

        var u4 = new User("admin@travelmatch.com", encoder.encode("password123"), "Admin", "User", "+51 1 888 777 000", List.of(adminRole));
        u4.verifyEmail();
        var admin = userRepository.save(u4);

        return List.of(tourist, corporate, staff, admin);
    }

    private List<Destination> seedDestinations() {
        var d1 = destinationRepository.save(new Destination("Cusco", "Plaza de Armas s/n", "Cusco", "Cusco", "Cusco", "Peru"));
        var d2 = destinationRepository.save(new Destination("Lima Centro", "Jr. de la Union 300", "Cercado de Lima", "Lima", "Lima", "Peru"));
        var d3 = destinationRepository.save(new Destination("Arequipa", "Calle Santa Catalina 210", "Cercado", "Arequipa", "Arequipa", "Peru"));
        return List.of(d1, d2, d3);
    }

    private Agency seedAgency(List<User> users) {
        return agencyRepository.save(new Agency(
                new AgencyName("TravelMatch Agency"),
                "Official partner agency for curated travel experiences.",
                "20123456789",
                "contact@travelmatchagency.com",
                "+51 1 555 1234",
                users.getFirst().getId()));
    }

    record TicketDef(TicketType type, BigDecimal price, int stock) {}
    record ExpDef(String title, String desc, Category cat, Destination dest, String dur, String meet,
                  CancellationPolicyType policy, String policyDesc, List<TicketDef> tickets) {}

    private List<TicketDef> gv(TicketType gen, TicketType vip, double gPrice, double vPrice) {
        return List.of(new TicketDef(gen, BigDecimal.valueOf(gPrice), 30), new TicketDef(vip, BigDecimal.valueOf(vPrice), 10));
    }

    private void seedExperiences(List<User> users, List<Destination> destinations, Agency agency) {
        var categoria = categoryRepository.findByName(Categories.CULTURA).orElseThrow();
        var gastronomia = categoryRepository.findByName(Categories.GASTRONOMIA).orElseThrow();
        var naturaleza = categoryRepository.findByName(Categories.NATURALEZA).orElseThrow();
        var deporte = categoryRepository.findByName(Categories.DEPORTE).orElseThrow();
        var gen = ticketTypeRepository.findByName(TicketTypes.TICKET_GENERAL).orElseThrow();
        var vip = ticketTypeRepository.findByName(TicketTypes.TICKET_VIP).orElseThrow();

        var cusco = destinations.get(0);
        var lima = destinations.get(1);
        var arequipa = destinations.get(2);

        var defs = List.of(
            new ExpDef("Machu Picchu Full Day", "Embark on an unforgettable journey to one of the Seven Wonders of the World. Enjoy a scenic train ride through the Sacred Valley, followed by a fully guided tour exploring the legendary Inca citadel ruins, temples, terraces, and panoramic viewpoints.", categoria, cusco, "10h", "Cusco Main Square", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 180, 270)),
            new ExpDef("Sacred Valley Explorer", "Discover the ancient heart of the Inca Empire. Traverse breathtaking Andean valley landscapes, visit the vibrant Pisac artisan market, marvel at the steep agricultural terraces, and tour the impressive stone fortress at Ollantaytambo with our expert historian guide.", categoria, cusco, "8h", "Hotel lobby pickup", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 95, 142.5)),
            new ExpDef("Cusco City Walking Tour", "Unravel the rich colonial and pre-Hispanic tapestry of Cusco. Wander through narrow cobblestone streets, admire imperial Inca stone walls, and visit historic landmarks like the majestic temple of Qorikancha and the bohemian neighborhood of San Blas.", categoria, cusco, "3h", "Plaza de Armas fountain", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 35, 52.5)),
            new ExpDef("Cusco Gastronomic Experience", "Tempt your palate with the rich culinary heritage of the Andes. Learn to prepare classic Peruvian dishes like fresh ceviche, savory lomo saltado, and shake up the perfect pisco sour cocktail using organic ingredients sourced directly from San Pedro Market.", gastronomia, cusco, "4h", "San Pedro Market entrance", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 65, 97.5)),
            new ExpDef("Rainbow Mountain Trek", "Test your limits with a high-altitude expedition to the stunning striped peaks of Vinicunca. Hike along dramatic valleys, see herds of native alpacas, and stand in awe of the surreal natural minerals that dye this sacred mountain in a rainbow of pastel colors.", deporte, cusco, "12h", "Hotel lobby pickup", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 120, 180)),
            new ExpDef("Humantay Lake Trek", "Journey to a hidden turquoise gem tucked beneath the snow-capped Humantay Peak. Trek through crisp alpine trails, breathe in the pure mountain air, and take iconic photos of the crystal-clear glacial waters reflecting the dramatic Andean skies.", naturaleza, cusco, "10h", "Cusco bus terminal", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 85, 127.5)),
            new ExpDef("Lima Historic Center Tour", "Immerse yourself in the colonial grandeur of Lima. Marvel at ornate wooden balconies, walk beneath the cavernous stone arches of the Plaza Mayor, and descend into the fascinating subterranean catacombs of San Francisco Basilica.", categoria, lima, "4h", "Plaza San Martin", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 40, 60)),
            new ExpDef("Miraflores Food Tour", "Savor the world's most acclaimed culinary capital. Walk through beautiful seaside neighborhoods, tasting succulent street food, gourmet amazon ingredients, fresh seafood delicacies, and gourmet artisanal desserts from Lima's trendiest award-winning bistros.", gastronomia, lima, "3h", "Parque Kennedy", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 55, 82.5)),
            new ExpDef("Lima Bike Tour", "Feel the cool ocean breeze as you pedal along the magnificent cliffs of the Costa Verde greenway. Explore the beautiful parks of Miraflores and cruise into the bohemian streets of Barranco to admire colorful street murals, bridges, and artistic architecture.", deporte, lima, "3h", "Larcomar mall entrance", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 30, 45)),
            new ExpDef("Pachacamac Ruins Tour", "Step back in time at this sacred pre-Inca pilgrimage complex overlooking the Pacific Ocean. Explore massive mud-brick adobe temples, palaces, and pyramid structures built by the ancient Ychsma and Wari cultures centuries before the Incas arrived.", categoria, lima, "5h", "Miraflores park", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 50, 75)),
            new ExpDef("Colca Canyon Trek", "Venture into one of the deepest river canyons in the world. Traverse dry high-altitude plains, soak in therapeutic volcanic hot springs, and stand at the Cruz del Condor viewpoint to witness majestic Andean condors soaring gracefully on thermal winds.", naturaleza, arequipa, "2 days", "Arequipa main plaza", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 150, 225)),
            new ExpDef("Arequipa City Tour", "Stroll through the sparkling 'White City', constructed from pearlescent volcanic sillar stone. Explore the vast, colorful walled cloisters of Santa Catalina Monastery, a miniature city in itself, and admire majestic views of the Misti and Chachani volcanoes.", categoria, arequipa, "4h", "Plaza de Armas", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 45, 67.5)),
            new ExpDef("Arequipa Picanterias Tour", "Delve into the spicy regional cuisine of southern Peru. Visit traditional 'picanteria' kitchens, learning about the wood-fired clay stoves, and sample typical specialties like rocoto relleno, creamy pastel de papa, and refreshing chicha de jora corn beer.", gastronomia, arequipa, "3h", "Calle Santa Catalina", CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.", gv(gen, vip, 50, 75))
        );

        for (var def : defs) {
            var exp = experienceRepository.save(new Experience(
                    def.title(), def.desc(), new AgencyId(agency.getId()),
                    def.cat(), new DestinationId(def.dest().getId()),
                    def.dur(), def.meet(), def.policy(), def.policyDesc()));
            var start = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
            var end = start.plusDays(90);
            var avail = new pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability(exp, start, end, 50);
            for (var t : def.tickets()) {
                avail.addTicketType(t.type(), t.price(), t.stock());
            }
            exp.getAvailabilities().add(avail);
            experienceRepository.save(exp);
        }

        int[] userIds = {0, 1, 2, 3};
        String[][] reviewData = {
            {"Machu Picchu Full Day", "An absolute dream come true! The guide was very knowledgeable.", "5", "Extremely well organized. The train views were breathtaking.", "5"},
            {"Sacred Valley Explorer", "A great way to see multiple ruins in a single day. Excellent buffet lunch.", "4", "Highly recommend this tour before heading to Machu Picchu.", "5"},
            {"Cusco City Walking Tour", "A pleasant walking pace. The San Blas neighborhood is charming.", "4", "Incredible history told by a very passionate local guide.", "5"},
            {"Cusco Gastronomic Experience", "Fabulous cooking class! The chef was fun and the food tasted amazing.", "5", null, null},
            {"Rainbow Mountain Trek", "Challenging altitude but the view at the summit is worth every single step.", "5", null, null},
            {"Humantay Lake Trek", "Prettiest lake I have ever seen. Absolutely magical landscape.", "5", null, null},
            {"Lima Historic Center Tour", "Catacombs tour was creepy but super cool. Nice historical walk.", "4", null, null},
            {"Miraflores Food Tour", "Lima food is unmatched. Best ceviche ever.", "5", null, null},
            {"Lima Bike Tour", "Super fun bike ride. Barranco murals are really beautiful.", "5", null, null},
            {"Pachacamac Ruins Tour", "A great historical archaeological site very close to Lima.", "4", null, null},
            {"Colca Canyon Tour", "Incredible landscape. Seeing the condors fly so close was unforgettable.", "5", null, null},
            {"Arequipa City Tour", "Santa Catalina monastery is absolutely stunning. Great city photos!", "5", null, null},
            {"Arequipa Picanterias Tour", "Spicy, authentic, and delicious. Real Peruvian country flavors.", "5", null, null}
        };

        var exps = experienceRepository.findAll();
        for (int i = 0; i < exps.size() && i < reviewData.length; i++) {
            var exp = exps.get(i);
            var rd = reviewData[i];
            var ui = userIds[i % 4];
            var expId = new ExperienceId(exp.getId());
            var uid = new UserId(users.get(ui).getId());
            reviewRepository.save(new Review(uid, expId, new Rating(Integer.parseInt(rd[2])), rd[1]));
            if (rd[4] != null) {
                var ui2 = userIds[(i + 1) % 4];
                var uid2 = new UserId(users.get(ui2).getId());
                reviewRepository.save(new Review(uid2, expId, new Rating(Integer.parseInt(rd[4])), rd[3]));
            }
        }
    }
}