/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unice.miage.ntdp.AppBerg.blog;

/**
 *
 * @author GLYSE581B
 */
public enum UserStatus {
    Enabled(0),
    Disabled(1);

    UserStatus(int val) {
        this.val = val;
    }
    int val;    
}
