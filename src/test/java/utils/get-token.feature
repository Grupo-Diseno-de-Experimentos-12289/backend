@ignore
Feature: Obtener token de autenticación

  Scenario: realizar login y retornar token
    Given url authConfig.url
    And request authConfig.payload
    When method post
    Then status 200
    * def token = response.token   # asume que tu endpoint devuelve { "token": "jwt..." }
    * print 'Token obtenido:', token