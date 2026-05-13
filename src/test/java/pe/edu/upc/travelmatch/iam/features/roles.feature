Feature: Roles API Tests
  Background:
    * def baseUrl = 'http://localhost:8091/api/v1'
    * url baseUrl
    * header Content-Type = 'application/json'
    * def uuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def uniqueEmail = 'role_' + uuid() + '@example.com'

    # Get Token
    Given path '/authentication/sign-up'
    And request 
    """
    { 
      "email": "#(uniqueEmail)", 
      "password": "password123", 
      "firstName": "Geo", 
      "lastName": "User", 
      "phone": "123456789", 
      "roles": ["ROLE_TOURIST"] 
    }
    """
    When method post
    Then status 201

    Given path '/authentication/sign-in'
    And request { "email": "#(uniqueEmail)", "password": "password123" }
    When method post
    Then status 200
    * def authHeader = 'Bearer ' + response.token

  # Endpoint: GET /roles
  Scenario: List all roles
    Given path '/roles'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'
    And match response[0] == 
    """
    {
      "id": "#number",
      "name": "#string"
    }
    """

  Scenario: Try to list roles without token
    * header Authorization = null
    Given path '/roles'
    When method get
    Then status 401

  Scenario: List roles with invalid token
    * header Authorization = 'Bearer this.is.invalid'
    Given path '/roles'
    When method get
    Then status 401
