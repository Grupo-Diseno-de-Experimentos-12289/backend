Feature: Users API Tests
  Background:
    * def baseUrl = 'http://localhost:8091/api/v1'
    * url baseUrl
    * header Content-Type = 'application/json'
    * def uuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def uniqueEmail = 'user_' + uuid() + '@example.com'

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
    * def currentUserId = response.id

    Given path '/authentication/sign-in'
    And request { "email": "#(uniqueEmail)", "password": "password123" }
    When method post
    Then status 200
    * def authHeader = 'Bearer ' + response.token

  # Endpoint: GET /users/{id}
  Scenario: Get user by valid ID
    Given path '/users', currentUserId
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == 
    """
    {
      "id": "#(currentUserId)",
      "email": "#string",
      "firstName": "Geo",
      "lastName": "User",
      "phone": "123456789",
      "roles": ["ROLE_TOURIST"],
      "profileType": "##string",
      "avatarUrl": "##string"
    }
    """

  Scenario: Get user by non-existent ID
    Given path '/users/9999999'
    And header Authorization = authHeader
    When method get
    Then status 404

  Scenario: Get user without token
    * header Authorization = null
    Given path '/users', currentUserId
    When method get
    Then status 401

  # Endpoint: GET /users
  Scenario: List all users
    Given path '/users'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match response == '#array'
    And match response[0] contains { id: '#number', email: '#string' }

  Scenario: List all users with invalid token
    * header Authorization = 'Bearer invalid.token.here'
    Given path '/users'
    When method get
    Then status 401

  Scenario: Verify user array structure
    Given path '/users'
    And header Authorization = authHeader
    When method get
    Then status 200
    And match each response == 
    """
    {
      "id": "#number",
      "email": "#string",
      "firstName": "#string",
      "lastName": "#string",
      "phone": "#string",
      "roles": "#array",
      "profileType": "##string",
      "avatarUrl": "##string"
    }
    """

  # Endpoint: PUT /users/{id}
  Scenario: Update user profile successfully
    Given path '/users', currentUserId
    And header Authorization = authHeader
    And request
    """
    {
      "firstName": "UpdatedGeo",
      "lastName": "UpdatedUser",
      "phone": "987654321",
      "profileType": "STANDARD",
      "avatarUrl": "https://example.com/avatar.jpg"
    }
    """
    When method put
    Then status 200
    And match response == 
    """
    {
      "id": "#(currentUserId)",
      "email": "#(uniqueEmail)",
      "firstName": "UpdatedGeo",
      "lastName": "UpdatedUser",
      "phone": "987654321",
      "roles": ["ROLE_TOURIST"],
      "profileType": "STANDARD",
      "avatarUrl": "https://example.com/avatar.jpg"
    }
    """
