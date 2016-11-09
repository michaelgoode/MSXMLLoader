/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import Utilities.References;

/**
 *
 * @author michaelgoode
 */
public class InternationalUPC extends UPC {
    
    public InternationalUPC(Size size, String upcSecondarySize) {
        super(size);
        this.setUpcNumber(References.DefaultUPC);
        this.setSizeCode(References.DefaultUPCSIZE);
        this.setSecondarySize(upcSecondarySize);
        this.setSellingPrice("");
        this.setBoxQuantity("0");
    }
    
}
