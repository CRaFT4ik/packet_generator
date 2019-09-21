package ru.er_log.ui;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.Collections;

import static ru.er_log.components.Utils.log;

public class TextFieldExtended extends TextField
{
    private BooleanProperty letOnlyNumbers = new SimpleBooleanProperty();
    private BooleanProperty letMaxNumValue = new SimpleBooleanProperty();
    private LongProperty maxNumValue = new SimpleLongProperty();
    private BooleanProperty letMinNumValue = new SimpleBooleanProperty();
    private LongProperty minNumValue = new SimpleLongProperty();
    private IntegerProperty maxCharsNumber = new SimpleIntegerProperty();

    // True, if the textfield is filled with an error.
    private BooleanProperty filedWithErrorProperty = new SimpleBooleanProperty();

    public TextFieldExtended() { this(""); }

    public TextFieldExtended(String text)
    {
        super(text);

        ChangeListener<Object> changeListener = new ChangeListener<Object>()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                if (!isLetOnlyNumbers()) setPromptText("");

                StringBuilder stringBuilder = new StringBuilder();
                if (isLetMinNumValue() && isLetMaxNumValue())
                    if (getMinNumValue() != getMaxNumValue())
                        stringBuilder.append(getMinNumValue()).append("-").append(getMaxNumValue());
                    else
                        stringBuilder.append(getMinNumValue());
                else if (isLetMinNumValue())
                    stringBuilder.append("≥ ").append(getMinNumValue());
                else if (isLetMaxNumValue())
                    stringBuilder.append("≤ ").append(getMaxNumValue());

                setPromptText(stringBuilder.toString());
            }
        };

        letOnlyNumbersProperty().addListener(changeListener);
        minNumValueProperty().addListener(changeListener);
        maxNumValueProperty().addListener(changeListener);

        this.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue) // Textfield out focus.
                {
                    if (totalValidate()) return;

                    setErrorStyle(true);
                } else
                {
                    setErrorStyle(false);
                }
            }
        });
    }

    public void setErrorStyle(boolean isError)
    {
        if (isError)
        {
            filedWithErrorProperty.setValue(true);
            getStyleClass().add("error");
        } else
        {
            filedWithErrorProperty.setValue(false);
            getStyleClass().removeAll(Collections.singleton("error"));
        }
    }

    @Override
    public void replaceText(final int start, final int end, final String text)
    {
        if (typingValidate(start, end, text))
            super.replaceText(start, end, text);
    }

    /* Uses for validate while typing process. */
    private boolean typingValidate(final int start, final int end, final String replacementString)
    {
        String curText = this.getText();
        String newText = curText.substring(0, start) + replacementString + curText.substring(end, curText.length());
        //log(curText + " :: " + newText + " :: " + getMinNumValue());

        if (newText.isEmpty())
            return true;

        if (getMaxCharsNumber() != 0 && newText.length() > getMaxCharsNumber())
            return false;

        if (isLetOnlyNumbers())
        {
            if (!newText.matches("[0-9]*"))
                return false;

            try { Long.parseLong(newText); }
            catch (NumberFormatException e) { return false; }

            if (newText.length() > 1 && newText.charAt(0) == '0')
                return false;
        }

        return true;
    }

    /* Uses for validate after finish typing. */
    private boolean totalValidate()
    {
        final String text = this.getText();
        if (text.isEmpty()) return true;

        if (!typingValidate(0, 0, ""))
            return false;

        if (isLetOnlyNumbers())
            try
            {
                long num = Long.parseLong(text);
                if (isLetMinNumValue() && num < getMinNumValue())
                    return false;
                if (isLetMaxNumValue() && num > getMaxNumValue())
                    return false;
            } catch (NumberFormatException e)
            { return false; }

        return true;
    }

    public boolean isLetOnlyNumbers() { return letOnlyNumbers.get(); }
    public BooleanProperty letOnlyNumbersProperty() { return letOnlyNumbers; }
    public void setLetOnlyNumbers(boolean letOnlyNumbers) { this.letOnlyNumbers.set(letOnlyNumbers); }

    public boolean isLetMaxNumValue() { return letMaxNumValue.get(); }
    public BooleanProperty letMaxNumValueProperty() { return letMaxNumValue; }
    public void setLetMaxNumValue(boolean letMaxNumValue) { this.letMaxNumValue.set(letMaxNumValue); }

    public long getMaxNumValue() { return maxNumValue.get(); }
    public LongProperty maxNumValueProperty() { return maxNumValue; }
    public void setMaxNumValue(long maxNumValue) { this.maxNumValue.set(maxNumValue); }

    public boolean isLetMinNumValue() { return letMinNumValue.get(); }
    public BooleanProperty letMinNumValueProperty() { return letMinNumValue; }
    public void setLetMinNumValue(boolean letMinNumValue) { this.letMinNumValue.set(letMinNumValue); }

    public long getMinNumValue() { return minNumValue.get(); }
    public LongProperty minNumValueProperty() { return minNumValue; }
    public void setMinNumValue(long minNumValue) { this.minNumValue.set(minNumValue); }

    public int getMaxCharsNumber() { return maxCharsNumber.get(); }
    public IntegerProperty maxCharsNumberProperty() { return maxCharsNumber; }
    public void setMaxCharsNumber(int maxCharsNumber) { this.maxCharsNumber.set(maxCharsNumber); }

    public boolean isFiledWithErrorProperty() { return filedWithErrorProperty.get(); }
    public BooleanProperty filedWithErrorPropertyProperty() { return filedWithErrorProperty; }
}
