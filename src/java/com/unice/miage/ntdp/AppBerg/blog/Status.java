/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unice.miage.ntdp.AppBerg.blog;

/**
 *
 * @author GLYSE581B
 */
public enum Status {
        Published(0),
    ReportAsAbused(1),
    WaitForValidation(2);

    Status(int val) {
        this.val = val;
    }
    int val;
}
