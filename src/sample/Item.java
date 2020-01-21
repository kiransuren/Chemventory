/**
 *  Item Class
 *
 *  Properties:
 *  type, title, description, notes, notes dateOrdered, expiryDate, dateAcq, prec, usage, dept, quantity, ID
 *
 *
 */

package sample;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Item {

    //Properties
    String type, title, description, notes, dateOrdered, expiryDate, dateAcq, prec, usage, dept, quantity;
    int ID;

    public Item(int _ID, String _type, String _title, String _description, String _notes, String _dateOrdered, String _expiryDate, String _dateAcq, String _prec, String _usage, String _dept, String _quantity){
        //Class Initializer
        ID = _ID;
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
        /**
         *  Formats date into a String to display on the main display on search
         */
        return "ID#" + ID + " Title: " + title + " Description: " + description + " Quantity: "+ quantity;
  }

    public double isExpired() throws ParseException {
        /**
         * Calculate the difference between current date and the expiry date of the item
         */

        String expiryDate = this.expiryDate;                                           //Get object's expiry date
        SimpleDateFormat formatter = new SimpleDateFormat(  "dd/MM/yyyy");     //Create format for date
        Date expiry = formatter.parse(expiryDate);                                    //Parse expiry date
        Date currentDate = new Date();

        double diff = expiry.getTime()- currentDate.getTime();                        //Calculate difference in terms of seconds
        diff = diff/86400000;                                                         //Convert seconds to days

        return diff;
    }

    public String getExpiryString() {
        /**
         * Formats string for expiry display
         */

        try{
            int diff = (int) Math.round(this.isExpired());
            if(diff <= 0){
                //if the expiry date is today or in the past
                return title + " | Item has EXPIRED | ID: " + ID;
            }else{
                //if the expiry date is yet to come
                return title + " | Expires in " + diff + " days |  ID: " + ID;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "ERROR OCCURRED";
    }

    public String getLowStockString(){
        /**
         * Format string for low stock display
         */
        return title + " | Current Quantity is " + quantity + " |  ID: " + ID;
    }

}
