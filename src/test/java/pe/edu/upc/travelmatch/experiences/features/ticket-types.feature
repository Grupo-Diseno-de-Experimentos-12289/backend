Feature: Gestión de Ticket Types (Experiences BC)

  Background:
    * url 'http://localhost:8091/api/v1'

    # Invocamos al ayudante para obtener Token y User ID frescos
    * def authData = call read('classpath:pe/edu/upc/travelmatch/auth-setup.feature')
    * def authHeader = authData.authToken
    * def staffId = authData.newUserId

    # Generadores de datos únicos para que NADA choque entre escenarios
    * def generateUuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def time = function(){ return java.lang.System.currentTimeMillis().toString() }

    * def uniqueId = generateUuid()
    * def uniqueRuc = '20' + time().substring(4, 13)
    * def uniquePhone = '9' + time().substring(5, 13)
    * def randomAgencyName = 'Agencia ' + uniqueId
    * def randomEmail = 'contacto_' + uniqueId + '@travelmatch.pe'


  # =========================================================
  #  1. ENDPOINTS DE TICKET TYPES
  # =========================================================

  Scenario: GET - Listar todos los Ticket Types (200)
    # Prueba Real: GET all ticket types (seeded por el sistema)
    Given path '/ticket-types'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: POST - Asociar Ticket Type a Availability exitosamente (200)
    # Precondición: Crear Destino
    * def destName = 'Destino ' + uniqueId
    Given path '/destinations'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(destName)", "address": "Av. Test 123", "district": "Miraflores", "city": "Lima", "state": "Lima", "country": "Peru" }
    When method post
    Then status 201
    * def destinationId = response.id

    # Precondición: Crear Experience
    Given path '/experiences/', staffId, '/experiences'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "title": "Ticket Type Test", "description": "Test asociación ticket", "category": "CULTURA", "destinationId": #(destinationId), "duration": "2 hours", "meetingPoint": "Cultural Center" }
    When method post
    Then status 201
    * def expId = response.id

    # Precondición: Crear Availability
    Given path '/experiences/', expId, '/availabilities'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "experienceId": #(expId), "startDateTime": "2027-09-01T10:00:00", "endDateTime": "2027-09-01T12:00:00", "capacity": 50 }
    When method post
    Then status 200
    * def availId = response

    # Precondición: Obtener ticket type disponible
    Given path '/ticket-types'
    And header Authorization = authHeader
    When method get
    Then status 200
    * def ticketTypeId = response[0].id

    # Prueba Real: POST asociar ticket type
    Given path '/availabilities/', availId, '/ticket-types'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "availabilityId": #(availId), "ticketTypeId": #(ticketTypeId), "ticketType": "TICKET_GENERAL", "price": 50.00, "stock": 40 }
    When method post
    Then status 200
    And match response == '#string'

  Scenario: POST - Asociar Ticket Type a Availability inexistente (400)
    # Precondición: Obtener ticket type disponible
    Given path '/ticket-types'
    And header Authorization = authHeader
    When method get
    Then status 200
    * def ticketTypeId = response[0].id

    # Prueba Real: POST con availability inexistente
    Given path '/availabilities/999999/ticket-types'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "availabilityId": 999999, "ticketTypeId": #(ticketTypeId), "ticketType": "TICKET_GENERAL", "price": 50.00, "stock": 20 }
    When method post
    Then status 401
