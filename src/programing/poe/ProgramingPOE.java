package programing.poe;

import java.util.Scanner;
import javax.swing.JOptionPane;

public class ProgramingPOE {

    public static void main(String[] args) {
        Login login = new Login();
        Message.loadStoredMessagesFromJSON();
        
        String username = JOptionPane.showInputDialog("Please create a username:");
        String password = JOptionPane.showInputDialog("Please create a password:");
        String phoneNumber = JOptionPane.showInputDialog("Please enter a phone number:");
        String firstName = JOptionPane.showInputDialog("Enter your name");
        String lastName = JOptionPane.showInputDialog("Enter your last name");
        
        String registerMessage = login.registerUser(username, password, phoneNumber, firstName, lastName);
        JOptionPane.showMessageDialog(null, registerMessage);

        boolean loginSuccess = false;
        if (registerMessage.equals("User registered successfully")) {
            String loginUsername = JOptionPane.showInputDialog("Enter username");
            String loginPassword = JOptionPane.showInputDialog("Enter your password");
            
            loginSuccess = login.loginUser(loginUsername, loginPassword);
            String loginMessage = login.returnLoginStatus(firstName, lastName, loginSuccess);
            
            JOptionPane.showMessageDialog(null, loginMessage);
            
        }
        
        if (loginSuccess) {
            JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");
            int maxMessages = Integer.parseInt(JOptionPane.showInputDialog("How many messages would you like to send?"));

            int sentCount = 0;
            while (true) {
                String menu = JOptionPane.showInputDialog("Select an option:\n1) Send Message\n2) Show recently sent messages\n3) Quit");
                switch (menu) {
                    case "1":
                        if (sentCount >= maxMessages) {
                            JOptionPane.showMessageDialog(null, "Message limit reached.");
                            continue;
                        }

                        String recipient = JOptionPane.showInputDialog("Enter recipient's cell number (+ international format):");
                        String messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");

                        if (messageText.length() > 250) {
                            JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters.");
                            continue;
                        }

                        Message msg = new Message(recipient, messageText);

                        if (!msg.checkMessageID() || !msg.checkRecipientCell()) {
                            JOptionPane.showMessageDialog(null, "Invalid Message ID or Recipient Number.");
                            continue;
                        }

                        String action = Message.sentMessageOption();
                        if (action.equals("Send Message")) {
                            Message.storeMessage(msg);
                            JOptionPane.showMessageDialog(null, "Message sent.\n" + msg.printMessage());
                            sentCount++;
                        } else if (action.equals("Store Message to send later")) {
                            // This can be extended to save to a separate list or JSON later.
                            JOptionPane.showMessageDialog(null, "Message saved to draft (not implemented).");
                        } else {
                            JOptionPane.showMessageDialog(null, "Message discarded.");
                        }
                        break;

                    case "2":
                        JOptionPane.showMessageDialog(null, "Coming Soon.");
                        break;

                    case "3":
                        JOptionPane.showMessageDialog(null, "You have sent " + Message.returnTotalMessages() + " messages.");
                        System.exit(0);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option.");
                }
            }
        }
        
        if (loginSuccess) {
            while (true) {
                String menu = JOptionPane.showInputDialog(
                    "Select an option:\n" +
                    "1) Send Message\n" +
                    "2) Show recently sent messages\n" +
                    "3) Display Report\n" +
                    "4) Search for a message\n" +
                    "5) Delete a message\n" +
                    "6) Quit"
                );

                switch (menu) {
                    case "1": // Send Message (existing logic)
                        break;
                    case "2": // Show recently sent messages
                        JOptionPane.showMessageDialog(null, Message.generateReport());
                        break;
                    case "3": // Display Report
                        JOptionPane.showMessageDialog(null, Message.generateReport());
                        break;
                    case "4": // Search for a message
                        String searchID = JOptionPane.showInputDialog("Enter Message ID to search:");
                        JOptionPane.showMessageDialog(null, Message.searchMessageByID(searchID));
                        break;
                    case "5": // Delete a message
                        String deleteHash = JOptionPane.showInputDialog("Enter Message Hash to delete:");
                        JOptionPane.showMessageDialog(null, Message.deleteMessageByHash(deleteHash));
                        break;
                    case "6": // Quit
                        System.exit(0);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option.");
                }
            }
        }
        
    }
    
    
}
