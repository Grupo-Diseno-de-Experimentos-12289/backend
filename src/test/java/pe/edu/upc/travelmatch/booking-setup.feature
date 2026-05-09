Feature: Setup completo para Bookings

  Scenario: Crear ecosistema completo y exportar variables
    * url 'http://localhost:8091/api/v1'
    * def uuid  = function(){ return java.util.UUID.randomUUID().toString().replaceAll('-','').substring(0,8) }
    * def time  = function(){ return java.lang.System.currentTimeMillis().toString() }
    * def uid   = uuid()

    # ─────────────────────────────────────────────────────────────
    # 1. CREAR STAFF Y LOGUEARSE
    # ─────────────────────────────────────────────────────────────
    * def staffEmail = 'staff_' + uid + '@setup.pe'
    Given path '/authentication/sign-up'
    And request
    """
    {
      "email":     "#(staffEmail)",
      "password":  "Exp@2024",
      "firstName": "Exp",
      "lastName":  "Staff",
      "phone":     "966000001",
      "roles":     ["ROLE_AGENCY_STAFF"]
    }
    """
    When method POST
    Then status 201
    * def staffId = response.id

    Given path '/authentication/sign-in'
    And request { email: '#(staffEmail)', password: 'Exp@2024' }
    When method POST
    Then status 200
    * def staffToken = 'Bearer ' + response.token

    # ─────────────────────────────────────────────────────────────
    # 2. CREAR AGENCIA
    # El RUC debe tener exactamente 11 dígitos.
    # ─────────────────────────────────────────────────────────────
    * def ruc = '20' + time().substring(4, 13)
    Given path '/agencies'
    And header Authorization = staffToken
    And request
    """
    {
      "name":         "#('Agency_' + uid)",
      "description":  "Agencia para pruebas automatizadas",
      "ruc":          "#(ruc)",
      "contactEmail": "#('agc_' + uid + '@test.pe')",
      "contactPhone": "900300001",
      "userId":       #(staffId)
    }
    """
    When method POST
    Then status 201
    * def agencyId = response.id

    # ─────────────────────────────────────────────────────────────
    # 3. CREAR DESTINO
    # ─────────────────────────────────────────────────────────────
    Given path '/destinations'
    And header Authorization = staffToken
    And request
    """
    {
      "name":     "#('Dest_' + uid)",
      "address":  "Av. Larco 100",
      "district": "Miraflores",
      "city":     "Lima",
      "state":    "Lima",
      "country":  "Peru"
    }
    """
    When method POST
    Then status 201
    * def destinationId = response.id

    # ─────────────────────────────────────────────────────────────
    # 4. CREAR EXPERIENCIA
    # OJO: el path usa staffId (userId del staff), NO agencyId.
    # El backend valida que ese userId tenga ROLE_AGENCY_STAFF.
    # ─────────────────────────────────────────────────────────────
    Given path '/experiences/' + staffId + '/experiences'
    And header Authorization = staffToken
    And request
    """
    {
      "title":        "Historic Lima Tour",
      "description":  "Tour E2E automatizado",
      "category":     "CULTURA",
      "destinationId": #(destinationId),
      "duration":     "3 hours",
      "meetingPoint": "Plaza Mayor"
    }
    """
    When method POST
    Then status 201
    * def expId = response.id

    # ─────────────────────────────────────────────────────────────
    # 5. CREAR DISPONIBILIDAD
    # El endpoint devuelve directamente el ID (Long), no un objeto.
    # ─────────────────────────────────────────────────────────────
    Given path '/experiences/' + expId + '/availabilities'
    And header Authorization = staffToken
    And request
    """
    {
      "experienceId":  #(expId),
      "startDateTime": "2027-01-01T09:00:00",
      "endDateTime":   "2027-01-01T12:00:00",
      "capacity":      50
    }
    """
    When method POST
    Then status 200
    * def generatedAvailabilityId = response

    # ─────────────────────────────────────────────────────────────
    # 6. OBTENER ID REAL DEL TICKET TYPE DESDE EL SERVIDOR
    # No asumimos ID=1 porque es auto-generado por el seeder.
    # Tomamos el primer ticket (TICKET_GENERAL).
    # ─────────────────────────────────────────────────────────────
    Given path '/ticket-types'
    And header Authorization = staffToken
    When method GET
    Then status 200
    And match response == '#[]'
    * def ticketTypeId = response[0].id

    # ─────────────────────────────────────────────────────────────
    # 7. ASIGNAR TICKET TYPE A LA DISPONIBILIDAD  ← EL PASO CRÍTICO
    # Sin esto, el booking falla con "TicketType not found" porque
    # ExperiencesContextFacade no encuentra precio ni stock.
    # ─────────────────────────────────────────────────────────────
    Given path '/availabilities/' + generatedAvailabilityId + '/ticket-types'
    And header Authorization = staffToken
    And request
    """
    {
      "availabilityId": #(generatedAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "ticketType":     "TICKET_GENERAL",
      "price":          75.00,
      "stock":          30
    }
    """
    When method POST
    Then status 200

    # ─────────────────────────────────────────────────────────────
    # 8. CREAR TURISTA COMPRADOR
    # ─────────────────────────────────────────────────────────────
    * def touristEmail = 'tourist_' + uid + '@travelmatch.pe'
    Given path '/authentication/sign-up'
    And request
    """
    {
      "email":     "#(touristEmail)",
      "password":  "Tourist@2024",
      "firstName": "Turista",
      "lastName":  "Comprador",
      "phone":     "977000002",
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
