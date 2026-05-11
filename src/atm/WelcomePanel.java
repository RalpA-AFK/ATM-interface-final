package atm;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends ScreenPanel {

    public WelcomePanel(ATMFrame parent) {
        super(parent, "WELCOME");
        setLayout(new BorderLayout());

        // Title text
        JLabel title = new JLabel("WELCOME", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Monospaced", Font.BOLD, 36));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Subtitle
        JLabel subtitle = new JLabel("Please scan your card to begin", SwingConstants.CENTER);
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Monospaced", Font.PLAIN, 18));
        add(subtitle, BorderLayout.CENTER);

        // Scan Card button
        JPanel buttonWrap = new JPanel();
        buttonWrap.setOpaque(false);
        buttonWrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        JButton scanCard = new JButton("Scan Card");
        scanCard.setFont(new Font("Monospaced", Font.BOLD, 18));
        scanCard.setBackground(Color.WHITE);
        scanCard.setForeground(Color.BLACK);
        scanCard.setOpaque(true);
        scanCard.setBorderPainted(false);
        scanCard.setFocusPainted(false);
        scanCard.addActionListener(e -> parent.showScreen("ACCOUNT_NUMBER"));
        buttonWrap.add(scanCard);

        add(buttonWrap, BorderLayout.SOUTH);
    }
}
