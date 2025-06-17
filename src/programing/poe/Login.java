package programing.poe;

public class Login {
    private String registeredUsername;
    private String registeredPassword;
    private String firstName;
    private String lastName;
    
    //Username validation
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }
    
    //Password validation
    public boolean checkPasswordComplexity(String password) {
        return password.length() >+ 8 && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*()].*");
    }
    
    //Phone number validation
    public boolean checkCellPhoneNumber(String phone) {
        return phone.matches("^(\\+27|27|0)[0-9]{9}$");
    }
    
    //Registration method
    public String registerUser(String username, String password, String phone, String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is incorrecty formatted.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password does not meet the complexity requirements.";
        }
        if (!checkCellPhoneNumber(phone)) {
            return "Phone number is incorrectly formatted";
        }
        
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.firstName = firstName;
        this.lastName = lastName;
        
        return "User registered successfully";
        
    }
    
    //Login Validation
    public boolean loginUser(String inputUsername, String inputPassword) {
        return inputUsername.equals(registeredUsername) && inputPassword.equals(registeredPassword);
    }
    
    //Login status message
    public String returnLoginStatus(String firstName, String lastName, boolean success) {
        if (success) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
