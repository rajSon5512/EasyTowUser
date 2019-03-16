package com.example.somzzzzz.easytowuser.Activity.Model;

public class Transactions {

        private String ORDERID;
   /*     private String date;
        private String status;
   */     private String FINE;
     /*   private String uid;
*/
        public void setORDERID(String ORDERID) {
                this.ORDERID = ORDERID;
        }

  /*      public void setDate(String date) {
                this.date = date;
        }

        public void setStatus(String status) {
                this.status = status;
        }
*/
        public void setFINE(String FINE) {
                this.FINE = FINE;
        }

  /*      public void setUid(String uid) {
                this.uid = uid;
        }
*/
        public Transactions(){

                this.ORDERID =null;
       /*         this.date=null;
                this.status=null;
       */         this.FINE =null;
         //       this.uid=null;
        }


        public Transactions(String orderId, String date, String status, String FINE, String uid) {
                this.ORDERID = orderId;
  /*              this.date = date;
                this.status = status;
  */              this.FINE = FINE;
    /*            this.uid = uid;
    */    }

        public String getORDERID() {
                return ORDERID;
        }

      /*  public String getDate() {
                return date;
        }

        public String getStatus() {
                return status;
        }
*/
        public String getFINE() {
                return FINE;
        }

  /*      public String getUid() {
                return uid;
        }
*/
}
