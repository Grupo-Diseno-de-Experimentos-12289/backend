Feature: Recomendaciones para viajeros corporativos (Experiences BC)

  Background:
    * url 'http://localhost:8091/api/v1'
    * def authData = call read('classpath:pe/edu/upc/travelmatch/auth-setup.feature')
    * def authHeader = authData.authToken
    * def staffId = authData.newUserId
    * def generateUuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def time = function(){ return java.lang.System.currentTimeMillis().toString() }

    * def locId = generateUuid()
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#('Agencia ' + locId)", "description": "Agencia test", "ruc": "#('20' + time().substring(4, 13))", "contactEmail": "#('c' + locId + '@tm.pe')", "contactPhone": "999888777", "userId": #(staffId) }
    When method post
    Then status 201

    Given path '/destinations'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#('Destino ' + locId)", "address": "Av. 123", "district": "San Isidro", "city": "Lima", "state": "Lima", "country": "Peru" }
    When method post
    Then status 201
    * def sharedDestId = response.id

    # Experience corta, ideal para la hora de almuerzo de un viajero corporativo
    Given path '/experiences/', staffId, '/experiences'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "title": "Lunch Break Gastronomic Tour", "description": "Short tour near the business district", "category": "GASTRONOMIA", "destinationId": #(sharedDestId), "duration": "1 hour", "meetingPoint": "Business Tower Lobby", "cancellationPolicyType": "FLEXIBLE", "cancellationPolicyDescription": "Free cancellation up to 2 hours before." }
    When method post
    Then status 201
    * def sharedExpId = response.id

    # Availability que calza dentro de una ventana de almuerzo (12:00 - 14:00)
    Given path '/experiences/', sharedExpId, '/availabilities'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "experienceId": #(sharedExpId), "startDateTime": "2027-10-01T12:30:00", "endDateTime": "2027-10-01T13:30:00", "capacity": 10 }
    When method post
    Then status 200
    * def availId = response

    Given path '/ticket-types'
    And header Authorization = authHeader
    When method get
    Then status 200
    * def ticketTypeId = response[0].id

    Given path '/availabilities/', availId, '/ticket-types'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "availabilityId": #(availId), "ticketTypeId": #(ticketTypeId), "ticketType": "TICKET_GENERAL", "price": 30.00, "stock": 5 }
    When method post
    Then status 200

  Scenario: GET - Recomendar experiencias por ubicacion, intereses y horario libre (200)
    Given path '/experiences/recommendations/corporate'
    And header Authorization = authHeader
    And param destinationId = sharedDestId
    And param interests = 'GASTRONOMIA'
    And param windowStart = '2027-10-01T12:00:00'
    And param windowEnd = '2027-10-01T14:00:00'
    When method get
    Then status 200
    And match response == '#array'
    And match response[*].experienceId contains sharedExpId

  Scenario: GET - No recomienda experiencias fuera de la ventana horaria del viajero (200, vacio)
    Given path '/experiences/recommendations/corporate'
    And header Authorization = authHeader
    And param destinationId = sharedDestId
    And param interests = 'GASTRONOMIA'
    And param windowStart = '2027-10-01T16:00:00'
    And param windowEnd = '2027-10-01T18:00:00'
    When method get
    Then status 200
    And match response[*].experienceId !contains sharedExpId

  Scenario: GET - No recomienda experiencias de un interes distinto (200, vacio)
    Given path '/experiences/recommendations/corporate'
    And header Authorization = authHeader
    And param destinationId = sharedDestId
    And param interests = 'DEPORTE'
    And param windowStart = '2027-10-01T12:00:00'
    And param windowEnd = '2027-10-01T14:00:00'
    When method get
    Then status 200
    And match response[*].experienceId !contains sharedExpId
