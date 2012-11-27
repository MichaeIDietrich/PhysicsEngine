package de.engineapp.controls;

import javax.swing.SpinnerNumberModel;

public class CyclingSpinnerNumberModel extends SpinnerNumberModel
{
    /**
     * 
     */
    private static final long serialVersionUID = 4424204376909620916L;

    public CyclingSpinnerNumberModel(double start, double min, double max, double step)
    {
        super(start, min, max, step);
    };

    public Object getNextValue() {
        Object value = super.getNextValue();
        if (value == null) {
            value = this.getMinimum();
            this.setValue(value);
        }
        return value;
    }

    public Object getPreviousValue() {
        Object value = super.getPreviousValue();
        if (value == null) {
            value = this.getMaximum();
            this.setValue(value);
        }
        return value;
    }
}
