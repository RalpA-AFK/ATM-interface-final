package atm;

import javax.swing.*;
import java.awt.*;

public class PINScreen extends ScreenPanel {

    private StringBuilder input = new StringBuilder();
    private JLabel displayLabel;
    private JLabel messageLabel;
    private static final int MAX_DIGITS = 4;

    public PINScreen(ATMFrame parent) {
        super(parent, "PIN");
        setLayout(new BorderLayout());

        JLabel prompt = new JLabel("Enter your PIN:", SwingConstants.CENTER);
        prompt.setForeground(Color.WHITE);
        prompt.setFont(new Font("Monospaced", Font.BOLD, 22));
        prompt.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(prompt, BorderLayout.NORTH);

        displayLabel = new JLabel("_ _ _ _", SwingConstants.CENTER);
        displayLabel.setForeground(Color.WHITE);
        displayLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        add(displayLabel, BorderLayout.CENTER);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        add(messageLabel, BorderLayout.SOUTH);
    }

    @Override
    public void onShow() {
        reset();
        parent.setNumpadHandlers(
            this::appendDigit,
            this::submit,
            this::clear,
            this::cancel
        );
        // Disable side buttons on this screen
        parent.setLeftButtonActions(new Runnable[]{null, null, null, null});
        parent.setRightButtonActions(new Runnable[]{null, null, null, null});
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
                shown.append('*');
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
        String pin = input.toString();
        if (Main.login(parent.pendingAccountNumber, pin)) {
            parent.showScreen("MENU");
        } else {
            messageLabel.setText("Incorrect PIN");
            reset();
        }
    }

    private void clear() {
        reset();
    }

    private void cancel() {
        parent.pendingAccountNumber = -1;
        Main.logout();
        parent.showScreen("WELCOME");
    }
}
