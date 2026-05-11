package atm;

import javax.swing.*;
import java.awt.*;

public class MenuScreen extends ScreenPanel {

    public MenuScreen(ATMFrame parent) {
        super(parent, "MENU");
        // Same vertical glue pattern as the side bars so the option labels
        // line up with the physical side buttons.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(Box.createVerticalGlue());
        add(buildRow("▶ Withdraw", "View History ◀"));
        add(Box.createVerticalGlue());
        add(buildRow("▶ Deposit", "Logout ◀"));
        add(Box.createVerticalGlue());
        add(buildRow("", ""));
        add(Box.createVerticalGlue());
        add(buildRow("", ""));
        add(Box.createVerticalGlue());
    }

    private JPanel buildRow(String leftText, String rightText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel left = new JLabel(leftText, SwingConstants.LEFT);
        left.setForeground(Color.WHITE);
        left.setFont(new Font("Monospaced", Font.BOLD, 20));
        row.add(left, BorderLayout.WEST);

        JLabel right = new JLabel(rightText, SwingConstants.RIGHT);
        right.setForeground(Color.WHITE);
        right.setFont(new Font("Monospaced", Font.BOLD, 20));
        row.add(right, BorderLayout.EAST);

        return row;
    }

    @Override
    public void onShow() {
        // Left buttons: Withdraw (1), Deposit (2)
        parent.setLeftButtonActions(new Runnable[]{
            () -> { parent.pendingTransactionMode = "WITHDRAW"; parent.showScreen("TRANSACTION"); },
            () -> { parent.pendingTransactionMode = "DEPOSIT"; parent.showScreen("TRANSACTION"); },
            null,
            null
        });
        // Right buttons: History (1), Logout (2)
        parent.setRightButtonActions(new Runnable[]{
            () -> parent.showScreen("HISTORY"),
            () -> { Main.logout(); parent.pendingAccountNumber = -1; parent.showScreen("WELCOME"); },
            null,
            null
        });
        // Numpad not used here
        parent.setNumpadHandlers(d -> {}, () -> {}, () -> {}, () -> {});
    }
}
