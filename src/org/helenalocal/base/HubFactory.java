package org.helenalocal.base;

import org.helenalocal.base.get.CSAHub;
import org.helenalocal.base.get.UnitHub;
import org.helenalocal.base.post.GrowerHub;
import org.helenalocal.base.test.MockHub;

/**
 * Created by abbie on 1/24/14.
 */



public abstract class HubFactory {
    public static IHub buildHubFetch(Hub.Type itype) {
        IHub out = new MockHub();  // default to test
        switch (itype) {
            case SALES:
                out = new UnitHub();
                break;
            case CSA:
                out = new CSAHub();
                break;
            case GROWER:
                out = new GrowerHub();
                break;
            case MOCK:
                out = new MockHub();
                break;
        }
        return out;
    }
}
