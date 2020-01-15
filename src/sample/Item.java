/**
 *
 * Damon Omand
 *
 */

package sample;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    String ID, type, title, description, notes, dateOrdered, expiryDate, dateAcq, prec, usage, dept, quantity;//values from main document/data collection
    boolean isExpiring=false,lowStock=false;

    public Item(String _ID, String _type, String _title, String _description, String _notes, String _dateOrdered, String _expiryDate,String _dateAcq,String _prec,String _usage,String _dept,String _quantity){
        ID = _ID;//converting values to values in object class
        type = _type;
        title = _title;
        description = _description;
        notes = _notes;
        dateOrdered = _dateOrdered;
        expiryDate = _expiryDate;
        dateAcq = _dateAcq;
        prec = _prec;
        usage = _usage;
        dept = _dept;
        quantity = _quantity;
  }

    public String getViewString(){
        return title + " | " + ID + " | " + description + " | " + quantity;
  }


    /*public String testIsExpired(String expiryDate) throws ParseException {
      //Calculate Expiration
      String answer =" ";
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      Date expiry = formatter.parse(expiryDate);
      Date currentDate = new Date();

      double diff = expiry.getTime()- currentDate.getTime();
      diff =diff/86400000;

      if (expiry.compareTo(currentDate)<0){
          answer= "Product Expired";
      }else if(diff<=7){
          //answer= "Expiring in "+diff+" days";
      }
        return answer;
    }*/

    public double isExpired() throws ParseException {
        //Calculate Expiration
        String answer =" ";
        String expiryDate = this.expiryDate;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date expiry = formatter.parse(expiryDate);
        Date currentDate = new Date();

        double diff = expiry.getTime()- currentDate.getTime();
        diff =diff/86400000;

        return diff;
    }

    public String getExpiryString() {
        try{
            int diff = (int) Math.round(this.isExpired());
            if(diff <= 0){
                return title + " | Item has EXPIRED | ID: " + ID;
            }else{
                return title + " | Expires in " + diff + " days |  ID: " + ID;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "ERROR OCCURRED";
    }

    public String getLowStockString(){
        return title + " | Current Quantity is " + quantity + " |  ID: " + ID;
    }

}
