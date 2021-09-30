package backoffice_credentials;

import enums.ReturnReason;

public class Package {

    private final static String reasonIDCore = "private_reason_key_";

    private String packageUrl;
    private String returnReason;

    public Package() {}

    public Package(String url, ReturnReason returnReason) {
        this.packageUrl = url;
        this.returnReason = reasonIDCore.concat(String.valueOf(returnReason));
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public String getReturnReason() {
        return returnReason;
    }
}
