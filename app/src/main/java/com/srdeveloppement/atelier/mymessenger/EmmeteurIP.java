package com.srdeveloppement.atelier.mymessenger;

import java.net.InetAddress;

/**
 * Created by lhadj on 12/12/2016.
 */

public class EmmeteurIP {
    private InetAddress adr ;

    public EmmeteurIP(InetAddress adr) {
        this.adr = adr;
    }

    public InetAddress getAdr() {
        return adr;
    }

    public void setAdr(InetAddress adr) {
        this.adr = adr;
    }
}
