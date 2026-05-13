Feature: Consulta de Categories (Experiences BC)

  Background:
    * url 'http://localhost:8091/api/v1'

    # Invocamos al ayudante para obtener Token y User ID frescos
    * def authData = call read('classpath:pe/edu/upc/travelmatch/auth-setup.feature')
    * def authHeader = authData.authToken
    * def staffId = authData.newUserId


  # =========================================================
  #  1. ENDPOINTS DE CATEGORIES
  # =========================================================

  Scenario: GET - Listar todas las Categories (200)
    # Prueba Real: GET all categories (seeded por el sistema)
    Given path '/categories'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: GET - Listar Categories sin token JWT (401)
    # Prueba Real: GET sin autenticación
    Given path '/categories'
    When method get
    Then status 401
