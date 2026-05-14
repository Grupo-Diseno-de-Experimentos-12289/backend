Feature: Gestión de Experiences (Experiences BC)

  Background:
    * url 'http://localhost:8091/api/v1'
    * def generateUuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def time = function(){ return java.lang.System.currentTimeMillis().toString() }

    # Setup de Auth
  * def scenarioId = generateUuid()
  * def authData = call read('classpath:pe/edu/upc/travelmatch/auth-setup.feature') { scenarioId: '#(scenarioId)' }
  * def authHeader = authData.authToken
  * def staffId = authData.newUserId

    # Setup de dependencias
    * def locId = generateUuid()

    Given path 'agencies'
    And header Authorization = authHeader
    And request { "name": "#('Agencia ' + locId)", "description": "Base", "ruc": "#('20' + time().substring(4, 13))", "contactEmail": "#('c' + locId + '@tm.pe')", "contactPhone": "999888777", "userId": #(staffId) }
    When method post
    Then status 201

    Given path 'destinations'
    And header Authorization = authHeader
    And request { "name": "#('Destino ' + locId)", "address": "Av. Test 123", "district": "Miraflores", "city": "Lima", "state": "Lima", "country": "Peru" }
    When method post
    Then status 201
    * def sharedDestId = response.id

  Scenario: POST - Crear Experience exitosamente (201)
    Given path 'experiences', staffId, 'experiences'
    And header Authorization = authHeader
    And request { "title": "City Tour Lima", "description": "Amazing tour", "category": "CULTURA", "destinationId": #(sharedDestId), "duration": "3 hours", "meetingPoint": "Plaza de Armas" }
    When method post
    Then status 201
    And match response.id == '#number'

  Scenario: GET - Listar todas las Experiences (200)
    Given path 'experiences'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: GET - Obtener Experience por ID (200)
    # 1. Crear con el staffId del Background (tiene ROLE_AGENCY_STAFF)
    Given path 'experiences', staffId, 'experiences'
    And header Authorization = authHeader
    And request { "title": "Specific", "description": "Test", "category": "NATURA", "destinationId": #(sharedDestId), "duration": "4 hours", "meetingPoint": "Park" }
    When method post
    Then status 201
    * def expId = response.id

    # 2. Obtener por ID
    Given path 'experiences', expId
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response.id == expId

  Scenario: GET - Obtener Experience con ID inexistente (404)
    Given path 'experiences', 999999
    And header Authorization = authHeader
    When method get
    Then status 404

  Scenario: PUT - Actualizar Experience exitosamente (200)
    # 1. Crear con el staffId del Background
    Given path 'experiences', staffId, 'experiences'
    And header Authorization = authHeader
    And request { "title": "Old", "description": "Old", "category": "CULTURA", "destinationId": #(sharedDestId), "duration": "2 hours", "meetingPoint": "Old" }
    When method post
    Then status 201
    * def expId = response.id

    # 2. Actualizar
    Given path 'experiences', expId
    And header Authorization = authHeader
    And request { "title": "Updated Title", "description": "Updated description", "category": "AVENTURA", "destinationId": #(sharedDestId), "duration": "3 hours", "meetingPoint": "Updated Point" }
    When method put
    Then status 200
    And match response.title == 'Updated Title'

  Scenario: DELETE - Eliminar Experience exitosamente (204)
    Given path 'experiences', staffId, 'experiences'
    And header Authorization = authHeader
    And request { "title": "Delete", "description": "Delete", "category": "CULTURA", "destinationId": #(sharedDestId), "duration": "1 hour", "meetingPoint": "Delete" }
    When method post
    Then status 201
    * def expId = response.id

    Given path 'experiences', expId
    And header Authorization = authHeader
    When method delete
    Then status 204

  Scenario: POST - Crear Experience sin token JWT (401)
    Given path 'experiences', staffId, 'experiences'
    # Sin header Authorization
    And request { "title": "No Token", "description": "Fail", "category": "CULTURA", "destinationId": #(sharedDestId), "duration": "1 hour", "meetingPoint": "Plaza" }
    When method post
    Then status 401

  Scenario: POST - Crear Experience con destinationId inexistente (401)
    Given path 'experiences', staffId, 'experiences'
    And header Authorization = authHeader
    And request { "title": "Invalid Dest", "description": "Fail", "category": "AVENTURA", "destinationId": 999999, "duration": "2 hours", "meetingPoint": "Unknown" }
    When method post
    Then status 401

  Scenario: POST - Crear Experience con campos vacíos (401)
    Given path 'experiences', staffId, 'experiences'
    And header Authorization = authHeader
    And request { "title": "", "description": "", "category": "CULTURA", "destinationId": #(sharedDestId), "duration": "", "meetingPoint": "" }
    When method post
    Then status 401