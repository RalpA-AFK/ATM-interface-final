package atm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;


public class ATMFrame extends JFrame {

    // Card switcher for the blue screen area
    private CardLayout screenLayout;
    private JPanel screenHost;
    private Map<String, ScreenPanel> screens = new HashMap<>();

    // Numpad button references for handler wiring
    private JButton[] digitButtons = new JButton[10];
    private JButton enterBtn, clearBtn, cancelBtn;

    // Side button references for handler wiring
    private JButton[] leftButtons = new JButton[4];
    private JButton[] rightButtons = new JButton[4];

    // Session-scoped state shared between screens
    public int pendingAccountNumber = -1;
    public String pendingTransactionMode = null;
    public String lastTransactionType = null;
    public int lastTransactionAmount = 0;
    public int lastTransactionBalance = 0;

    public ATMFrame() {
        setTitle("ATM Interface");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());

        add(buildLeftButtons(), BorderLayout.WEST);
        add(buildRightButtons(), BorderLayout.EAST);
        add(buildBlueScreen(), BorderLayout.CENTER);
        add(buildNumpad(), BorderLayout.SOUTH);

        // Each panel registers itself with this frame
        new WelcomePanel(this);
        new AccountNumberScreen(this);
        new PINScreen(this);
        new MenuScreen(this);
        new TransactionScreen(this);
        new HistoryScreen(this);
        new ReceiptScreen(this);

        showScreen("WELCOME");
    }

    /** Called by ScreenPanel constructor to register itself under a name. */
    public void registerScreen(String name, ScreenPanel panel) {
        screens.put(name, panel);
        screenHost.add(panel, name);
    }

    /** Switches the inner blue-area panel to the named screen and calls onShow(). */
    public void showScreen(String name) {
        screenLayout.show(screenHost, name);
        ScreenPanel p = screens.get(name);
        if (p != null) p.onShow();
    }

    /** Clears existing numpad listeners and attaches new ones. */
    public void setNumpadHandlers(IntConsumer onDigit, Runnable onEnter, Runnable onClear, Runnable onCancel) {
        for (JButton b : digitButtons) {
            if (b != null) {
                for (ActionListener al : b.getActionListeners()) b.removeActionListener(al);
            }
        }
        for (ActionListener al : enterBtn.getActionListeners()) enterBtn.removeActionListener(al);
        for (ActionListener al : clearBtn.getActionListeners()) clearBtn.removeActionListener(al);
        for (ActionListener al : cancelBtn.getActionListeners()) cancelBtn.removeActionListener(al);

        for (int i = 0; i < 10; i++) {
            final int digit = i;
            if (digitButtons[i] != null) {
                digitButtons[i].addActionListener(e -> onDigit.accept(digit));
            }
        }
        enterBtn.addActionListener(e -> onEnter.run());
        clearBtn.addActionListener(e -> onClear.run());
        cancelBtn.addActionListener(e -> onCancel.run());
    }

    /** Wires up to 4 actions to the left side buttons. Null entries disable that button. */
    public void setLeftButtonActions(Runnable[] actions) {
        wireSideButtons(leftButtons, actions);
    }

    /** Wires up to 4 actions to the right side buttons. Null entries disable that button. */
    public void setRightButtonActions(Runnable[] actions) {
        wireSideButtons(rightButtons, actions);
    }

    private void wireSideButtons(JButton[] buttons, Runnable[] actions) {
        for (int i = 0; i < buttons.length; i++) {
            JButton b = buttons[i];
            for (ActionListener al : b.getActionListeners()) b.removeActionListener(al);
            if (i < actions.length && actions[i] != null) {
                Runnable a = actions[i];
                b.addActionListener(e -> a.run());
                b.setEnabled(true);
            } else {
                b.setEnabled(false);
            }
        }
    }

    private JPanel buildLeftButtons(){
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        bar.setBackground(Color.GRAY);
        bar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for(int i = 0; i < 4; i++){
            bar.add(Box.createVerticalGlue());
            JButton btn = new JButton("▶");
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftButtons[i] = btn;
            bar.add(btn);
        }
        bar.add(Box.createVerticalGlue());
        return bar;
    }

    private JPanel buildRightButtons() {
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        bar.setBackground(Color.GRAY);
        bar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < 4; i++) {
            bar.add(Box.createVerticalGlue());
            JButton btn = new JButton("◀");
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            rightButtons[i] = btn;
            bar.add(btn);
        }
        bar.add(Box.createVerticalGlue());
        return bar;
    }

    private JPanel buildBlueScreen() {
        // Blue area is a CardLayout container holding all screens
        screenLayout = new CardLayout();
        screenHost = new JPanel(screenLayout);
        screenHost.setBackground(new Color(30, 80, 160));
        screenHost.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        return screenHost;
    }
// makes the numpad
    private JPanel buildNumpad() {
        JPanel padWrapper = new JPanel();
        padWrapper.setBackground(Color.GRAY);
        padWrapper.setBorder(BorderFactory.createEmptyBorder(10, 80, 20, 80));

        JPanel pad = new JPanel(new GridLayout(4, 4, 5, 5));
        pad.setBackground(Color.GRAY);

        String[] keys = {
            "1", "2", "3", "Enter",
            "4", "5", "6", "Clear",
            "7", "8", "9", "Cancel",
            "",  "0", "",  ""
        };
//use a loop to make everything rather then rewriting all those lines indi
        for (String key : keys) {
            if (key.isEmpty()) {
                JLabel spacer = new JLabel();
                spacer.setOpaque(true);
                spacer.setBackground(Color.GRAY);
                pad.add(spacer);
            } else {
                JButton btn = new JButton(key);
                if (key.equals("Enter")) {
                    btn.setBackground(new Color(60, 180, 60));
                    btn.setForeground(Color.WHITE);
                    btn.setOpaque(true);
                    btn.setBorderPainted(false);
                    enterBtn = btn;
                } else if (key.equals("Clear")) {
                    btn.setBackground(new Color(230, 200, 40));
                    btn.setForeground(Color.BLACK);
                    btn.setOpaque(true);
                    btn.setBorderPainted(false);
                    clearBtn = btn;
                } else if (key.equals("Cancel")) {
                    btn.setBackground(new Color(200, 50, 50));
                    btn.setForeground(Color.WHITE);
                    btn.setOpaque(true);
                    btn.setBorderPainted(false);
                    cancelBtn = btn;
                } else {
                    // it's a digit 0-9
                    int digit = Integer.parseInt(key);
                    digitButtons[digit] = btn;
                }
                pad.add(btn);
            }
        }

        padWrapper.add(pad);
        return padWrapper;
    }
}
