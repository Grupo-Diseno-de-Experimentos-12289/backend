Feature: Gestión de Experience Media (Experiences BC)

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
    And request { "name": "#('Destino ' + locId)", "address": "Av. Test 123", "district": "Miraflores", "city": "Lima", "state": "Lima", "country": "Peru" }
    When method post
    Then status 201
    * def sharedDestId = response.id

    Given path '/experiences/', staffId, '/experiences'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "title": "Media Tour Base", "description": "Tour con archivos media", "category": "GASTRONOMIA", "destinationId": #(sharedDestId), "duration": "3 hours", "meetingPoint": "Food Market" }
    When method post
    Then status 201
    * def sharedExpId = response.id

  Scenario: POST - Crear Media para una Experience (200)
    Given path '/experience-media/experiences/', sharedExpId, '/media'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "mediaUrl": "https://example.com/image1.jpg", "caption": "Beautiful view of the tour" }
    When method post
    Then status 200
    And match response == '#regex \\d+'

  Scenario: GET - Listar todos los Experience Media (200)
    Given path '/experience-media'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: GET - Obtener Media por Experience ID (200)
    Given path '/experience-media/experiences/', sharedExpId, '/media'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "mediaUrl": "https://example.com/photo.jpg", "caption": "Tour photo" }
    When method post
    Then status 200

    Given path '/experience-media/experience/', sharedExpId
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: PUT - Actualizar Experience Media exitosamente (200)
    Given path '/experience-media/experiences/', sharedExpId, '/media'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "mediaUrl": "https://example.com/old-image.jpg", "caption": "Old caption" }
    When method post
    Then status 200
    * def mediaId = response

    Given path '/experience-media/', mediaId
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "mediaUrl": "https://example.com/new-image.jpg", "caption": "Updated caption" }
    When method put
    Then status 200
    And match response.mediaUrl == 'https://example.com/new-image.jpg'
    And match response.caption  == 'Updated caption'

  Scenario: DELETE - Eliminar Experience Media exitosamente (204)
    Given path '/experience-media/experiences/', sharedExpId, '/media'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "mediaUrl": "https://example.com/delete-me.jpg", "caption": "To be deleted" }
    When method post
    Then status 200
    * def mediaId = response

    Given path '/experience-media/', mediaId
    And header Authorization = authHeader
    When method delete
    Then status 204