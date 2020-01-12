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


      public String isExpired(String expiryDate) throws ParseException {
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
              answer= "Expiring in "+diff+" days";
          }
            return answer;
    }
      public String lowQuant(String quantity) {//if quantity < 3, notify
          String r = " ";
          try {
              int q = Integer.parseInt(quantity);
              if (q <= 3) {
                  r = "Low stock, only " + q + "left";
              }
          } catch (Exception e) {


          }
          return r;
      }
}
