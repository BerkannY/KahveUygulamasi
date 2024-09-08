package com.itc.coffee.NumberFormat;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumberFormattingTextWatcher implements TextWatcher {

    private EditText editText;

    public PhoneNumberFormattingTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Nothing to do here
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Nothing to do here
    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = s.toString().replaceAll("\\D", ""); // Non-digit characters removed
        String formatted = formatPhoneNumber(input);

        editText.removeTextChangedListener(this);
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }

    private String formatPhoneNumber(String phoneNumber) {
        if ( phoneNumber.length() <= 3) {
            return "O("+ phoneNumber+ ")";
        } else if (phoneNumber.length() <= 6) {
            return "O("+phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3);
        }  else if (phoneNumber.length() <= 10) {
            return "O("+phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3,6)+" "+phoneNumber.substring(6);
        } else {
            return "O("+phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3,6) + " " +
                    phoneNumber.substring(6,10) + " " + phoneNumber.substring(10);
        }
    }
}
