package atm;

import javax.swing.*;
import java.awt.*;

public class TransactionScreen extends ScreenPanel {

    private JLabel titleLabel;
    private JLabel contentLabel;
    private JLabel messageLabel;
    private JPanel optionsArea;

    private String mode;          // "WITHDRAW" or "DEPOSIT"
    private StringBuilder amountInput = new StringBuilder();

    public TransactionScreen(ATMFrame parent) {
        super(parent, "TRANSACTION");
        setLayout(new BorderLayout());

        titleLabel = new JLabel(" ", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        contentLabel = new JLabel(" ", SwingConstants.CENTER);
        contentLabel.setForeground(Color.WHITE);
        contentLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));

        optionsArea = new JPanel();
        optionsArea.setLayout(new BoxLayout(optionsArea, BoxLayout.Y_AXIS));
        optionsArea.setOpaque(false);
        optionsArea.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(contentLabel, BorderLayout.NORTH);
        centerWrap.add(optionsArea, BorderLayout.CENTER);
        add(centerWrap, BorderLayout.CENTER);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        add(messageLabel, BorderLayout.SOUTH);
    }

    @Override
    public void onShow() {
        this.mode = parent.pendingTransactionMode;
        amountInput.setLength(0);
        messageLabel.setText(" ");

        if ("WITHDRAW".equals(mode)) {
            showWithdrawAmountSelection();
        } else if ("DEPOSIT".equals(mode)) {
            showDepositEntry();
        }
    }

    // ---------- WITHDRAW: select amount ----------

    private void showWithdrawAmountSelection() {
        titleLabel.setText("Withdraw");
        contentLabel.setText("Select an amount:");
        optionsArea.removeAll();

        optionsArea.add(Box.createVerticalGlue());
        optionsArea.add(buildRow("▶ $20", "$40 ◀"));
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.add(buildRow("▶ $60", "$80 ◀"));
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.add(buildRow("▶ $100", "Cancel ◀"));
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.add(buildRow("", ""));
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.revalidate();
        optionsArea.repaint();

        parent.setLeftButtonActions(new Runnable[]{
            () -> showFeeWarning(20),
            () -> showFeeWarning(60),
            () -> showFeeWarning(100),
            null
        });
        parent.setRightButtonActions(new Runnable[]{
            () -> showFeeWarning(40),
            () -> showFeeWarning(80),
            () -> parent.showScreen("MENU"),
            null
        });
        parent.setNumpadHandlers(d -> {}, () -> {}, () -> {}, () -> parent.showScreen("MENU"));
    }

    // ---------- WITHDRAW: fee warning ----------

    private void showFeeWarning(int amount) {
        titleLabel.setText("Withdraw");
        contentLabel.setText("<html><center>This vendor charges $2 to process<br>withdrawals.<br><br>Total to be withdrawn: " + formatMoney(amount + 2) + "<br><br>Would you like to proceed?</center></html>");
        optionsArea.removeAll();
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.add(buildRow("▶ Yes", "No ◀"));
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.add(buildRow("", ""));
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.add(buildRow("", ""));
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.add(buildRow("", ""));
        optionsArea.add(Box.createVerticalGlue());
        optionsArea.revalidate();
        optionsArea.repaint();
        messageLabel.setText(" ");

        parent.setLeftButtonActions(new Runnable[]{
            () -> processWithdraw(amount),
            null, null, null
        });
        parent.setRightButtonActions(new Runnable[]{
            () -> showWithdrawAmountSelection(),
            null, null, null
        });
        parent.setNumpadHandlers(d -> {}, () -> {}, () -> {}, () -> showWithdrawAmountSelection());
    }

    private void processWithdraw(int amount) {
        int total = amount + 2;  // $2 vendor fee
        AccountProfile a = Main.activeAccount;
        if (a == null) {
            parent.showScreen("WELCOME");
            return;
        }
        if (total > a.getBalance()) {
            messageLabel.setText("Insufficient funds");
            return;
        }
        a.withdraw(total);
        parent.lastTransactionType = "Withdrawal";
        parent.lastTransactionAmount = amount;
        parent.lastTransactionBalance = a.getBalance();
        parent.showScreen("RECEIPT");
    }

    // ---------- DEPOSIT: amount entry ----------

    private void showDepositEntry() {
        amountInput.setLength(0);
        titleLabel.setText("Deposit");
        contentLabel.setText("Enter amount to deposit:");
        optionsArea.removeAll();

        JLabel amount = new JLabel("$0", SwingConstants.CENTER);
        amount.setForeground(Color.WHITE);
        amount.setFont(new Font("Monospaced", Font.BOLD, 36));
        amount.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsArea.add(Box.createVerticalStrut(20));
        optionsArea.add(amount);
        optionsArea.revalidate();
        optionsArea.repaint();

        parent.setLeftButtonActions(new Runnable[]{null, null, null, null});
        parent.setRightButtonActions(new Runnable[]{null, null, null, () -> parent.showScreen("MENU")});

        parent.setNumpadHandlers(
            d -> {
                amountInput.append(d);
                amount.setText(formatMoney(Integer.parseInt(amountInput.toString())));
            },
            () -> {
                if (amountInput.length() == 0) {
                    messageLabel.setText("Enter an amount");
                    return;
                }
                int val = Integer.parseInt(amountInput.toString());
                if (val <= 0) {
                    messageLabel.setText("Amount must be positive");
                    return;
                }
                processDeposit(val);
            },
            () -> {
                amountInput.setLength(0);
                amount.setText("$0");
                messageLabel.setText(" ");
            },
            () -> parent.showScreen("MENU")
        );
    }

    private void processDeposit(int amount) {
        AccountProfile a = Main.activeAccount;
        if (a == null) {
            parent.showScreen("WELCOME");
            return;
        }
        a.deposit(amount);
        parent.lastTransactionType = "Deposit";
        parent.lastTransactionAmount = amount;
        parent.lastTransactionBalance = a.getBalance();
        parent.showScreen("RECEIPT");
    }

    private static String formatMoney(int amount) {
        return String.format("$%,d", amount);
    }

    private JPanel buildRow(String leftText, String rightText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel left = new JLabel(leftText, SwingConstants.LEFT);
        left.setForeground(Color.WHITE);
        left.setFont(new Font("Monospaced", Font.BOLD, 18));
        row.add(left, BorderLayout.WEST);

        JLabel right = new JLabel(rightText, SwingConstants.RIGHT);
        right.setForeground(Color.WHITE);
        right.setFont(new Font("Monospaced", Font.BOLD, 18));
        row.add(right, BorderLayout.EAST);

        return row;
    }
}
