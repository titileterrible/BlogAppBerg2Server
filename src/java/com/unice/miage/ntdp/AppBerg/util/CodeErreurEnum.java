
package com.unice.miage.ntdp.AppBerg.util;

/**
 *
 * @author GLYSE581B
 */
public enum CodeErreurEnum {
    
    Succes(0),
    Failure(1),
    Unregistered(3),
    BooleanTrue(4),
    BooleanFalse(5);     
   
    CodeErreurEnum(int val) {
        this.val = val;
    }
    int val;
}
