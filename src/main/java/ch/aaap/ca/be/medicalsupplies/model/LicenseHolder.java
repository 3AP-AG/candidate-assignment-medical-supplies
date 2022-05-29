package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;

public class LicenseHolder {
    private String licenseHolderId;
    private String licenseHolderName;
    private String licenseHolderAddress;

    LicenseHolder(MSProductRow msProductRow) {
        this.licenseHolderId = msProductRow.getLicenseHolderId();
        this.licenseHolderName = msProductRow.getLicenseHolderName();
        this.licenseHolderAddress = msProductRow.getLicenseHolderAddress();
    }

    public String getLicenseHolderId() {
        return licenseHolderId;
    }
}
