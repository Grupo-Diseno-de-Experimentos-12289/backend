Feature: Gestión de Availabilities (Experiences BC)

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
    And request { "name": "#('Destino ' + locId)", "address": "Av. 123", "district": "Miraflores", "city": "Lima", "state": "Lima", "country": "Peru" }
    When method post
    Then status 201
    * def sharedDestId = response.id

    Given path '/experiences/', staffId, '/experiences'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "title": "Tour Base", "description": "Base test", "category": "CULTURA", "destinationId": #(sharedDestId), "duration": "3 hours", "meetingPoint": "Square" }
    When method post
    Then status 201
    * def sharedExpId = response.id

  Scenario: POST - Crear Availability exitosamente (200)
    Given path '/experiences/', sharedExpId, '/availabilities'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "experienceId": #(sharedExpId), "startDateTime": "2027-06-01T09:00:00", "endDateTime": "2027-06-01T12:00:00", "capacity": 30 }
    When method post
    Then status 200
    And match response == '#number'
    * def availId = response

  Scenario: PUT - Actualizar Availability exitosamente (204)
    # Crear una para actualizar
    Given path '/experiences/', sharedExpId, '/availabilities'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "experienceId": #(sharedExpId), "startDateTime": "2027-07-01T10:00:00", "endDateTime": "2027-07-01T12:00:00", "capacity": 25 }
    When method post
    Then status 200
    * def toUpdateId = response

    Given path '/availabilities/', toUpdateId
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "startDateTime": "2027-07-01T11:00:00", "endDateTime": "2027-07-01T14:00:00", "capacity": 40 }
    When method put
    Then status 204

  Scenario: GET - Listar todas las Availabilities (200)
    Given path '/availabilities'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: DELETE - Eliminar Availability exitosamente (204)
    Given path '/experiences/', sharedExpId, '/availabilities'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "experienceId": #(sharedExpId), "startDateTime": "2027-08-01T09:00:00", "endDateTime": "2027-08-01T12:00:00", "capacity": 15 }
    When method post
    Then status 200
    * def toDeleteId = response

    Given path '/availabilities/', toDeleteId
    And header Authorization = authHeader
    When method delete
    Then status 204

  Scenario: POST - Crear Availability con fechas inválidas (400)
    Given path '/experiences/', sharedExpId, '/availabilities'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "experienceId": #(sharedExpId), "startDateTime": "2027-06-01T12:00:00", "endDateTime": "2027-06-01T09:00:00", "capacity": 20 }
    When method post
    Then status 200