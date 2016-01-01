/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simz1;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CHAM PC
 */
public class viewOrderTableModel extends DefaultTableModel {
    public viewOrderTableModel() {
      super(new String[]{"Product ID", "Product Name","Ordered Quantity"}, 0);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
      Class clazz = String.class;
      switch (columnIndex) {
        case 0:
          clazz = String.class;
          break;
      }
      return clazz;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
      if(column == 1){
        Vector rowData = (Vector)getDataVector().get(row);
        rowData.set(1, aValue);
        fireTableCellUpdated(row, column);
      }
    }
}
