Feature: Booking Management - Bookings Bounded Context

  Background:
    * url 'http://localhost:8091/api/v1'

    # Invocamos el setup completo (agencia + destino + experiencia + disponibilidad + ticket type + turista)
    * def setupData          = call read('classpath:pe/edu/upc/travelmatch/booking-setup.feature')
    * def authHeader         = setupData.touristToken
    * def dynamicUserId      = setupData.touristId
    * def testAvailabilityId = setupData.generatedAvailabilityId*1
    * def ticketTypeId       = setupData.ticketTypeId

  # =========================================================
  #  CREATE BOOKING
  # =========================================================

  Scenario: Crear Booking exitoso - Happy Path (201 Created)
    * def currentInstant = java.time.Instant.now().toString()
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 201
    And match response.id            == '#number'
    And match response.userId        == dynamicUserId
    And match response.availabilityId == testAvailabilityId
    And match response.quantity      == 1
    And match response.bookingStatus == 'PENDING'
    And match response.totalAmount   == '#number'
    And match response.currency      == 'PEN'
    * def createdBookingId = response.id

  Scenario: Crear Booking con cantidad mayor al stock disponible (400)
    * def currentInstant = java.time.Instant.now().toString()
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       9999,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 400

  Scenario: Crear Booking con availabilityId inexistente (400)
    * def currentInstant = java.time.Instant.now().toString()
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": 999999,
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 400

  Scenario: Crear Booking sin token JWT (401 Unauthorized)
    * def currentInstant = java.time.Instant.now().toString()
    Given path '/bookings'
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 401

  # =========================================================
  #  CANCEL BOOKING
  # =========================================================

  Scenario: Cancelar Booking en estado PENDING (200 - CANCELLED)
    * def currentInstant = java.time.Instant.now().toString()
    # Crear booking
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 201
    * def bookingId = response.id

    # Cancelar
    Given path '/bookings/' + bookingId + '/cancel'
    And header Authorization = authHeader
    And request { userId: #(dynamicUserId), reason: 'Cambié de planes' }
    When method POST
    Then status 200
    And match response.bookingStatus == 'CANCELLED'
    And match response.id == bookingId

  Scenario: Cancelar Booking inexistente (404)
    Given path '/bookings/999999/cancel'
    And header Authorization = authHeader
    And request { userId: #(dynamicUserId), reason: 'Booking fantasma' }
    When method POST
    Then status 404

  Scenario: Cancelar Booking ya CANCELLED - segunda cancelación (400)
    * def currentInstant = java.time.Instant.now().toString()
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 201
    * def bookingId = response.id

    Given path '/bookings/' + bookingId + '/cancel'
    And header Authorization = authHeader
    And request { userId: #(dynamicUserId), reason: 'Primera cancelación' }
    When method POST
    Then status 200

    # Intentar cancelar de nuevo
    Given path '/bookings/' + bookingId + '/cancel'
    And header Authorization = authHeader
    And request { userId: #(dynamicUserId), reason: 'Segunda cancelación - debe fallar' }
    When method POST
    Then status 400

  # =========================================================
  #  FAIL PAYMENT
  # =========================================================

  Scenario: Marcar pago como fallido en Booking PENDING (200)
    * def currentInstant = java.time.Instant.now().toString()
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 201
    * def bookingId = response.id

    Given path '/bookings/fail-payment'
    And header Authorization = authHeader
    And request { bookingId: '#(bookingId)', failureReason: 'Tarjeta rechazada' }
    When method POST
    Then status 200

  Scenario: Marcar pago fallido en Booking inexistente (400)
    Given path '/bookings/fail-payment'
    And header Authorization = authHeader
    And request { bookingId: 999999, failureReason: 'No existe' }
    When method POST
    Then status 400

  # =========================================================
  #  REFUND
  # =========================================================

  Scenario: Iniciar Refund sobre Booking CANCELLED (201 Created)
    * def currentInstant = java.time.Instant.now().toString()
    # Crear y cancelar booking
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 201
    * def bookingId = response.id

    Given path '/bookings/' + bookingId + '/cancel'
    And header Authorization = authHeader
    And request { userId: #(dynamicUserId), reason: 'Solicito reembolso' }
    When method POST
    Then status 200

    # Crear refund
    Given path '/bookings/' + bookingId + '/refunds'
    And header Authorization = authHeader
    And request { refundReason: 'El cliente solicitó reembolso tras cancelar' }
    When method POST
    Then status 201
    And match response.id           == '#number'
    And match response.bookingId    == bookingId
    And match response.refundStatus == 'PENDING'
    And match response.amount       == '#number'
    And match response.currency     == 'PEN'

  Scenario: Iniciar Refund sobre Booking PENDING - debe fallar (400)
    * def currentInstant = java.time.Instant.now().toString()
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 201
    * def bookingId = response.id

    Given path '/bookings/' + bookingId + '/refunds'
    And header Authorization = authHeader
    And request { refundReason: 'Demasiado pronto' }
    When method POST
    Then status 400

  Scenario: Iniciar segundo Refund sobre el mismo Booking - debe fallar (400)
    * def currentInstant = java.time.Instant.now().toString()
    Given path '/bookings'
    And header Authorization = authHeader
    And request
    """
    {
      "userId":         #(dynamicUserId),
      "availabilityId": #(testAvailabilityId),
      "ticketTypeId":   #(ticketTypeId),
      "quantity":       1,
      "bookingDate":    "#(currentInstant)"
    }
    """
    When method POST
    Then status 201
    * def bookingId = response.id

    Given path '/bookings/' + bookingId + '/cancel'
    And header Authorization = authHeader
    And request { userId: #(dynamicUserId), reason: 'Test doble refund' }
    When method POST
    Then status 200

    # Primer refund - ok
    Given path '/bookings/' + bookingId + '/refunds'
    And header Authorization = authHeader
    And request { refundReason: 'Primer reembolso' }
    When method POST
    Then status 201

    # Segundo refund - debe fallar
    Given path '/bookings/' + bookingId + '/refunds'
    And header Authorization = authHeader
    And request { refundReason: 'Segundo reembolso - debe fallar' }
    When method POST
    Then status 400
