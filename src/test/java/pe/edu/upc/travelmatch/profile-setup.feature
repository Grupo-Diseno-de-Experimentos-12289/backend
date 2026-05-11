Feature: Setup completo para Profiles BC

  Scenario: Crear ecosistema (staff + destino + experiencia + turista) y exportar variables
    * url 'http://localhost:8091/api/v1'
    * def uuid = function(){ return java.util.UUID.randomUUID().toString().replaceAll('-','').substring(0,8) }
    * def time = function(){ return java.lang.System.currentTimeMillis().toString() }
    * def uid  = uuid()

    # ─────────────────────────────────────────────
    # 1. STAFF (para crear experiencia)
    # ─────────────────────────────────────────────
    * def staffEmail = 'staff_' + uid + '@profiles.pe'
    Given path '/authentication/sign-up'
    And request
    """
    {
      "email":     "#(staffEmail)",
      "password":  "Staff@2024",
      "firstName": "Profile",
      "lastName":  "Staff",
      "phone":     "966111222",
      "roles":     ["ROLE_AGENCY_STAFF"]
    }
    """
    When method POST
    Then status 201
    * def staffId = response.id

    Given path '/authentication/sign-in'
    And request { email: '#(staffEmail)', password: 'Staff@2024' }
    When method POST
    Then status 200
    * def staffToken = 'Bearer ' + response.token

    # ─────────────────────────────────────────────
    # 2. DESTINO
    # ─────────────────────────────────────────────
    Given path '/destinations'
    And header Authorization = staffToken
    And request
    """
    {
      "name":     "#('Dest_' + uid)",
      "address":  "Av. Profiles 100",
      "district": "Miraflores",
      "city":     "Lima",
      "state":    "Lima",
      "country":  "Peru"
    }
    """
    When method POST
    Then status 201
    * def destinationId = response.id

    # ─────────────────────────────────────────────
    # 3. EXPERIENCIA (path usa staffId, no agencyId)
    # ─────────────────────────────────────────────
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = staffToken
    And request
    """
    {
      "title":         "Profiles Test Tour",
      "description":   "Experiencia para tests Karate de Profiles",
      "category":      "CULTURA",
      "destinationId": #(destinationId),
      "duration":      "2 hours",
      "meetingPoint":  "Plaza Mayor"
    }
    """
    When method POST
    Then status 201
    * def experienceId = response.id

    # ─────────────────────────────────────────────
    # 4. TURISTA
    # ─────────────────────────────────────────────
    * def touristEmail = 'tourist_' + uid + '@profiles.pe'
    Given path '/authentication/sign-up'
    And request
    """
    {
      "email":     "#(touristEmail)",
      "password":  "Tourist@2024",
      "firstName": "Profile",
      "lastName":  "Tourist",
      "phone":     "977333444",
      "roles":     ["ROLE_TOURIST"]
    }
    """
    When method POST
    Then status 201
    * def touristId = response.id

    Given path '/authentication/sign-in'
    And request { email: '#(touristEmail)', password: 'Tourist@2024' }
    When method POST
    Then status 200
    * def touristToken = 'Bearer ' + response.token
