Feature: Destinations Controller API Tests
  As a client of the API
  I want to manage destinations
  So that I can assign them to experiences

  Background:
    * def baseUrl = 'http://localhost:8091/api/v1'
    * url baseUrl
    * header Content-Type = 'application/json'
    * def uuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def uniqueEmail = 'geo_' + uuid() + '@test.com'
    * def uniqueName = 'Dest_' + uuid()

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

  # Endpoint: POST /destinations
  Scenario: Create a new destination successfully
    Given path '/destinations'
    And header Authorization = authHeader
    And request 
    """
    { 
      "name": "#(uniqueName)", 
      "address": "Av. 123", 
      "district": "Miraflores", 
      "city": "Lima", 
      "state": "Lima", 
      "country": "Peru" 
    }
    """
    When method post
    Then status 201
    And match response == 
    """
    {
      "id": "#number",
      "name": "#(uniqueName)",
      "address": "Av. 123",
      "district": "Miraflores",
      "city": "Lima",
      "state": "Lima",
      "country": "Peru"
    }
    """

  Scenario: Create a destination with missing required fields
    Given path '/destinations'
    And header Authorization = authHeader
    And request 
    """
    { 
      "address": "Av. 123", 
      "district": "Miraflores", 
      "city": "Lima", 
      "state": "Lima", 
      "country": "Peru" 
    }
    """
    When method post
    Then status 401

  Scenario: Create destination with special characters in name
    Given path '/destinations'
    And header Authorization = authHeader
    And request 
    """
    { 
      "name": "#(uniqueName + ' & Machu Picchu - 2026!')", 
      "address": "Centro", 
      "district": "Cusco", 
      "city": "Cusco", 
      "state": "Cusco", 
      "country": "Peru" 
    }
    """
    When method post
    Then status 201

  # Endpoint: GET /destinations
  Scenario: List all destinations
    Given path '/destinations'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'

  Scenario: List destinations with invalid token
    * header Authorization = 'Bearer invalid'
    Given path '/destinations'
    When method get
    Then status 401

  Scenario: Verify destination array structure
    Given path '/destinations'
    And header Authorization = authHeader
    And request 
    """
    { 
      "name": "#(uniqueName + '2')", 
      "address": "Av. 123", 
      "district": "Miraflores", 
      "city": "Lima", 
      "state": "Lima", 
      "country": "Peru" 
    }
    """
    When method post
    Then status 201

    Given path '/destinations'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response[0] ==
    """
    {
      "id": "#number",
      "name": "#string",
      "address": "#string",
      "district": "#string",
      "city": "#string",
      "state": "#string",
      "country": "#string"
    }
    """

  # Endpoint: GET /destinations/{id}
  Scenario: Get destination by valid ID
    Given path '/destinations'
    And header Authorization = authHeader
    And request 
    """
    { 
      "name": "#(uniqueName + '3')", 
      "address": "Av. 123", 
      "district": "Miraflores", 
      "city": "Lima", 
      "state": "Lima", 
      "country": "Peru" 
    }
    """
    When method post
    Then status 201
    * def createdId = response.id

    Given path '/destinations', createdId
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response.id == createdId

  Scenario: Get destination with non-existent ID
    Given path '/destinations/9999999'
    And header Authorization = authHeader
    When method get
    Then status 400
