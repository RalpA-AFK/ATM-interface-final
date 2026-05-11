package atm;

import javax.swing.*;
import java.awt.*;

public class AccountNumberScreen extends ScreenPanel {

    private StringBuilder input = new StringBuilder();
    private JLabel displayLabel;
    private JLabel messageLabel;
    private static final int MAX_DIGITS = 4;

    public AccountNumberScreen(ATMFrame parent) {
        super(parent, "ACCOUNT_NUMBER");
        setLayout(new BorderLayout());

        // Prompt at the top
        JLabel prompt = new JLabel("Enter your account number:", SwingConstants.CENTER);
        prompt.setForeground(Color.WHITE);
        prompt.setFont(new Font("Monospaced", Font.BOLD, 22));
        prompt.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(prompt, BorderLayout.NORTH);

        // Display in the middle (shows typed digits)
        displayLabel = new JLabel("_ _ _ _", SwingConstants.CENTER);
        displayLabel.setForeground(Color.WHITE);
        displayLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        add(displayLabel, BorderLayout.CENTER);

        // Status / error message at the bottom
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        add(messageLabel, BorderLayout.SOUTH);
    }

    @Override
    public void onShow() {
        reset();
        // Wire numpad to this screen's logic
        parent.setNumpadHandlers(
            this::appendDigit,
            this::submit,
            this::clear,
            this::cancel
        );
    }

    private void reset() {
        input.setLength(0);
        updateDisplay();
        messageLabel.setText(" ");
    }

    private void appendDigit(int digit) {
        if (input.length() >= MAX_DIGITS) return;
        input.append(digit);
        updateDisplay();
    }

    private void updateDisplay() {
        StringBuilder shown = new StringBuilder();
        for (int i = 0; i < MAX_DIGITS; i++) {
            if (i < input.length()) {
                shown.append(input.charAt(i));
            } else {
                shown.append('_');
            }
            if (i < MAX_DIGITS - 1) shown.append(' ');
        }
        displayLabel.setText(shown.toString());
    }

    private void submit() {
        if (input.length() < MAX_DIGITS) {
            messageLabel.setText("Please enter all " + MAX_DIGITS + " digits");
            return;
        }
        int accountNum = Integer.parseInt(input.toString());

        // Check if account exists in Main's account list
        for (AccountProfile a : Main.accounts) {
            if (a.getAccountNumber() == accountNum) {
                // Found — remember the pending account number for PIN screen
                parent.pendingAccountNumber = accountNum;
                parent.showScreen("PIN");
                return;
            }
        }

        // Not found
        messageLabel.setText("Account not found");
        reset();
    }

    private void clear() {
        reset();
    }

    private void cancel() {
        parent.pendingAccountNumber = -1;
        parent.showScreen("WELCOME");
    }
}
