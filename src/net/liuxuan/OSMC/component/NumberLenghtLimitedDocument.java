
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package net.liuxuan.OSMC.component;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 用法:
 *
 * JTextField  text=new JTextField();
 *
 * text.setDocument(new NumberLenghtLimitedDmt(7));
 *
 * 那么这个文本框只能输入7位而且是只能是数字!!!
 *
 * @author Administrator
 */
public class NumberLenghtLimitedDocument extends PlainDocument {
    private int limit;

    /**
     * Constructs ...
     *
     *
     * @param limit
     */
    public NumberLenghtLimitedDocument(int limit) {
        super();
        this.limit = limit;
    }

    /**
     * Method description
     *
     *
     * @param offset
     * @param str
     * @param attr
     *
     * @throws BadLocationException
     */
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            char[] upper  = str.toCharArray();
            int    length = 0;

            for (int i = 0; i < upper.length; i++) {
                if ((upper[i] >= '0') && (upper[i] <= '9')) {
                    upper[length++] = upper[i];
                }
            }

            super.insertString(offset, new String(upper, 0, length), attr);
        }
    }
}
