Feature: User Login
  User wants to login to the application
  So that user can access the secure area

  Background:
    Given USER is on the login page

  @smoke @regression
  Scenario Outline: Successful login with valid credential
    When USER enter username "<username>"
    And USER enter password "<password>"
    And USER click on login button
    Then USER should see "<expectedResult>"
    Examples:
      | username  | password             | expectedResult                 |
      | practice  | SuperSecretPassword! | You logged into a secure area! |
      | wronguser | wrongpassword        | Your password is invalid!      |

  @regression
  Scenario Outline: Login with empty credential
    When USER enter username "<username>"
    And USER enter password "<password>"
    And USER click on login button
    Then USER should see "<expectedResult>"
    Examples:
      | username | password | expectedResult            |
      |          |          | Your username is invalid! |



