package tests.aStarTest;

import javax.swing.*;
import java.awt.*;

public class GridNode extends JButton {

    private Color state;

    public GridNode() {
        super();
        setEnabled(false);
    }

    public void setState(Color state) {
        this.state = state;
        setBackground(this.state);
    }
}
