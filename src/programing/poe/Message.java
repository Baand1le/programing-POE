package programing.poe;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;

public class Message {
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int messageCounter = 0;
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();

    // Constructor
    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        messageCounter++;
        messageIDs.add(this.messageID);
        messageHashes.add(this.messageHash);
    }

    // Generate a random 10-digit message ID
    private String generateMessageID() {
        Random rand = new Random();
        long number = 1000000000L + (long)(rand.nextDouble() * 8999999999L);
        return String.valueOf(number);
    }

    // 1. Validate Message ID
    public boolean checkMessageID() {
        return messageID.length() == 10;
    }

    // 2. Validate recipient phone number
    public boolean checkRecipientCell() {
        return recipient.matches("^\\+\\d{9,10}$");
    }

    // 3. Create a message hash in format: First2Digits:MessageNum:FIRSTLAST
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        String prefix = messageID.substring(0, 2);
        return (prefix + ":" + messageCounter + ":" + firstWord + lastWord).toUpperCase();
    }
    
    // Store message in JSON file (using ChatGPT-generated code)
    public static void storeMessageToJSON(Message msg) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray;
            
            // Read existing JSON file (if any)
            try (FileReader reader = new FileReader("messages.json")) {
                jsonArray = (JSONArray) parser.parse(reader);
            } catch (Exception e) {
                jsonArray = new JSONArray(); // Create new array if file doesn't exist
            }

            // Add new message
            JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("messageID", msg.messageID);
            jsonMsg.put("recipient", msg.recipient);
            jsonMsg.put("messageText", msg.messageText);
            jsonMsg.put("messageHash", msg.messageHash);
            jsonArray.add(jsonMsg);

            // Write back to file
            try (FileWriter file = new FileWriter("messages.json")) {
                file.write(jsonArray.toJSONString());
                file.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read messages from JSON file into storedMessages array
    public static void loadStoredMessagesFromJSON() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("messages.json"));

            for (Object obj : jsonArray) {
                JSONObject jsonMsg = (JSONObject) obj;
                Message msg = new Message(
                    (String) jsonMsg.get("recipient"),
                    (String) jsonMsg.get("messageText")
                );
                storedMessages.add(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Display the longest message in the sentMessages array
    public static String getLongestMessage() {
        String longest = "";
        for (Message msg : sentMessages) {
            if (msg.messageText.length() > longest.length()) {
                longest = msg.messageText;
            }
        }
        return longest;
    }

    // Search for a message by ID and return its details
    public static String searchMessageByID(String id) {
        for (Message msg : sentMessages) {
            if (msg.messageID.equals(id)) {
                return "Recipient: " + msg.recipient + "\nMessage: " + msg.messageText;
            }
        }
        return "Message not found.";
    }

    // Search for all messages sent to a recipient
    public static String searchMessagesByRecipient(String recipient) {
        StringBuilder result = new StringBuilder();
        for (Message msg : sentMessages) {
            if (msg.recipient.equals(recipient)) {
                result.append(msg.messageText).append("\n");
            }
        }
        return result.toString().isEmpty() ? "No messages found." : result.toString();
    }

    // Delete a message by its hash
    public static String deleteMessageByHash(String hash) {
        for (Message msg : sentMessages) {
            if (msg.messageHash.equals(hash)) {
                sentMessages.remove(msg);
                return "Message deleted successfully.";
            }
        }
        return "Message not found.";
    }

    // Generate a report of all sent messages
    public static String generateReport() {
        StringBuilder report = new StringBuilder();
        for (Message msg : sentMessages) {
            report.append("Message ID: ").append(msg.messageID).append("\n")
                  .append("Message Hash: ").append(msg.messageHash).append("\n")
                  .append("Recipient: ").append(msg.recipient).append("\n")
                  .append("Message: ").append(msg.messageText).append("\n\n");
        }
        return report.toString().isEmpty() ? "No messages sent." : report.toString();
    }

    // Getters for arrays (for unit testing)
    public static ArrayList<Message> getSentMessages() { return sentMessages; }
    public static ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<Message> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getMessageHashes() { return messageHashes; }
    public static ArrayList<String> getMessageIDs() { return messageIDs; }


    // 4. Let user decide to send, store, or disregard message
    public static String sentMessageOption() {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        return (String) JOptionPane.showInputDialog(null, "Choose an option:",
                "Message Action", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    // 5. Display message details
    public String printMessage() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText;
    }

    // 6. Return total messages sent
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Add message to the sent list
    public static void storeMessage(Message m) {
        sentMessages.add(m);
    }

    // List all messages sent
    public static String printMessages() {
        if (sentMessages.isEmpty()) return "No messages sent.";
        StringBuilder result = new StringBuilder();
        for (Message msg : sentMessages) {
            result.append(msg.printMessage()).append("\n\n");
        }
        return result.toString();
    }
}
