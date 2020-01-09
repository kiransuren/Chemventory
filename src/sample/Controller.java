package sample;

import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Controller {

    public TextField idLabel, typeLabel, titleLabel, descLabel, notesLabel, ordDateLabel, expDateLabel, acqDateLabel, precLabel, useLabel, depsLabel, quanLabel;
    ArrayList<Item> itemArr;

    public void onEnterData(){


    }

    public void getItemData(){
        String id = Integer.toString(Integer.parseInt(idLabel.getText());
        String type = typeLabel.getText();
        String title = titleLabel.getText();
        String desc = descLabel.getText();
        String notes = notesLabel.getText();
        String orderDate = ordDateLabel.getText();
        String expiryDate = expDateLabel.getText();
        String acqDate = acqDateLabel.getText();
        String precDate = precLabel.getText();
        String useDate = useLabel.getText();
        String department = depsLabel.getText();
        String quantity = quanLabel.getText();

        Item temp = new Item(id,type, title, desc, notes, orderDate, expiryDate, acqDate, precDate, useDate, department, quantity);
        itemArr.add(temp);

    }
}
