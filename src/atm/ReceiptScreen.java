package atm;

import javax.swing.*;
import java.awt.*;

public class ReceiptScreen extends ScreenPanel {

    private JLabel typeLabel;
    private JLabel amountLabel;
    private JLabel balanceLabel;

    public ReceiptScreen(ATMFrame parent) {
        super(parent, "RECEIPT");
        // Use same vertical glue pattern as side bars so the Yes/No row
        // lines up with the physical button next to it.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header — makes it obvious this is a receipt
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel header = new JLabel("====== RECEIPT ======", SwingConstants.CENTER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Monospaced", Font.BOLD, 22));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeader = new JLabel("Transaction Complete", SwingConstants.CENTER);
        subHeader.setForeground(Color.WHITE);
        subHeader.setFont(new Font("Monospaced", Font.PLAIN, 14));
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(header);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subHeader);
        add(headerPanel);

        add(Box.createVerticalGlue());

        // Slot 1: Yes/No question (aligned with side buttons row 1)
        JPanel questionRow = buildRow("▶ Yes (Menu)", "No (Logout) ◀");
        add(questionRow);

        add(Box.createVerticalGlue());

        // Slot 2: Transaction details
        JPanel detailRow = new JPanel();
        detailRow.setLayout(new BoxLayout(detailRow, BoxLayout.Y_AXIS));
        detailRow.setOpaque(false);
        detailRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        typeLabel = makeDetailLabel("Transaction: ");
        amountLabel = makeDetailLabel("Amount: ");
        balanceLabel = makeDetailLabel("New Balance: ");
        detailRow.add(typeLabel);
        detailRow.add(Box.createVerticalStrut(8));
        detailRow.add(amountLabel);
        detailRow.add(Box.createVerticalStrut(8));
        detailRow.add(balanceLabel);
        add(detailRow);

        add(Box.createVerticalGlue());
        add(buildRow("", ""));
        add(Box.createVerticalGlue());
        add(buildRow("", ""));
        add(Box.createVerticalGlue());
    }

    private JLabel makeDetailLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Monospaced", Font.PLAIN, 18));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
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

    @Override
    public void onShow() {
        typeLabel.setText("Transaction: " + parent.lastTransactionType);
        amountLabel.setText("Amount: " + formatMoney(parent.lastTransactionAmount));
        balanceLabel.setText("New Balance: " + formatMoney(parent.lastTransactionBalance));

        parent.setLeftButtonActions(new Runnable[]{
            () -> parent.showScreen("MENU"),
            null, null, null
        });
        parent.setRightButtonActions(new Runnable[]{
            () -> {
                Main.logout();
                parent.pendingAccountNumber = -1;
                parent.showScreen("WELCOME");
            },
            null, null, null
        });
        parent.setNumpadHandlers(d -> {}, () -> {}, () -> {}, () -> parent.showScreen("MENU"));
    }

    private static String formatMoney(int amount) {
        return String.format("$%,d", amount);
    }
}
