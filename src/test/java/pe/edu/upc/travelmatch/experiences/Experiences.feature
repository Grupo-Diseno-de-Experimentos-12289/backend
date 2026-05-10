Feature: Experiences Bounded Context Testing

  Background:
    * url baseUrl

  # =========================================================
  #  1. ENDPOINTS DE CATEGORIAS (CATEGORIES)
  # =========================================================
  Scenario: GET - Listar todas las categorias (200)
    Given path '/categories'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  # =========================================================
  #  2. ENDPOINTS DE DISPONIBILIDAD (AVAILABILITIES)
  # =========================================================
  Scenario: POST - Crear una Disponibilidad exitosamente (201)
    Given path '/availabilities'
    And header Authorization = authHeader
    And header Content-Type = 'application/json'
    And request
      """
      {
        "experienceId": 1,
        "startDateTime": "2026-10-10T10:00:00",
        "endDateTime": "2026-10-10T14:00:00",
        "capacity": 20
      }
      """
    When method post
    Then status 201

  Scenario: GET - Listar todas las disponibilidades por Experiencia (200)
    Given path '/availabilities/experience/1'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'
