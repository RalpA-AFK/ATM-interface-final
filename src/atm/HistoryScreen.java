package atm;

import javax.swing.*;
import java.awt.*;

public class HistoryScreen extends ScreenPanel {

    private JTextArea historyArea;

    public HistoryScreen(ATMFrame parent) {
        super(parent, "HISTORY");
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Transaction History", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Monospaced", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(20, 60, 130));
        historyArea.setForeground(Color.WHITE);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        add(scroll, BorderLayout.CENTER);

        JLabel footer = new JLabel("Press any side button to return", SwingConstants.CENTER);
        footer.setForeground(Color.WHITE);
        footer.setFont(new Font("Monospaced", Font.PLAIN, 14));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        add(footer, BorderLayout.SOUTH);
    }

    @Override
    public void onShow() {
        historyArea.setText(buildHistoryText());
        historyArea.setCaretPosition(0);

        Runnable back = () -> parent.showScreen("MENU");
        parent.setLeftButtonActions(new Runnable[]{back, back, back, back});
        parent.setRightButtonActions(new Runnable[]{back, back, back, back});
        parent.setNumpadHandlers(d -> {}, () -> {}, () -> {}, back);
    }

    private String buildHistoryText() {
        AccountProfile a = Main.activeAccount;
        if (a == null) return "No active account.";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %12s %12s%n", "Type", "Amount", "Balance"));
        sb.append("-------------------------------------------\n");
        for (LogEntry e : a.getLogs().getEntries()) {
            sb.append(String.format("%-15s %12s %12s%n",
                e.getTransactionType(),
                String.format("$%,d", e.getAmount()),
                String.format("$%,d", e.getBalance())));
        }
        return sb.toString();
    }
}
