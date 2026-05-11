Feature: Experience Management - Experiences Bounded Context

  Background:
    * url 'http://localhost:8091/api/v1'

    # Configuración básica: crear usuario staff, agencia y destino
    * def uuid  = function(){ return java.util.UUID.randomUUID().toString().replaceAll('-','').substring(0,8) }
    * def time  = function(){ return java.lang.System.currentTimeMillis().toString() }
    * def uid   = uuid()

    # Crear Staff y obtener token
    * def staffEmail = 'staff_' + uid + '@test.pe'
    Given path '/authentication/sign-up'
    And request
    """
    {
      "email":     "#(staffEmail)",
      "password":  "Staff@2024",
      "firstName": "Test",
      "lastName":  "Staff",
      "phone":     "966000001",
      "roles":     ["ROLE_AGENCY_STAFF"]
    }
    """
    When method POST
    Then status 201
    * def staffId = response.id

    Given path '/authentication/sign-in'
    And request { email: '#(staffEmail)', password: 'Staff@2024' }
    When method POST
    Then status 200
    * def authHeader = 'Bearer ' + response.token

    # Crear Agencia
    * def ruc = '20' + time().substring(4, 13)
    Given path '/agencies'
    And header Authorization = authHeader
    And request
    """
    {
      "name":         "#('Agency_' + uid)",
      "description":  "Test Agency",
      "ruc":          "#(ruc)",
      "contactEmail": "#('agency_' + uid + '@test.pe')",
      "contactPhone": "900300001",
      "userId":       #(staffId)
    }
    """
    When method POST
    Then status 201
    * def agencyId = response.id

    # Crear Destino
    Given path '/destinations'
    And header Authorization = authHeader
    And request
    """
    {
      "name":     "#('Destination_' + uid)",
      "address":  "Av. Test 123",
      "district": "Test District",
      "city":     "Lima",
      "state":    "Lima",
      "country":  "Peru"
    }
    """
    When method POST
    Then status 201
    * def destinationId = response.id

  # =========================================================
  #  CREATE EXPERIENCE
  # =========================================================

  Scenario: Crear Experience exitoso - Happy Path (201 Created)
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "City Tour Lima",
      "description":  "Amazing tour through Lima city",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "3 hours",
      "meetingPoint": "Plaza de Armas"
    }
    """
    When method POST
    Then status 201
    And match response.id           == '#number'
    And match response.title        == 'City Tour Lima'
    And match response.description  == 'Amazing tour through Lima city'
    And match response.category     == 'CULTURA'
    And match response.destinationId == destinationId
    And match response.duration     == '3 hours'
    And match response.meetingPoint == 'Plaza de Armas'
    * def createdExperienceId = response.id

  Scenario: Crear Experience con destinationId inexistente (400)
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Invalid Tour",
      "description":  "Tour with invalid destination",
      "category":     "AVENTURA",
      "destinationId": 999999,
      "duration":     "2 hours",
      "meetingPoint": "Unknown Place"
    }
    """
    When method POST
    Then status 400

  Scenario: Crear Experience sin token JWT (401 Unauthorized)
    Given path '/experiences/' + staffId + '/experiences'
    And request
    """
    {
      "title":        "Unauthorized Tour",
      "description":  "Should fail",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "1 hour",
      "meetingPoint": "Plaza Mayor"
    }
    """
    When method POST
    Then status 401

  Scenario: Crear Experience con campos vacíos (400)
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "",
      "description":  "",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "",
      "meetingPoint": ""
    }
    """
    When method POST
    Then status 400

  # =========================================================
  #  GET EXPERIENCES
  # =========================================================

  Scenario: Obtener todas las Experiences (200)
    # Crear una experience primero
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Test Experience",
      "description":  "Experience for GET test",
      "category":     "GASTRONOMIA",
      "destinationId": #(destinationId),
      "duration":     "2 hours",
      "meetingPoint": "Central Market"
    }
    """
    When method POST
    Then status 201

    # Obtener todas
    Given path '/experiences'
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response == '#[]'
    And match response[0].id == '#number'

  Scenario: Obtener Experience por ID (200)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Specific Experience",
      "description":  "Experience to retrieve by ID",
      "category":     "NATURA",
      "destinationId": #(destinationId),
      "duration":     "4 hours",
      "meetingPoint": "Park Entrance"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Obtener por ID
    Given path '/experiences/' + expId
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response.id == expId
    And match response.title == 'Specific Experience'

  Scenario: Obtener Experience inexistente (404)
    Given path '/experiences/999999'
    And header Authorization = authHeader
    When method GET
    Then status 404

  # =========================================================
  #  UPDATE EXPERIENCE
  # =========================================================

  Scenario: Actualizar Experience exitosamente (200)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Original Title",
      "description":  "Original description",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "2 hours",
      "meetingPoint": "Original Point"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Actualizar
    Given path '/experiences/' + expId
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Updated Title",
      "description":  "Updated description",
      "category":     "AVENTURA",
      "destinationId": #(destinationId),
      "duration":     "3 hours",
      "meetingPoint": "Updated Point"
    }
    """
    When method PUT
    Then status 200
    And match response.title == 'Updated Title'
    And match response.description == 'Updated description'
    And match response.category == 'AVENTURA'
    And match response.duration == '3 hours'
    And match response.meetingPoint == 'Updated Point'

  Scenario: Actualizar Experience inexistente (404)
    Given path '/experiences/999999'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Should Fail",
      "description":  "Experience does not exist",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "1 hour",
      "meetingPoint": "Nowhere"
    }
    """
    When method PUT
    Then status 404

  # =========================================================
  #  DELETE EXPERIENCE
  # =========================================================

  Scenario: Eliminar Experience exitosamente (204)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "To Be Deleted",
      "description":  "This will be deleted",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "1 hour",
      "meetingPoint": "Delete Point"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Eliminar
    Given path '/experiences/' + expId
    And header Authorization = authHeader
    When method DELETE
    Then status 204

  # =========================================================
  #  AVAILABILITY MANAGEMENT
  # =========================================================

  Scenario: Crear Availability para una Experience (200)
    # Crear experience primero
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Tour with Availability",
      "description":  "Experience to add availability",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "3 hours",
      "meetingPoint": "Main Square"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Crear availability
    Given path '/experiences/' + expId + '/availabilities'
    And header Authorization = authHeader
    And request
    """
    {
      "experienceId":  #(expId),
      "startDateTime": "2027-06-01T09:00:00",
      "endDateTime":   "2027-06-01T12:00:00",
      "capacity":      30
    }
    """
    When method POST
    Then status 200
    And match response == '#number'
    * def availabilityId = response

  Scenario: Crear Availability con fechas inválidas (400)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Invalid Dates Tour",
      "description":  "Test invalid dates",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "2 hours",
      "meetingPoint": "Test Point"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Intentar crear availability con fecha fin antes de fecha inicio
    Given path '/experiences/' + expId + '/availabilities'
    And header Authorization = authHeader
    And request
    """
    {
      "experienceId":  #(expId),
      "startDateTime": "2027-06-01T12:00:00",
      "endDateTime":   "2027-06-01T09:00:00",
      "capacity":      20
    }
    """
    When method POST
    Then status 400

  Scenario: Obtener todas las Availabilities (200)
    Given path '/availabilities'
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response == '#[]'

  Scenario: Actualizar Availability (204)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Update Availability Test",
      "description":  "Test availability update",
      "category":     "NATURA",
      "destinationId": #(destinationId),
      "duration":     "2 hours",
      "meetingPoint": "Nature Park"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Crear availability
    Given path '/experiences/' + expId + '/availabilities'
    And header Authorization = authHeader
    And request
    """
    {
      "experienceId":  #(expId),
      "startDateTime": "2027-07-01T10:00:00",
      "endDateTime":   "2027-07-01T12:00:00",
      "capacity":      25
    }
    """
    When method POST
    Then status 200
    * def availId = response

    # Actualizar availability
    Given path '/availabilities/' + availId
    And header Authorization = authHeader
    And request
    """
    {
      "startDateTime": "2027-07-01T11:00:00",
      "endDateTime":   "2027-07-01T14:00:00",
      "capacity":      40
    }
    """
    When method PUT
    Then status 204

  Scenario: Eliminar Availability (204)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Delete Availability Test",
      "description":  "Test availability deletion",
      "category":     "AVENTURA",
      "destinationId": #(destinationId),
      "duration":     "3 hours",
      "meetingPoint": "Adventure Base"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Crear availability
    Given path '/experiences/' + expId + '/availabilities'
    And header Authorization = authHeader
    And request
    """
    {
      "experienceId":  #(expId),
      "startDateTime": "2027-08-01T09:00:00",
      "endDateTime":   "2027-08-01T12:00:00",
      "capacity":      15
    }
    """
    When method POST
    Then status 200
    * def availId = response

    # Eliminar
    Given path '/availabilities/' + availId
    And header Authorization = authHeader
    When method DELETE
    Then status 204

  # =========================================================
  #  TICKET TYPE MANAGEMENT
  # =========================================================

  Scenario: Obtener todos los Ticket Types (200)
    Given path '/ticket-types'
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response == '#[]'

  Scenario: Asociar Ticket Type a Availability (200)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Ticket Type Test",
      "description":  "Test ticket type association",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "2 hours",
      "meetingPoint": "Cultural Center"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Crear availability
    Given path '/experiences/' + expId + '/availabilities'
    And header Authorization = authHeader
    And request
    """
    {
      "experienceId":  #(expId),
      "startDateTime": "2027-09-01T10:00:00",
      "endDateTime":   "2027-09-01T12:00:00",
      "capacity":      50
    }
    """
    When method POST
    Then status 200
    * def availId = response

    # Obtener ticket types disponibles
    Given path '/ticket-types'
    And header Authorization = authHeader
    When method GET
    Then status 200
    * def ticketTypeId = response[0].id

    # Asociar ticket type a availability
    Given path '/availabilities/' + availId + '/ticket-types'
    And header Authorization = authHeader
    And request
    """
    {
      "availabilityId": #(availId),
      "ticketTypeId":   #(ticketTypeId),
      "ticketType":     "TICKET_GENERAL",
      "price":          50.00,
      "stock":          40
    }
    """
    When method POST
    Then status 200
    And match response == '#number'

  Scenario: Asociar Ticket Type a Availability inexistente (400)
    # Obtener ticket type
    Given path '/ticket-types'
    And header Authorization = authHeader
    When method GET
    Then status 200
    * def ticketTypeId = response[0].id

    # Intentar asociar a availability que no existe
    Given path '/availabilities/999999/ticket-types'
    And header Authorization = authHeader
    And request
    """
    {
      "availabilityId": 999999,
      "ticketTypeId":   #(ticketTypeId),
      "ticketType":     "TICKET_GENERAL",
      "price":          50.00,
      "stock":          20
    }
    """
    When method POST
    Then status 400

  # =========================================================
  #  EXPERIENCE MEDIA MANAGEMENT
  # =========================================================

  Scenario: Crear Media para una Experience (200)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Media Test Tour",
      "description":  "Tour with media files",
      "category":     "GASTRONOMIA",
      "destinationId": #(destinationId),
      "duration":     "3 hours",
      "meetingPoint": "Food Market"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Crear media
    Given path '/experience-media/experiences/' + expId + '/media'
    And header Authorization = authHeader
    And request
    """
    {
      "mediaUrl": "https://example.com/image1.jpg",
      "caption":  "Beautiful view of the tour"
    }
    """
    When method POST
    Then status 200
    And match response == '#number'
    * def mediaId = response

  Scenario: Obtener todos los Experience Media (200)
    Given path '/experience-media'
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response == '#[]'

  Scenario: Obtener Media por Experience ID (200)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Get Media Test",
      "description":  "Test get media by experience",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "2 hours",
      "meetingPoint": "Museum"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Crear media
    Given path '/experience-media/experiences/' + expId + '/media'
    And header Authorization = authHeader
    And request
    """
    {
      "mediaUrl": "https://example.com/photo.jpg",
      "caption":  "Tour photo"
    }
    """
    When method POST
    Then status 200

    # Obtener media del experience
    Given path '/experience-media/experience/' + expId
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response == '#[]'

  Scenario: Actualizar Experience Media (200)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Update Media Test",
      "description":  "Test media update",
      "category":     "NATURA",
      "destinationId": #(destinationId),
      "duration":     "2 hours",
      "meetingPoint": "Nature Reserve"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Crear media
    Given path '/experience-media/experiences/' + expId + '/media'
    And header Authorization = authHeader
    And request
    """
    {
      "mediaUrl": "https://example.com/old-image.jpg",
      "caption":  "Old caption"
    }
    """
    When method POST
    Then status 200
    * def mediaId = response

    # Actualizar media
    Given path '/experience-media/' + mediaId
    And header Authorization = authHeader
    And request
    """
    {
      "mediaUrl": "https://example.com/new-image.jpg",
      "caption":  "Updated caption"
    }
    """
    When method PUT
    Then status 200
    And match response.mediaUrl == 'https://example.com/new-image.jpg'
    And match response.caption == 'Updated caption'

  Scenario: Eliminar Experience Media (204)
    # Crear experience
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = authHeader
    And request
    """
    {
      "title":        "Delete Media Test",
      "description":  "Test media deletion",
      "category":     "AVENTURA",
      "destinationId": #(destinationId),
      "duration":     "4 hours",
      "meetingPoint": "Adventure Park"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # Crear media
    Given path '/experience-media/experiences/' + expId + '/media'
    And header Authorization = authHeader
    And request
    """
    {
      "mediaUrl": "https://example.com/delete-me.jpg",
      "caption":  "To be deleted"
    }
    """
    When method POST
    Then status 200
    * def mediaId = response

    # Eliminar media
    Given path '/experience-media/' + mediaId
    And header Authorization = authHeader
    When method DELETE
    Then status 204

  # =========================================================
  #  CATEGORIES
  # =========================================================

  Scenario: Obtener todas las Categories (200)
    Given path '/categories'
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response == '#[]'
