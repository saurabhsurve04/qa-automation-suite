Feature: User Login
  User wants to login to the application
  So that user can access the secure area

  Scenario Outline: Successful login with valid credentials
    Given USER is on the login page
    When USER enter username "<username>"
    And USER enter password "<password>"
    And USER click on login button
    Then USER should see "<expectedResult>"
    Examples:
      | username  | password             | expectedResult                 |
      | practice  | SuperSecretPassword! | You logged into a secure area! |
      | wronguser | wrongpassword        | Your password is invalid!      |



