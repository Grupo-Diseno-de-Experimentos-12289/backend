Feature: Setup de Autenticación Reutilizable

  Scenario: Crear usuario de staff y obtener token
    * url 'http://localhost:8091/api/v1'

    # 1. Generamos email único
    * def uuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def uniqueEmail = 'staff_' + uuid() + '@travelmatch.pe'

    # 2. Registramos al usuario (Sign-Up)
    Given path '/authentication/sign-up'
    And request { "firstName": "Karate", "lastName": "Bot", "email": "#(uniqueEmail)", "password": "password123", "roles": ["ROLE_AGENCY_STAFF"] }
    When method post
    Then status 201
    * def newUserId = response.id

    # 3. Iniciamos sesión (Sign-In)
    Given path '/authentication/sign-in'
    And request { "email": "#(uniqueEmail)", "password": "password123" }
    When method post
    Then status 200

    # 4. Preparamos el Header que exportaremos
    * def authToken = 'Bearer ' + response.token