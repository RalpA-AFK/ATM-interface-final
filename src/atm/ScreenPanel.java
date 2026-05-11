package atm;

import javax.swing.*;

/**
 * Base class for all screen panels that live inside ATMFrame's blue area.
 * Each screen registers itself with the parent ATMFrame using a name,
 * and may override onShow() to set up state (like numpad handlers) when activated.
 */
public abstract class ScreenPanel extends JPanel {

    protected ATMFrame parent;

    public ScreenPanel(ATMFrame parent, String name) {
        this.parent = parent;
        setOpaque(false); // let blue background show through
        parent.registerScreen(name, this);
    }

    /** Called by ATMFrame when this screen becomes visible. Override as needed. */
    public void onShow() {
        // default: do nothing
    }
}
