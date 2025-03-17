import java.util.Scanner;
import java.io.*;

class TextEditor {
    private StringBuilder content; // The text being edited
    private String clipboard;      // For cut/copy/paste functionality
    private StringBuilder undoStack; // For undo functionality
    private StringBuilder redoStack; // For redo functionality

    // Constructor to initialize the editor
    public TextEditor() {
        content = new StringBuilder();
        clipboard = "";
        undoStack = new StringBuilder();
        redoStack = new StringBuilder();
    }

    // 1. Display current content
    public void displayContent() {
        System.out.println("\n--- Current Content ---");
        if (content.length() == 0) {
            System.out.println("[Empty Document]");
        } else {
            System.out.println(content);
        }
    }

    // 2. Add text to the editor
    public void addText(String text) {
        saveStateForUndo(); // Save current state for undo
        content.append(text);
        System.out.println("Text added successfully.");
    }

    // 3. Cut text by range
    public void cutText(int start, int end) {
        try {
            if (isValidRange(start, end)) {
                saveStateForUndo(); // Save current state for undo
                clipboard = content.substring(start, end);
                content.delete(start, end);
                System.out.println("Text cut successfully.");
            } else {
                System.out.println("Error: Invalid range! Please enter valid indices.");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to cut text. Please try again.");
        }
    }

    // 4. Copy text by range
    public void copyText(int start, int end) {
        try {
            if (isValidRange(start, end)) {
                clipboard = content.substring(start, end);
                System.out.println("Text copied to clipboard.");
            } else {
                System.out.println("Error: Invalid range! Please enter valid indices.");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to copy text. Please try again.");
        }
    }

    // 5. Paste text at a position
    public void pasteText(int position) {
        try {
            if (position >= 0 && position <= content.length()) {
                saveStateForUndo(); // Save current state for undo
                content.insert(position, clipboard);
                System.out.println("Text pasted successfully.");
            } else {
                System.out.println("Error: Invalid position! Please enter a valid position.");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to paste text. Please try again.");
        }
    }

    // 6. Undo last operation
    public void undo() {
        try {
            if (undoStack.length() > 0) {
                saveStateForRedo(); // Save current state for redo
                content = new StringBuilder(undoStack.toString());
                undoStack.setLength(0); // Clear undoStack
                System.out.println("Undo successful.");
            } else {
                System.out.println("Error: Nothing to undo!");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to perform undo. Please try again.");
        }
    }

    // 7. Redo last undone operation
    public void redo() {
        try {
            if (redoStack.length() > 0) {
                saveStateForUndo(); // Save current state for undo
                content = new StringBuilder(redoStack.toString());
                redoStack.setLength(0); // Clear redoStack
                System.out.println("Redo successful.");
            } else {
                System.out.println("Error: Nothing to redo!");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to perform redo. Please try again.");
        }
    }

    // 8. Find and replace text
    public void findAndReplace(String find, String replace) {
        try {
            if (content.toString().contains(find)) {
                saveStateForUndo(); // Save current state for undo
                String replacedContent = content.toString().replace(find, replace);
                content = new StringBuilder(replacedContent);
                System.out.println("Text replaced successfully.");
            } else {
                System.out.println("Error: Text to find not found in the document.");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to find and replace text. Please try again.");
        }
    }

    // 9. Save text to a file
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content.toString());
            System.out.println("Content saved to file " + filename + " successfully.");
        } catch (IOException e) {
            System.out.println("Error: Unable to save content to file.");
        }
    }

    // Utility to check valid range
    private boolean isValidRange(int start, int end) {
        return start >= 0 && end <= content.length() && start < end;
    }

    // Save state for undo
    private void saveStateForUndo() {
        undoStack = new StringBuilder(content.toString());
    }

    // Save state for redo
    private void saveStateForRedo() {
        redoStack = new StringBuilder(content.toString());
    }
}

public class ConsoleTextEditor {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor(); // Create editor instance
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Text Editor Menu ---");
            System.out.println("1. Display Content");
            System.out.println("2. Add Text");
            System.out.println("3. Cut Text");
            System.out.println("4. Copy Text");
            System.out.println("5. Paste Text");
            System.out.println("6. Undo");
            System.out.println("7. Redo");
            System.out.println("8. Find and Replace");
            System.out.println("9. Save to File");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    editor.displayContent();
                    break;
                case 2:
                    System.out.print("Enter text to add: ");
                    String text = scanner.nextLine();
                    editor.addText(text);
                    break;
                case 3:
                    try {
                        System.out.print("Enter start and end index to cut (space-separated): ");
                        int cutStart = scanner.nextInt();
                        int cutEnd = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        editor.cutText(cutStart, cutEnd);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid indices! Please enter valid numbers.");
                        scanner.nextLine(); // Clear buffer
                    }
                    break;
                case 4:
                    try {
                        System.out.print("Enter start and end index to copy (space-separated): ");
                        int copyStart = scanner.nextInt();
                        int copyEnd = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        editor.copyText(copyStart, copyEnd);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid indices! Please enter valid numbers.");
                        scanner.nextLine(); // Clear buffer
                    }
                    break;
                case 5:
                    try {
                        System.out.print("Enter position to paste: ");
                        int pastePosition = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        editor.pasteText(pastePosition);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid position! Please enter a valid number.");
                        scanner.nextLine(); // Clear buffer
                    }
                    break;
                case 6:
                    editor.undo();
                    break;
                case 7:
                    editor.redo();
                    break;
                case 8:
                    System.out.print("Enter text to find: ");
                    String find = scanner.nextLine();
                    System.out.print("Enter text to replace: ");
                    String replace = scanner.nextLine();
                    editor.findAndReplace(find, replace);
                    break;
                case 9:
                    System.out.print("Enter filename to save: ");
                    String filename = scanner.nextLine();
                    editor.saveToFile(filename);
                    break;
                case 10:
                    running = false;
                    System.out.println("Exiting Text Editor. Goodbye!");
                    break;
                default:
                    System.out.println("Error: Invalid choice! Please enter a number between 1 and 10.");
            }
        }
        scanner.close();
    }
}
