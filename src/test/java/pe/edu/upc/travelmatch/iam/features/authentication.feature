Feature: Authentication API Tests
  Background:
    * def baseUrl = 'http://localhost:8091/api/v1'
    * url baseUrl
    * header Content-Type = 'application/json'
    * def uuid = function(){ return java.util.UUID.randomUUID().toString().substring(0,8) }
    * def uniqueEmail = 'auth_' + uuid() + '@example.com'

  # Endpoint: POST /authentication/sign-up
  Scenario: Register a new user successfully
    Given path '/authentication/sign-up'
    And request 
    """
    { 
      "email": "#(uniqueEmail)", 
      "password": "Password123!", 
      "firstName": "Alice", 
      "lastName": "Wonder", 
      "phone": "987654321", 
      "roles": ["ROLE_TOURIST"] 
    }
    """
    When method post
    Then status 201
    And match response == 
    """
    {
      "id": "#number",
      "email": "#(uniqueEmail)",
      "firstName": "Alice",
      "lastName": "Wonder",
      "phone": "987654321",
      "roles": ["ROLE_TOURIST"]
    }
    """

  Scenario: Register with an already existing email
    Given path '/authentication/sign-up'
    And request 
    """
    { 
      "email": "#(uniqueEmail)", 
      "password": "Password123!", 
      "firstName": "Alice", 
      "lastName": "Wonder", 
      "phone": "987654321", 
      "roles": ["ROLE_TOURIST"] 
    }
    """
    When method post
    Then status 201

    Given path '/authentication/sign-up'
    And request 
    """
    { 
      "email": "#(uniqueEmail)", 
      "password": "Password123!", 
      "firstName": "Bob", 
      "lastName": "Builder", 
      "phone": "123456789", 
      "roles": ["ROLE_TOURIST"] 
    }
    """
    When method post
    Then status 401

  Scenario: Register a user with multiple roles
    * def multiRoleEmail = 'multi_' + uniqueEmail
    Given path '/authentication/sign-up'
    And request 
    """
    { 
      "email": "#(multiRoleEmail)", 
      "password": "Password123!", 
      "firstName": "Charlie", 
      "lastName": "Chaplin", 
      "phone": "111222333", 
      "roles": ["ROLE_TOURIST", "ROLE_AGENCY_STAFF"] 
    }
    """
    When method post
    Then status 201
    And match response.roles contains "ROLE_TOURIST"

  # Endpoint: POST /authentication/sign-in
  Scenario: Authenticate with correct credentials
    Given path '/authentication/sign-up'
    And request 
    """
    { 
      "email": "#(uniqueEmail)", 
      "password": "Password123!", 
      "firstName": "Alice", 
      "lastName": "Wonder", 
      "phone": "987654321", 
      "roles": ["ROLE_TOURIST"] 
    }
    """
    When method post
    Then status 201

    Given path '/authentication/sign-in'
    And request { "email": "#(uniqueEmail)", "password": "Password123!" }
    When method post
    Then status 200
    And match response == 
    """
    {
      "id": "#number",
      "email": "#(uniqueEmail)",
      "token": "#notnull",
      "roles": "#array"
    }
    """

  Scenario: Authenticate with wrong password
    Given path '/authentication/sign-up'
    And request 
    """
    { 
      "email": "#(uniqueEmail)", 
      "password": "Password123!", 
      "firstName": "Alice", 
      "lastName": "Wonder", 
      "phone": "987654321", 
      "roles": ["ROLE_TOURIST"] 
    }
    """
    When method post
    Then status 201

    Given path '/authentication/sign-in'
    And request { "email": "#(uniqueEmail)", "password": "WrongPassword!" }
    When method post
    Then status 401

  Scenario: Authenticate with non-existent email
    Given path '/authentication/sign-in'
    And request { "email": "doesnotexist@example.com", "password": "Password123!" }
    When method post
    Then status 401
