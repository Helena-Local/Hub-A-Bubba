package org.helenalocal.base.fetch;

import org.helenalocal.base.fetch.test.MockHubFetch;

import java.util.logging.Logger;

/**
 * Created by abbie on 1/24/14.
 */



public abstract class HubFetchFactory {
    public static Logger HubLog = Logger.getLogger("HL- HubLog");

    public static IHubFetch buildHubFetch(int itype) {
        IHubFetch out = new MockHubFetch();  // default to test
        switch (itype) {
            case HubFetch.SALES:
                out = new SalesHubFetch();
                break;
            case HubFetch.CSA:
                out = new CSAHubFetch();
                break;
            case HubFetch.GROWER:
                out = new GrowerHubFetch();
                break;
            case HubFetch.MOCK:
                out = new MockHubFetch();
                break;
        }
        return out;
    }
}
