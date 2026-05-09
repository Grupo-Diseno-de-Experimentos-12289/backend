@bookings
Feature: Gestión de Reservas (Bookings API)
  Como turista usando TravelMatch
  Quiero poder crear y consultar mis reservas
  Para gestionar mis itinerarios

  Background:
    # baseUrl viene de karate-config.js
    * url baseUrl
    * path '/bookings'
    # Buenas prácticas: Definir cabeceras estándar
    * header Accept = 'application/json'

  Scenario: Crear una reserva exitosamente (Camino Feliz)
    # Arrange: Preparamos el payload basado en tu CreateBookingResource
    * def bookingPayload =
    """
    {
      "userId": 1,
      "availabilityId": 5,
      "ticketTypeId": 3,
      "quantity": 2,
      "total": 250
    }
    """

    # Act: Ejecutamos el POST
    Given request bookingPayload
    When method post

    # Assert: Verificamos el código de estado y el contenido
    Then status 201
    # Validamos que devuelva el ID y que los datos coincidan
    And match response.id == '#present'
    And match response.userId == 1
    And match response.quantity == 2

  Scenario: Intentar crear una reserva con un userId nulo (Camino de Error)
    # En tu backend ya tienes validaciones que lanzan IllegalArgumentException [cite: 139, 140]
    * def invalidPayload =
    """
    {
      "userId": null,
      "availabilityId": 5,
      "ticketTypeId": 3,
      "quantity": 2,
      "total": 250
    }
    """

    Given request invalidPayload
    When method post
    # Debería devolver un 400 Bad Request debido a la validación
    Then status 400