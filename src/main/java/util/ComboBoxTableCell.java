package main.java.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

public class ComboBoxTableCell<T> extends TableCell<T, String> {

    private ComboBox<String> comboBox;
    private Callback<T,ObservableList<String>> optionsCallBack;

    public ComboBoxTableCell(Callback<T,ObservableList<String>> optionsCallBack) {
        this.comboBox = new ComboBox<>();
        this.optionsCallBack = optionsCallBack;
    }


    @Override
    public void startEdit()
    {
        if ( !isEmpty() )
        {
            super.startEdit();

            
			comboBox.setItems( optionsCallBack.call(getTableView().getItems().get( getIndex() )));
            comboBox.getSelectionModel().select( getItem() );
            comboBox.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) { 
                    this.commitEdit(comboBox.getValue());
                }
            });
            comboBox.focusedProperty().addListener( new ChangeListener<Boolean>()
            {
                @Override
                public void changed( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue )
                {
                    if ( !newValue )
                    {
                        commitEdit( comboBox.getSelectionModel().getSelectedItem() );
                    }
                }
            } );

            setText( null );
            setGraphic( comboBox );
        }
    }


    @Override
    public void cancelEdit()
    {
        super.cancelEdit();

        setText( ( String ) getItem() );
        setGraphic( null );
    }


    @Override
    public void updateItem( String item, boolean empty )
    {
        super.updateItem( item, empty );

        if ( empty )
        {
            setText( null );
            setGraphic( null );
        }
        else
        {
            if ( isEditing() )
            {
                setText( null );
                setGraphic( comboBox );
            }
            else
            {
                setText( getItem() );
                setGraphic( null );
            }
        }
    }

}