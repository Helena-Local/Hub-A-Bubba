package org.helenalocal.base;

import org.helenalocal.base.get.ItemHub;
import org.helenalocal.base.post.GrowerHub;
import org.helenalocal.base.test.MockHub;

/**
 * Created by abbie on 1/24/14.
 */



public abstract class HubFactory {
    public static IHub buildHubFetch(Hub.HubType itype) {
        IHub out = new MockHub();  // default to test
        switch (itype) {
            case ITEM:
                out = new ItemHub();
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
