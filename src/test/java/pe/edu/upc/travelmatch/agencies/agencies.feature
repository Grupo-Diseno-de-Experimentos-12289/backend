Feature: Gestión de Agencias, Documentos y Staff (Agencies API)

  Background:
    * url 'http://localhost:8091/api/v1'

    # Invocamos al ayudante para obtener Token y User ID frescos
    * def authData = call read('classpath:pe/edu/upc/travelmatch/auth-setup.feature')
    * def authHeader = authData.authToken
    * def staffId = authData.newUserId

    #  Generadores de datos únicos para que NADA choque entre escenarios
    * def generateUuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def time = function(){ return java.lang.System.currentTimeMillis().toString() }

    * def uniqueId = generateUuid()
    * def uniqueRuc = '20' + time().substring(4, 13)
    * def uniquePhone = '9' + time().substring(5, 13)
    * def randomAgencyName = 'Agencia ' + uniqueId
    * def randomEmail = 'contacto_' + uniqueId + '@travelmatch.pe'


  # =========================================================
  #  1. ENDPOINTS DE AGENCIAS (AGENCIES)
  # =========================================================

  Scenario: POST - Crear una Agencia exitosamente (201)
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request
      """
      {
        "name": "#(randomAgencyName)",
        "description": "Prueba de creación aislada",
        "ruc": "#(uniqueRuc)",
        "contactEmail": "#(randomEmail)",
        "contactPhone": "#(uniquePhone)",
        "userId": #(staffId)
      }
      """
    When method post
    Then status 201
    And match response.name == randomAgencyName

  Scenario: GET - Listar todas las Agencias (200)
    Given path '/agencies'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: GET - Obtener Agencia por ID (200)
    # Precondición: Crear la agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "GET ID Test", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Prueba Real: GET by ID
    Given path '/agencies/', agencyId
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response.id == agencyId

  Scenario: PUT - Actualizar una Agencia exitosamente (200)
    # Precondición: Crear la agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Para actualizar", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Prueba Real: PUT
    * def emailEditado = 'editado_' + randomEmail
    Given path '/agencies/', agencyId
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "Nombre Editado", "description": "Desc Editada", "contactEmail": "#(emailEditado)", "contactPhone": "999111222" }
    When method put
    Then status 200
    And match response.name == 'Nombre Editado'

  Scenario: DELETE - Eliminar una Agencia exitosamente (204)
    # Precondición: Crear la agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Para eliminar", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Prueba Real: DELETE
    Given path '/agencies/', agencyId
    And header Authorization = authHeader
    When method delete
    Then status 204


  # =========================================================
  #  2. ENDPOINTS DE DOCUMENTOS (AGENCY DOCUMENTS)
  # =========================================================

  Scenario: POST - Crear un Documento de Agencia (201)
    # Precondición: Crear Agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Doc Test", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Prueba Real: POST Document
    Given path '/agencies/', agencyId, '/documents'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "documentType": "RUC", "documentUrl": "http://test.com/doc.pdf", "description": "Doc inicial" }
    When method post
    Then status 201
    And match response.documentType == 'RUC'

  Scenario: GET - Listar Documentos por Agencia (200)
    # Precondición: Crear Agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Doc List", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Prueba Real: GET Documents
    Given path '/agencies/', agencyId, '/documents'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: PUT - Actualizar un Documento de Agencia (200)
    # Precondición 1: Crear Agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Doc Update", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Precondición 2: Crear Documento
    Given path '/agencies/', agencyId, '/documents'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "documentType": "RUC", "documentUrl": "http://test.com/doc.pdf", "description": "Antes de edit" }
    When method post
    Then status 201
    * def docId = response.id

    # Prueba Real: PUT Document
    Given path '/agencies/', agencyId, '/documents/', docId
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "id": #(docId), "documentType": "LICENCIA", "documentUrl": "http://test.com/new.pdf", "description": "Editado" }
    When method put
    Then status 200
    And match response.documentType == 'LICENCIA'

  Scenario: DELETE - Eliminar un Documento de Agencia (204)
    # Precondición 1: Crear Agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Doc Delete", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Precondición 2: Crear Documento
    Given path '/agencies/', agencyId, '/documents'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "documentType": "RUC", "documentUrl": "http://test.com/doc.pdf", "description": "A eliminar" }
    When method post
    Then status 201
    * def docId = response.id

    # Prueba Real: DELETE Document
    Given path '/agencies/', agencyId, '/documents/', docId
    And header Authorization = authHeader
    When method delete
    Then status 204


  # =========================================================
  #  3. ENDPOINTS DE STAFF (AGENCY STAFF)
  # =========================================================

  Scenario: POST - Crear Staff de Agencia (201)
    # Precondición: Crear Agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Staff Test", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Prueba Real: POST Staff
    * def staffEmail = 'staff_' + uniqueId + '@test.pe'
    Given path '/agencies/', agencyId, '/staff'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "firstName": "Juan", "lastName": "Perez", "email": "#(staffEmail)", "phone": "987654321", "position": "Guide" }
    When method post
    Then status 201
    And match response.firstName == 'Juan'

  Scenario: GET - Listar Staff por Agencia (200)
    # Precondición: Crear Agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Staff List", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Prueba Real: GET Staff
    Given path '/agencies/', agencyId, '/staff'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: PUT - Actualizar Staff de Agencia (200)
    # Precondición 1: Crear Agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Staff Update", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Precondición 2: Crear Staff
    * def staffEmail = 'staff_' + uniqueId + '@test.pe'
    Given path '/agencies/', agencyId, '/staff'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "firstName": "Juan", "lastName": "Perez", "email": "#(staffEmail)", "phone": "987654321", "position": "Guide" }
    When method post
    Then status 201
    * def staffMemberId = response.id

    # Prueba Real: PUT Staff
    Given path '/agencies/', agencyId, '/staff/', staffMemberId
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "id": #(staffMemberId), "firstName": "Pedro", "lastName": "Perez", "email": "#(staffEmail)", "phone": "987654321", "position": "Manager" }
    When method put
    Then status 200
    And match response.firstName == 'Pedro'

  Scenario: DELETE - Eliminar Staff de Agencia (204)
    # Precondición 1: Crear Agencia
    Given path '/agencies'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "name": "#(randomAgencyName)", "description": "Staff Delete", "ruc": "#(uniqueRuc)", "contactEmail": "#(randomEmail)", "contactPhone": "#(uniquePhone)", "userId": #(staffId) }
    When method post
    Then status 201
    * def agencyId = response.id

    # Precondición 2: Crear Staff
    * def staffEmail = 'staff_' + uniqueId + '@test.pe'
    Given path '/agencies/', agencyId, '/staff'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request { "firstName": "Rosa", "lastName": "Vargas", "email": "#(staffEmail)", "phone": "987654321", "position": "Cook" }
    When method post
    Then status 201
    * def staffMemberId = response.id

    # Prueba Real: DELETE Staff
    Given path '/agencies/', agencyId, '/staff/', staffMemberId
    And header Authorization = authHeader
    When method delete
    Then status 204