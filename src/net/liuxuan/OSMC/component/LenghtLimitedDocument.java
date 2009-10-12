/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liuxuan.OSMC.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Administrator
 */
public class LenghtLimitedDocument extends PlainDocument {

    private int limit;

    public LenghtLimitedDocument(int limit) {
        super();
        this.limit = limit;
    }

    

    public void insertString(int offset, String str, AttributeSet attr)
            throws BadLocationException {
        if (str == null) {
            return;
        }
        if ((getLength() + str.length()) <= limit) {
//            Character a = 'a';
//
//            char[] upper = str.toCharArray();
//            int length = 0;
//            for (int i = 0; i < upper.length; i++) {
//
//                if (upper[i] >= '0' && upper[i] <= '9') {
//                    upper[length++] = upper[i];
//                }
//            }
//            super.insertString(offset, new String(upper, 0, length), attr);
            super.insertString(offset, str, attr);
        }
    }
}
