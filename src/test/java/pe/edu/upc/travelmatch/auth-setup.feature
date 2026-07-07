Feature: Setup de Autenticación Reutilizable - Profiles BC

  Scenario: Crear usuario turista y obtener token
    * url 'http://localhost:8091/api/v1'

    # 1. Email único por corrida
    * def uuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def uniqueEmail = 'tourist_' + uuid() + '@travelmatch.pe'

    # 2. Sign-Up
    Given path '/authentication/sign-up'
    And request
    """
    {
      "email":     "#(uniqueEmail)",
      "password":  "password123",
      "firstName": "Karate",
      "lastName":  "Bot",
      "phone":     "999000111",
      "roles":     ["ROLE_AGENCY_STAFF"]
    }
    """
    When method post
    Then status 201
    * def newUserId = response.id

    # 3. Sign-In
    Given path '/authentication/sign-in'
    And request { "email": "#(uniqueEmail)", "password": "password123" }
    When method post
    Then status 200

    # 4. Header listo para exportar
    * def authToken = 'Bearer ' + response.token