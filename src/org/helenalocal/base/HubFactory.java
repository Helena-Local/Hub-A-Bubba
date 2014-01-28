package org.helenalocal.base;

import org.helenalocal.base.get.CSAHub;
import org.helenalocal.base.get.UnitHub;
import org.helenalocal.base.post.GrowerHub;
import org.helenalocal.base.test.MockHub;

/**
 * Created by abbie on 1/24/14.
 */



public abstract class HubFactory {
    public static IHub buildHubFetch(int itype) {
        IHub out = new MockHub();  // default to test
        switch (itype) {
            case Hub.SALES:
                out = new UnitHub();
                break;
            case Hub.CSA:
                out = new CSAHub();
                break;
            case Hub.GROWER:
                out = new GrowerHub();
                break;
            case Hub.MOCK:
                out = new MockHub();
                break;
        }
        return out;
    }
}
