/**
 *
 * Damon Omand
 *
 */

package sample;


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
}
