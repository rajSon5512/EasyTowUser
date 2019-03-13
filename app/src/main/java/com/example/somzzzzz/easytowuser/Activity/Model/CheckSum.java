package com.example.somzzzzz.easytowuser.Activity.Model;

public class CheckSum {

    private String MID;
    private String ORDER_ID;
    private String CUST_ID;
    private String INDUSTRY_TYPE_ID;
    private String CHANNEL_ID;
    private String TXN_AMOUNT;
    private String WEBSITE;
    private String CALLBACK_URL;
    private String EMAIL;
    private String MOBILE_NO;
    private String CHECKSUMHASH;

    public CheckSum(String MID, String ORDER_ID, String CUST_ID, String INDUSTRY_TYPE_ID, String CHANNEL_ID, String TXN_AMOUNT, String WEBSITE, String CALLBACK_URL, String EMAIL, String MOBILE_NO, String CHECKSUMHASH) {

        this.MID = MID;
        this.ORDER_ID = ORDER_ID;
        this.CUST_ID = CUST_ID;
        this.INDUSTRY_TYPE_ID = INDUSTRY_TYPE_ID;
        this.CHANNEL_ID = CHANNEL_ID;
        this.TXN_AMOUNT = TXN_AMOUNT;
        this.WEBSITE = WEBSITE;
        this.CALLBACK_URL = CALLBACK_URL;
        this.EMAIL = EMAIL;
        this.MOBILE_NO = MOBILE_NO;
        this.CHECKSUMHASH = CHECKSUMHASH;
    }

    public String getMID() {
        return MID;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getCUST_ID() {
        return CUST_ID;
    }

    public void setCUST_ID(String CUST_ID) {
        this.CUST_ID = CUST_ID;
    }

    public String getINDUSTRY_TYPE_ID() {
        return INDUSTRY_TYPE_ID;
    }

    public void setINDUSTRY_TYPE_ID(String INDUSTRY_TYPE_ID) {
        this.INDUSTRY_TYPE_ID = INDUSTRY_TYPE_ID;
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public String getTXN_AMOUNT() {
        return TXN_AMOUNT;
    }

    public void setTXN_AMOUNT(String TXN_AMOUNT) {
        this.TXN_AMOUNT = TXN_AMOUNT;
    }

    public String getWEBSITE() {
        return WEBSITE;
    }

    public void setWEBSITE(String WEBSITE) {
        this.WEBSITE = WEBSITE;
    }

    public String getCALLBACK_URL() {
        return CALLBACK_URL;
    }

    public void setCALLBACK_URL(String CALLBACK_URL) {
        this.CALLBACK_URL = CALLBACK_URL;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getCHECKSUMHASH() {
        return CHECKSUMHASH;
    }

    public void setCHECKSUMHASH(String CHECKSUMHASH) {
        this.CHECKSUMHASH = CHECKSUMHASH;
    }


    /*
     "MID": "dbMIND07876515785068",
             "ORDER_ID": "ORDER00001",
             "CUST_ID": "CUST0001",
             "INDUSTRY_TYPE_ID": "Retail",
             "CHANNEL_ID": "WAP",
             "TXN_AMOUNT": "1.00",
             "WEBSITE": "WEBSTAGING",
             "CALLBACK_URL": "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp",
             "EMAIL": "abc@gmail.com",
             "MOBILE_NO": "9999999999",
             "CHECKSUMHASH": "G4jOtaeMjBdYeKPiyiQZLp6EQupuweiLmU1QFiQbOfbXqc+ryHCq7129W4yLfRKLDwiyNILUIzMN5iGF0TwLZiMPtvR07+gcgDrZpoe0YzE="


*/
}
