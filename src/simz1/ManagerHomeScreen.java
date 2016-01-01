/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simz1;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import static simz1.LoginFrame1.mhp;
import static simz1.LoginFrame1.spi;

/**
 *
 * @author DELL
 */
public class ManagerHomeScreen extends javax.swing.JFrame {

    public static MyTableModel model1;

    AutoSuggest as = new AutoSuggest();
    DBOperations dbOps = new DBOperations();
    Vector<String> v = new Stack<String>();
    Vector<String> v2 = new Stack<String>();
    private boolean hide_flag = false;
    JTextField tx, tx2;
    public int rawNo, incmRaw = 0;
    public static int orderRowNo;
    JComboBox combodesig = new JComboBox();

    java.util.Date date = new java.util.Date();
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    String time = sdf.format(date);
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy:MM:dd");
    String today = sdf2.format(date);

    public void clocker() {
        class Listner implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);
                int sec = cal.get(Calendar.SECOND);
                if (sec < 10) {
                    timeLabel.setText(hour + ":" + min + ":" + "0" + sec);
                } else if (min < 10) {
                    timeLabel.setText(hour + ":" + "0" + min + ":" + sec);
                } else {
                    timeLabel.setText(hour + ":" + min + ":" + sec);
                }

                if (min < 10 && sec < 10) {
                    timeLabel.setText(hour + ":" + "0" + min + ":" + "0" + sec);
                }

            }
        }
        Timer t = new Timer(1000, new Listner());
        t.start();
    }

    public void autoSuggest() {

        Search.removeAllItems();
        try {
            ResultSet rst = dbOps.getProducts();
            rst.first();
            if (Search.getItemCount() == 0) {
                do {
                    Search.addItem(rst.getString(1));
                    v.addElement(rst.getString(1));
                    Search.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent ie) {
                            if (ie.getStateChange() == ItemEvent.SELECTED) {
                                Search.getSelectedIndex();
                            }
                        }
                    });
                } while (rst.next());
            } else {
                Search.addItem("");
            }
        } catch (SQLException e) {
        }

        //jComboBoxSearch.setEditable(true);
        tx = (JTextField) Search.getEditor().getEditorComponent();
        tx.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        String text = tx.getText();
                        if (text.length() == 0) {
                            Search.hidePopup();
                            setModel(new DefaultComboBoxModel(v), "");
                        } else {
                            DefaultComboBoxModel m = getSuggestedModel(v, text);
                            if (m.getSize() == 0) {
                                Search.hidePopup();
                            } else {
                                setModel(m, text);
                                Search.showPopup();
                            }
                        }
                    }
                });
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                String txt = tx.getText();
                int code = ke.getKeyCode();
                if (code == KeyEvent.VK_ESCAPE) {
                    hide_flag = true;
                } else if (code == KeyEvent.VK_ENTER) {
                    for (int i = 0; i < v.size(); i++) {
                        String str = (String) v.elementAt(i);
                        if (str.toLowerCase().startsWith(txt)) {
                            tx.setText(str);
                            viewProduct vw = new viewProduct(str);
                            vw.setVisible(true);
                            vw.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            return;
                        } else if (str.equals(tx.getText())) {
                            viewProduct vw = new viewProduct(str);
                            vw.setVisible(true);
                            vw.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            return;
                        }
                    }
                }
                Search.setSelectedIndex(-1);
            }

        });
    }

    public void autoSuggest2() {
        jcomboAddTodaysStock.removeAllItems();
        try {
            ResultSet rst = dbOps.getProducts();
            rst.first();
            if (jcomboAddTodaysStock.getItemCount() == 0) {
                do {
                    jcomboAddTodaysStock.addItem(rst.getString(1));
                    v2.addElement(rst.getString(1));
                    jcomboAddTodaysStock.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent ie) {
                            if (ie.getStateChange() == ItemEvent.SELECTED) {
                                jcomboAddTodaysStock.getSelectedIndex();
                            }
                        }
                    });
                } while (rst.next());
            } else {
                jcomboAddTodaysStock.addItem("");
            }
        } catch (SQLException e) {
        }

        //jComboBoxSearch.setEditable(true);
        tx2 = (JTextField) jcomboAddTodaysStock.getEditor().getEditorComponent();
        tx2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        String text = tx2.getText();
                        if (text.length() == 0) {
                            jcomboAddTodaysStock.hidePopup();
                            setModel2(new DefaultComboBoxModel(v2), "");
                        } else {
                            DefaultComboBoxModel m = getSuggestedModel(v2, text);
                            if (m.getSize() == 0) {
                                jcomboAddTodaysStock.hidePopup();
                            } else {
                                setModel2(m, text);
                                jcomboAddTodaysStock.showPopup();
                            }
                        }
                    }
                });
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                String txt = tx2.getText();

                int code = ke.getKeyCode();
                if (code == KeyEvent.VK_ESCAPE) {
                    hide_flag = true;
                } else if (code == KeyEvent.VK_ENTER) {
                    for (int i = 0; i < v2.size(); i++) {
                        String str = (String) v2.elementAt(i);
                        if (str.toLowerCase().startsWith(txt)) {
                            try {
                                tx2.setText(str);
                                DefaultTableModel model = (DefaultTableModel) tableProduct.getModel();
                                ResultSet rst = dbOps.viewStock2(str);
                                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                                if (rst.next()) {
                                    boolean flag2 = true;
                                    for (int k = 0; k < model.getRowCount(); k++) {
                                        if (model.getValueAt(k, 2).equals(str)) {
                                            flag2 = false;
                                        }
                                    }
                                    if (flag2 == true) {
                                        model.addRow(new Object[]{true, rst.getInt(1), rst.getString(3), rst.getString(5), rst.getString(6), 0, 0});
                                    }
                                    //jcomboAddTodaysStock.setSelectedIndex(-1);
                                }
                                return;
                            } catch (SQLException ex) {

                            }
                        } else if (str.equals(tx.getText())) {
                            try {
                                DefaultTableModel model = (DefaultTableModel) tableProduct.getModel();
                                ResultSet rst = dbOps.viewStock2(str);
                                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                                if (rst.next()) {
                                    boolean flag3 = true;
                                    for (int k = 0; k < model.getRowCount(); k++) {
                                        if (model.getValueAt(k, 2).equals(str)) {
                                            flag3 = false;
                                        }
                                    }
                                    if (flag3 == true) {
                                        model.addRow(new Object[]{true, rst.getInt(1), rst.getString(3), rst.getString(5), rst.getString(6), 0, 0});
                                    }
                                    //jcomboAddTodaysStock.setSelectedIndex(-1);
                                }
                                return;
                            } catch (SQLException ex) {

                            }
                        }
                    }
                }
            }

        });
    }

    private void setModel(DefaultComboBoxModel mdl, String str) {
        Search.setModel(mdl);
        tx.setText(str);
    }

    private void setModel2(DefaultComboBoxModel mdl, String str) {
        jcomboAddTodaysStock.setModel(mdl);
        tx2.setText(str);
    }

    private DefaultComboBoxModel getSuggestedModel(List<String> list, String txt) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {
            if (s.toLowerCase().startsWith(txt)) {
                m.addElement(s);
            }
        }
        return m;
    }

    public ManagerHomeScreen() {
        try {
            initComponents();
            this.btnReset.setVisible(false);
            this.btnSaveChanges.setVisible(false);
            autoSuggest();
            autoSuggest2();
            Search.setSelectedIndex(-1);

            // Get income and expenditure data to the table
            tblIncome.setModel(incomeModel);
            ResultSet rs = dbOps.getIAndExpences();
            while (rs.next()) {
                String Descript = rs.getString(1);
                float Credit = Float.parseFloat(rs.getString(2));
                float Debit = Float.parseFloat(rs.getString(3));
                if (Credit == 0.0) {
                    incomeModel.addRow(new Object[]{Descript, null, Debit});
                } else if (Debit == 0.0) {
                    incomeModel.addRow(new Object[]{Descript, Credit, null});
                } else {
                    incomeModel.addRow(new Object[]{Descript, Credit, Debit});
                }

            }

            jcomboAddTodaysStock.setSelectedIndex(-1);

            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo1.jpg")));

            /*JTable tblUsers2 = new JTable() {
             public boolean isCellEditatable(int row, int column) {
             return column == 4;
             }
             };*/
            ResultSet rst = dbOps.viewUser();
            tblUsers.setModel(DbUtils.resultSetToTableModel(rst));
            combodesig.addItem("Manager");
            combodesig.addItem("Sales Person");
            tblUsers.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(combodesig));
            for (Class c : Arrays.asList(Object.class, Number.class, Boolean.class)) {
                TableCellEditor ce = tblUsers.getDefaultEditor(c);
                if (ce instanceof DefaultCellEditor) {
                    ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
                }
            }
            //this.Search.requestFocusInWindow();
            /*this.jComboBoxSearch = new JComboBox(new Object[] { "Ester", "Jordi",
             "Jordina", "Jorge", "Sergi" });
             AutoCompleteDecorator.decorate(this.jComboBoxSearch);*/
            setMorningStock();
            this.dateLabel.setText(today);
            this.clocker();
            int max = dbOps.getMaxBillID();
            this.billno.setText(max + 1 + "");
        } catch (SQLException ex) {

        }
        //this.Search.requestFocusInWindow();
        setMorningStock();
        TableColumn dateColumn = tableProduct.getColumnModel().getColumn(4);
        dateColumn.setCellEditor(new DatePickerCellEditor());
        //DateCellRenderer renderer = new DateCellRenderer();
        //tableProduct.getColumnModel().getColumn(4).setCellEditor(renderer.getFormats());

        this.dateLabel.setText(today);
        this.clocker();
        int max = dbOps.getMaxBillID();
        this.billno.setText(max + 1 + "");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnAddProduct = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableProduct = new javax.swing.JTable();
        btnReset = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        Search = new javax.swing.JComboBox();
        btnSetStock = new javax.swing.JButton();
        btnSaveChanges = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jcomboAddTodaysStock = new javax.swing.JComboBox();
        btnAddOrderToStock = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        lblStockStatus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        BillingTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        txtCash = new javax.swing.JTextField();
        ItemSelecter = new javax.swing.JComboBox();
        amount = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnBalance = new javax.swing.JButton();
        txtBalance = new javax.swing.JTextField();
        total = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        billLabel = new javax.swing.JLabel();
        billno = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnDeletePrdct = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblIncome = new javax.swing.JTable();
        btnTotalIncome = new javax.swing.JButton();
        btnTotalExpences = new javax.swing.JButton();
        txtTotalIncome = new javax.swing.JTextField();
        txtTotalExpences = new javax.swing.JTextField();
        txtProfit = new javax.swing.JTextField();
        btnGenerateReport = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnProfit = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblOrder = new javax.swing.JTable();
        btnProcessOrder = new javax.swing.JButton();
        btnRefill = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblOrderStatus = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        users = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        btnNewUser = new javax.swing.JButton();
        btnRemoveUser = new javax.swing.JButton();
        btnSaveUser = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        name1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lablePic = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jMenu3.setText("jMenu3");

        jMenu4.setText("jMenu4");

        jMenu5.setText("jMenu5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manager-Home Page ");
        setResizable(false);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        btnAddProduct.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 11)); // NOI18N
        btnAddProduct.setText("Add Product");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        tableProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Product Code", "Name", "Price", "Expiry Date", "Current Quantity", "Total Received Qty.", "If Expired"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableProduct.setGridColor(new java.awt.Color(51, 51, 51));
        tableProduct.setRowHeight(20);
        jScrollPane2.setViewportView(tableProduct);

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Search");

        Search.setEditable(true);
        Search.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });
        Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SearchKeyPressed(evt);
            }
        });

        btnSetStock.setText("Create Today's Stock");
        btnSetStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetStockActionPerformed(evt);
            }
        });

        btnSaveChanges.setText("Save Changes");
        btnSaveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveChangesActionPerformed(evt);
            }
        });

        jLabel9.setText("Add new product to Todays Stock");

        jcomboAddTodaysStock.setEditable(true);
        jcomboAddTodaysStock.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcomboAddTodaysStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcomboAddTodaysStockActionPerformed(evt);
            }
        });
        jcomboAddTodaysStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcomboAddTodaysStockKeyPressed(evt);
            }
        });

        btnAddOrderToStock.setText("Add Orders");
        btnAddOrderToStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddOrderToStockActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Stock Status :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jcomboAddTodaysStock, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddOrderToStock, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSaveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btnSetStock)
                        .addGap(27, 27, 27))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 910, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblStockStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Search, 0, 200, Short.MAX_VALUE))
                                .addGap(451, 451, 451)
                                .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 18, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblStockStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcomboAddTodaysStock, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddOrderToStock)
                    .addComponent(btnSetStock)
                    .addComponent(btnSaveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        jTabbedPane1.addTab("Stock Details", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        BillingTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        BillingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Product Code", "Product Name", "Quantity", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        BillingTable.setGridColor(new java.awt.Color(51, 51, 51));
        BillingTable.setRowHeight(20);
        jScrollPane3.setViewportView(BillingTable);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Cash (Rs.)");

        txtTotal.setEditable(false);
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotal.setText("0");

        txtCash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCashKeyPressed(evt);
            }
        });

        ItemSelecter.setEditable(true);
        ItemSelecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemSelecterActionPerformed(evt);
            }
        });
        ItemSelecter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ItemSelecterFocusLost(evt);
            }
        });
        ItemSelecter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ItemSelecterKeyPressed(evt);
            }
        });

        amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amountActionPerformed(evt);
            }
        });
        amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                amountKeyPressed(evt);
            }
        });

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText(" Quantity");

        btnBalance.setText("Balance (Rs.)");
        btnBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBalanceActionPerformed(evt);
            }
        });

        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setText("Total (Rs.)");

        dateLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateLabel.setText("Date");

        billLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        billLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        billLabel.setText("Bill No:");

        billno.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        billno.setText("Bill No");

        timeLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeLabel.setText("Time");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Date");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Time");

        btnDeletePrdct.setText("Delete Product");
        btnDeletePrdct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePrdctActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(ItemSelecter, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(billLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(billno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeletePrdct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(txtCash)
                    .addComponent(txtBalance))
                .addGap(31, 31, 31))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ItemSelecter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billno, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDeletePrdct))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBalance)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Transactions  ", jPanel2);

        tblIncome.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Description", "Credit(Rs)", "Debit(Rs)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblIncome);

        btnTotalIncome.setText("Calculate Total Income ");
        btnTotalIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTotalIncomeActionPerformed(evt);
            }
        });

        btnTotalExpences.setText("Calculate Total Expences");
        btnTotalExpences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTotalExpencesActionPerformed(evt);
            }
        });

        txtTotalIncome.setEditable(false);

        txtTotalExpences.setEditable(false);

        txtProfit.setEditable(false);

        btnGenerateReport.setText("Generate Account Report");

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Expences"));

        jLabel12.setText("Description ");

        txtDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescriptionKeyPressed(evt);
            }
        });

        jLabel13.setText("Amount (Rs.) ");

        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAmountKeyPressed(evt);
            }
        });

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDescription)
                    .addComponent(txtAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(33, 231, Short.MAX_VALUE)
                .addComponent(btnAdd)
                .addGap(115, 115, 115))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnAdd)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnProfit.setText("Calculate Profit");
        btnProfit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfitActionPerformed(evt);
            }
        });

        jLabel14.setText("Rs. ");

        jLabel15.setText("Rs. ");

        jLabel16.setText("Rs. ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 876, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnTotalIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTotalExpences, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(btnProfit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel14)
                                .addComponent(jLabel15))
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTotalExpences)
                            .addComponent(txtProfit)
                            .addComponent(txtTotalIncome, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                            .addComponent(btnGenerateReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTotalIncome)
                            .addComponent(jLabel14))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTotalExpences)
                            .addComponent(txtTotalExpences, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProfit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProfit)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGenerateReport, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Income & Expenditure  ", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 949, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Reports  ", jPanel4);

        tblOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Order No", "Product ID", "Product Name", "Date", "Time", "Order Quantity", "Alert"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblOrder);

        btnProcessOrder.setText("Process Order");
        btnProcessOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessOrderActionPerformed(evt);
            }
        });

        btnRefill.setText("Re-fill Quantities");
        btnRefill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefillActionPerformed(evt);
            }
        });

        jLabel6.setText("LABEL");

        jLabel8.setText("alert");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Order Status: ");

        jButton2.setText("View Orders");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(390, 390, 390)
                .addComponent(btnProcessOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnRefill)
                .addContainerGap(52, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblOrderStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 852, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(lblOrderStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(btnRefill)
                    .addComponent(btnProcessOrder))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 949, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Orders  ", jPanel5);

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Emp ID", "Employee First Name", "Employee Last Name", "NIC number", "Designation"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblUsers);

        btnNewUser.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        btnNewUser.setText("New User");
        btnNewUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewUserActionPerformed(evt);
            }
        });

        btnRemoveUser.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        btnRemoveUser.setText("Remove User");
        btnRemoveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveUserActionPerformed(evt);
            }
        });

        btnSaveUser.setText("Save Changes");
        btnSaveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout usersLayout = new javax.swing.GroupLayout(users);
        users.setLayout(usersLayout);
        usersLayout.setHorizontalGroup(
            usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(usersLayout.createSequentialGroup()
                        .addComponent(btnSaveUser)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(usersLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addGroup(usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNewUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRemoveUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        usersLayout.setVerticalGroup(
            usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(usersLayout.createSequentialGroup()
                        .addComponent(btnNewUser, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btnRemoveUser, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnSaveUser)
                .addContainerGap(371, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Users ", users);

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 36)); // NOI18N
        jLabel1.setText("SIMZ");
        jPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 21, -1, -1));
        jPanel7.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(455, 11, -1, -1));

        btnLogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simz1/1439648143_logout.png"))); // NOI18N
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });
        jPanel7.add(btnLogOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 20, 45, 40));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simz1/logo1.jpg"))); // NOI18N
        jPanel7.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, 61));

        jButton1.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 14)); // NOI18N
        jButton1.setText("Edit My Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, -1, 40));

        name1.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 14)); // NOI18N
        name1.setText("Lalith");
        jPanel7.add(name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 18)); // NOI18N
        jLabel2.setText("Logged in As: ");
        jPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 145, -1));

        lablePic.setMaximumSize(new java.awt.Dimension(60, 60));
        lablePic.setMinimumSize(new java.awt.Dimension(60, 60));
        jPanel7.add(lablePic, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 60, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 987, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 956, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void loggedInAs() {
        LoginFrame1 lf = new LoginFrame1();
        String uName = lf.getTxtUserName().getText();
        //name1.setText(uName);
        System.out.println(uName);
    }

    private void btnNewUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewUserActionPerformed
        NewUserFrame nu = new NewUserFrame();
        nu.setVisible(true);
        nu.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }//GEN-LAST:event_btnNewUserActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        this.setVisible(false);
        LoginFrame1 lf = new LoginFrame1();
        lf.setSize(755, 610);
        lf.setVisible(true);
        lf.btnHint.setVisible(false);
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void btnRemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveUserActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
        if (tblUsers.getSelectedRow() == -1) {
            if (tblUsers.getSelectedRow() == 0) {
                JOptionPane.showMessageDialog(this, "Table is empty");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete");
            }
        } else {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            String uID = model.getValueAt(tblUsers.getSelectedRow(), 0).toString();
            int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete user having Employee ID of " + uID + "? ", "Warning", dialogButton);
            if (a == JOptionPane.YES_OPTION) {

                int id = Integer.parseInt(uID);

                int rst = dbOps.removeUser(id);
                if (rst == 1) {
                    JOptionPane.showMessageDialog(this, "User successfully deleted");
                    ResultSet rst1 = dbOps.viewUser();
                    mhp.tblUsers.setModel(DbUtils.resultSetToTableModel(rst1));
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured! User couldn't be deleted");
                }

            } else {
                return;
            }

        }
    }//GEN-LAST:event_btnRemoveUserActionPerformed


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //this.setVisible(false);
        ManagerProfileFrame mpf = new ManagerProfileFrame();
        mpf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mpf.name.setText(mhp.name1.getText());
        mpf.uName.setText(mhp.name1.getText());

        String s1 = mpf.uName.getText();
        String rst = dbOps.getPropic(s1);
        ImageIcon image1 = new ImageIcon(rst);
        ImageIcon image2 = resizeImageIcon(image1, 100, 100);
        mpf.jLabel18.setIcon(image2);

        String tmpName = dbOps.getName(mpf.uName.getText());
        mpf.txtName.setText(tmpName);
        int tmpID = dbOps.getID(mpf.uName.getText());
        mpf.jLId.setText("PSB" + tmpID);
        String tmpNic = dbOps.getNic(mpf.uName.getText());
        mpf.nicLabel.setText(tmpNic);
        mpf.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnSetStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetStockActionPerformed

        DefaultTableModel model = (DefaultTableModel) this.tableProduct.getModel();

        int count = tableProduct.getRowCount();

        int num = 0;
        for (int i = 0; i < count; i++) {
            if ((boolean) tableProduct.getModel().getValueAt(i, 0) == false) {
                num++;
            }
        }
        int[] rows = new int[num];
        int index = 0;
        for (int i = 0; i < count; i++) {
            if ((boolean) tableProduct.getModel().getValueAt(i, 0) == false) {
                rows[index] = i;
                index++;
            }
        }

        for (int i = 0; i < rows.length; i++) {
            model.removeRow(rows[i] - i);
        }
        boolean an = dbOps.deleteTodayStock();
        if (an == false) {
            JOptionPane.showMessageDialog(this, "Error occured while updating previous Today Stock");
            return;
        }

        for (int j = 0; j < model.getRowCount(); j++) {
            int id = Integer.parseInt(tableProduct.getModel().getValueAt(j, 1).toString());

            String dateCrnt = today;
            String dte = "0000-00-00";
            try {
                SimpleDateFormat javadate = new SimpleDateFormat("yyyy-MM-dd");
                dte = javadate.format(tableProduct.getModel().getValueAt(j, 4));

                if ("".equals(dte)) {
                    dte = "0000-00-00";
                } else {
                    Date x = javadate.parse(dte);
                    if ((x.getDate() - date.getDate()) <= 0) {
                        JOptionPane.showMessageDialog(this, "Please enter a reasonable expire date");
                        model.setValueAt(null, j, 4);
                        return;
                    } else {
                        model.setValueAt(dte, j, 4);
                    }

                }
            } catch (NullPointerException | IllegalArgumentException | ParseException ex) {
                //System.out.println(ex);
            }

            int crnt = 0, totl = 0;
            try {
                crnt = Integer.parseInt(tableProduct.getModel().getValueAt(j, 5).toString());
                totl = Integer.parseInt(tableProduct.getModel().getValueAt(j, 6).toString());
                if (totl <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter numbers greater than 0 in quantity field!!!");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter only numbers in quantity field!!!");
                return;
            }
            if (dbOps.getTodayStockQty(id) != null) {
                try {
                    crnt = totl + dbOps.getTodayStockQty(id).getInt(1);

                    if ("0000-00-00".equals(dte)) {
                        dte = dbOps.getTodayStockQty(id).getString(3);
                        //sqldte = new java.sql.Date(dte.getDay());
                    }

                } catch (SQLException ex) {
                    System.out.println(ex);
                }

                boolean c = dbOps.updateTodayStockQty(id, dateCrnt, totl, crnt, dte, totl);
                model.setValueAt(crnt, j, 5);
                model.setValueAt(dte, j, 4);
                model.setValueAt(totl, j, 6);
                if (c == false) {
                    JOptionPane.showMessageDialog(this, "Error occured while updating a product in previous Today Stock");
                    return;
                }
            } else {
                crnt = crnt + totl;
                boolean ans = dbOps.setTodayStock(id, dateCrnt, totl, crnt, dte, totl);
                model.setValueAt(crnt, j, 5);
                model.setValueAt(totl, j, 6);
                if (ans == false) {
                    JOptionPane.showMessageDialog(this, "Error occured while creating Today Stock");
                    return;
                }
            }
        }

        try {
            ResultSet rst = dbOps.searchTodayStock();
            ArrayList<Integer> tmp = new ArrayList<>();
            for (int k = 0; k < model.getRowCount(); k++) {
                int Id = Integer.parseInt(tableProduct.getModel().getValueAt(k, 1).toString());
                tmp.add(Id);
            }
            while (rst.next()) {
                int id1 = rst.getInt(1);
                if (!tmp.contains(id1)) {
                    //System.out.println(id1);
                    try {
                        ResultSet rs = dbOps.combineTwoTables(id1, today);
                        while (rs.next()) {
                            String s1 = rs.getString(1);
                            int s2 = rs.getInt(2);
                            String s3 = rs.getString(3);
                            int s4 = rs.getInt(4);
                            int s5 = rs.getInt(5);
                            if ((rs.getDate(3).getDate() - rs.getDate(6).getDate()) <= 3) {
                                model.addRow(new Object[]{true, id1, s1, s2, s3, s4, s5, 1});
                            } else {
                                model.addRow(new Object[]{true, id1, s1, s2, s3, s4, s5, 0});
                            }

                        }

                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        as.autoSuggest(ItemSelecter);
        ItemSelecter.setSelectedIndex(-1);

        //setting the created stock to the salesperson
        tableModelSalesperson tmSPmodel = new tableModelSalesperson();
        spi.SalesPStock.setModel(tmSPmodel);
        for (int k = 0; k < model.getRowCount(); k++) {
            int Id = Integer.parseInt(tableProduct.getModel().getValueAt(k, 1).toString());
            int exp = 0;
            try {
                exp = Integer.parseInt(tableProduct.getModel().getValueAt(k, 7).toString());
            } catch (NullPointerException x) {

            }

            ResultSet rs = dbOps.combineTwoTablesForSP(Id);
            try {
                while (rs.next()) {
                    if (rs.isFirst()) {
                        if (exp == 0) {
                            tmSPmodel.addRow(new Object[]{Id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), 0});
                        } else {
                            tmSPmodel.addRow(new Object[]{Id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), 1});
                        }

                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

        //setting the default order set in orders table
        DefaultTableModel modelOrder = (DefaultTableModel) this.tblOrder.getModel();
        ResultSet rst = dbOps.combineAfternoonStockAndStock();
        int orderTableRows = 0;
        int max = dbOps.getMaxOrderID();
        try {
            while (rst.next()) {
                modelOrder.insertRow(orderTableRows, new Object[]{true, max + 1, rst.getInt(1), rst.getString(2), today, time, rst.getString(3)});
                orderTableRows++;
            }
        } catch (SQLException ex) {

        }
        
        int reply = JOptionPane.showConfirmDialog(null, "Todays Stock has been created successfully \n Do you wish to pay now?", "", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            mhp.jTabbedPane1.setSelectedIndex(2);
            txtDescription.requestFocusInWindow();
        }

        tableProduct.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                int tmpEx = 0;
                try {
                    tmpEx = Integer.parseInt(table.getModel().getValueAt(row, 7).toString());
                } catch (NullPointerException s) {

                }

                if (tmpEx == 1) {
                    setBackground(Color.RED);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }

                return this;
            }

        });
        lblStockStatus.setText("Morning Stock is set");
        lblOrderStatus.setText("Afternoon Stock is due to order");
        btnSetStock.setVisible(false);
        btnSaveChanges.setVisible(true);
    }//GEN-LAST:event_btnSetStockActionPerformed

    private void SearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchKeyPressed

    }//GEN-LAST:event_SearchKeyPressed

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchActionPerformed

//Set default morning stock to the tableProduct table
    private void setMorningStock() {
        ResultSet rst = dbOps.combineMorningStockAndStock();
        //MyTableModel model = new MyTableModel();
        model1 = new MyTableModel();
        tableProduct.setModel(model1);
        try {
            while (rst.next()) {
                model1.addRow(new Object[]{true, rst.getString(1), rst.getString(2), rst.getString(3), rst.getDate(4), rst.getString(5), rst.getString(6), 0});
            }
        } catch (SQLException ex) {
            //Logger.getLogger(ManagerHomeScreen.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
        }
    }

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        try {
            int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset? ", "warning", JOptionPane.YES_NO_OPTION);
            if (a == JOptionPane.YES_OPTION) {
                ResultSet rst = dbOps.viewStock();
                MyTableModel model = new MyTableModel();
                tableProduct.setModel(model);

                OrderTableModel model2 = new OrderTableModel();
                tblOrder.setModel((TableModel) model2);//new table model for order table.....

                this.btnSetStock.setVisible(true);
                while (rst.next()) {
                    model.addRow(new Object[]{false, rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), 0});
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        addProduct ad = new addProduct();
        ad.setVisible(true);
        ad.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        int quantity = 0;
        if (amount.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "First you should select an item");
        } else if (ItemSelecter.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Quantity field cannot be null");
        } else {
            int crntQty = dbOps.getPrdctQty(String.valueOf(ItemSelecter.getSelectedItem()));
            try {
                quantity = Integer.parseInt(amount.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter only numbers!!!");
                amount.setText("");
                amount.requestFocusInWindow();
                return;
            }
            if (quantity < 1) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be less than 1!!!");
                amount.setText("");
                amount.requestFocusInWindow();
                return;
            }
            if (crntQty == -1) {
                JOptionPane.showMessageDialog(this, "This product is not in the stock!!!");
                ItemSelecter.setSelectedIndex(-1);
                amount.setText("");
                ItemSelecter.requestFocusInWindow();
                return;
            } else if (crntQty < quantity) {
                JOptionPane.showMessageDialog(this, "There is only " + crntQty + " items left in the stock!!!");
                amount.setText("");
                amount.requestFocusInWindow();
                return;
            }
            try {
                String txt = (String) ItemSelecter.getEditor().getItem();
                ResultSet rst = dbOps.getPID(txt);

                while (rst.next()) {
                    if (rawNo != 0) {
                        for (int i = 0; i < rawNo; i++) {
                            if (Integer.parseInt(BillingTable.getValueAt(i, 0).toString()) == rst.getInt(1)) {
                                int oldQuantity = Integer.parseInt(BillingTable.getValueAt(i, 2).toString());
                                int a = rst.getInt(3);
                                BillingTable.setValueAt(a * quantity, i, 3);
                                String c = rst.getString(1);
                                BillingTable.setValueAt(c, i, 0);
                                String b = rst.getString(2);
                                BillingTable.setValueAt(b, i, 1);
                                BillingTable.setValueAt(quantity, i, 2);
                                txtTotal.setText(String.valueOf(Integer.parseInt(txtTotal.getText()) + a * quantity - (oldQuantity * a)));
                                ItemSelecter.setSelectedIndex(-1);
                                amount.setText(null);
                                return;
                            }
                        }
                        int a = rst.getInt(3);
                        BillingTable.setValueAt(a * quantity, rawNo, 3);
                        txtTotal.setText(String.valueOf(Integer.parseInt(txtTotal.getText()) + a * quantity));
                        String c = rst.getString(1);
                        BillingTable.setValueAt(c, rawNo, 0);
                        String b = rst.getString(2);
                        BillingTable.setValueAt(b, rawNo, 1);
                        BillingTable.setValueAt(quantity, rawNo, 2);
                        rawNo++;

                    } else {
                        int a = rst.getInt(3);
                        BillingTable.setValueAt(a * quantity, rawNo, 3);
                        txtTotal.setText(String.valueOf(Integer.parseInt(txtTotal.getText()) + a * quantity));
                        String c = rst.getString(1);
                        BillingTable.setValueAt(c, rawNo, 0);
                        String b = rst.getString(2);
                        BillingTable.setValueAt(b, rawNo, 1);
                        BillingTable.setValueAt(quantity, rawNo, 2);
                        rawNo++;
                    }

                }
                ItemSelecter.setSelectedIndex(-1);
                amount.setText(null);
                BillingTable.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent ke) {
                        int code = ke.getKeyCode();
                        DefaultTableModel model = (DefaultTableModel) BillingTable.getModel();
                        int selectedRow = BillingTable.getSelectedRow();
                        if ((code == KeyEvent.VK_DELETE) && (selectedRow != -1)) {
                            int tot = (int) model.getValueAt(selectedRow, 3);
                            int temp = Integer.parseInt(txtTotal.getText());
                            txtTotal.setText(temp - tot + "");
                            model.removeRow(selectedRow);
                            rawNo--;
                        }
                        ItemSelecter.requestFocusInWindow();
                    }
                });

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error occured while the transaction");
            }
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void amountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountActionPerformed

    private void ItemSelecterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemSelecterActionPerformed

    }//GEN-LAST:event_ItemSelecterActionPerformed

    private void txtCashKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String amount = txtTotal.getText();
            if (txtCash.getText().equals("") || txtTotal.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "No text feild should be empty");
            } else {
                String payment = txtCash.getText();
                int paymenti = 0;
                int amounti = Integer.parseInt(amount);
                try {
                    paymenti = Integer.parseInt(payment);
                    if (paymenti < 0) {
                        JOptionPane.showMessageDialog(this, "Only positive numbers are allowed");
                        txtCash.setText("");
                        return;
                    }
                    if (paymenti < amounti) {
                        JOptionPane.showMessageDialog(this, "Please enter an amonut larger than total");
                        txtCash.setText("");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter only numbers");
                    txtCash.setText("");
                    return;
                }

                int balance = paymenti - amounti;
                txtBalance.setText(String.valueOf(balance));
                int result = JOptionPane.showConfirmDialog(null, "Your balance is Rs " + String.valueOf(balance) + " Print the bill? ", null, JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    String input = JOptionPane.showInputDialog(null, "Don't have change? enter balance you pay  or just enter", "0");
                    try {
                        if (input == null) {
                            amounti = amounti;
                        } else if (Integer.parseInt(input) == 0) {
                            amounti = amounti;
                        } else if (Integer.parseInt(input) > 0) {
                            int actualBalance = Integer.parseInt(input);
                            amounti = paymenti - actualBalance;
                        }
                    } catch (NumberFormatException e) {

                    }
                    dbOps.addTransaction(timeLabel.getText(), today);
                    int billNo = dbOps.getBillID(timeLabel.getText(), today);

                    //add data of the transaction to the income and expenditure
                    tblIncome.setModel(incomeModel);
                    String descript = "bill " + Integer.toString(billNo);

                    incomeModel.addRow(new Object[]{descript, amounti, null});

                    int userID = dbOps.getID(name1.getText());
                    incomeModel.addRow(new Object[]{descript, amounti, null});
                    dbOps.addToIncomeAndExpenditure(userID, descript, amounti, 0);

                    DefaultTableModel model = (DefaultTableModel) this.tableProduct.getModel();//update stock table from 
                    //from transactions(here we get the table model of the stock table)
                    DefaultTableModel model2 = (DefaultTableModel) this.tblOrder.getModel();
                    Bill b1 = new Bill();

                    for (int i = 0; i < rawNo; i++) {
                        int id = Integer.parseInt(BillingTable.getValueAt(i, 0).toString());
                        String prdctName = BillingTable.getValueAt(i, 1).toString();
                        int quantity = Integer.parseInt(BillingTable.getValueAt(i, 2).toString());
                        int subTot = Integer.parseInt(BillingTable.getValueAt(i, 3).toString());

                        b1.printBill.setValueAt(prdctName, i, 0);
                        b1.printBill.setValueAt(quantity, i, 1);
                        b1.printBill.setValueAt(subTot, i, 2);

                        dbOps.addTransaction_2(billNo, id, quantity);
                        int rslt = dbOps.updateTodayStockByTransactions(id, quantity);
                        boolean flag = true;

                        if (rslt == 11) {
                            for (int k = 0; k < model2.getRowCount(); k++) {
                                if (model2.getValueAt(k, 2) == null) {
                                    orderRowNo = 0;
                                    flag = true;
                                    break;
                                }
                                
                                int id2 = 0,id3 = 0;
                                try {
                                    id2 = (int) model2.getValueAt(k, 2);
                                    id3 = (int) model2.getValueAt(k, 7);
                                } catch (NullPointerException ex) {
                                    
                                }
                                
                                if((id == id2) && (model2.getValueAt(k, 7) == null)){
                                    orderRowNo = 0;
                                    flag = true;
                                    model2.setValueAt(1, k, 7);
                                    break;
                                }
                                
                                if ((id == id2) && (id3 == 1)) {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag == true) {
                                NotificationPopup nw2 = new NotificationPopup();
                                nw2.main1("Quantity limit reached for " + prdctName);
                                boolean checkOrder = true;
                                for (int k = 0; k < model2.getRowCount(); k++) {
                                    if (model2.getValueAt(k, 2).equals(id)) {
                                        checkOrder = false;
                                    }
                                }
                                if (checkOrder == true) {
                                    int max = dbOps.getMaxOrderID();
                                    model2.insertRow(orderRowNo, new Object[]{true, max + 1, id, prdctName, today, timeLabel.getText(), 0, 1, 0});
                                    orderRowNo++;
                                }
                            }
                        }

                        for (int j = 0; j < model.getRowCount(); j++) {
                            if (id == Integer.parseInt(model.getValueAt(j, 1).toString())) {
                                int current = (int) model.getValueAt(j, 5);
                                model.setValueAt(current - quantity, j, 5);
                            }
                        }
                    }

                    b1.total.setText(amounti + "");
                    b1.recieve.setText(paymenti + "");
                    b1.balance.setText(balance + "");
                    int max1 = dbOps.getMaxBillID();
                    b1.billnum.setText(max1 + 1 + "");
                    b1.setSize(350, 500);
                    b1.setVisible(true);
                    b1.setDefaultCloseOperation(HIDE_ON_CLOSE);

                    for (int i = 0; i < BillingTable.getRowCount(); i++) {
                        for (int j = 0; j < 4; j++) {
                            BillingTable.setValueAt("", i, j);
                        }
                    }
                    txtTotal.setText("0");
                    txtCash.setText("");
                    txtBalance.setText("");
                    rawNo = 0;

                    int max = dbOps.getMaxBillID();
                    this.billno.setText(max + 1 + "");
                    ItemSelecter.requestFocusInWindow();
                } else if (result == JOptionPane.NO_OPTION) {
                    txtCash.setText("");
                    txtBalance.setText("");
                    ItemSelecter.requestFocusInWindow();
                }

            }
        }
    }//GEN-LAST:event_txtCashKeyPressed

    private void amountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int quantity = 0;
            if (amount.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Quantity field cannot be null");
            } else if (ItemSelecter.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "First you should select an item");
            } else {
                int crntQty = dbOps.getPrdctQty(String.valueOf(ItemSelecter.getSelectedItem()));

                try {
                    quantity = Integer.parseInt(amount.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter only numbers!!!");
                    amount.setText("");
                    amount.requestFocusInWindow();
                    return;
                }

                if (quantity < 1) {
                    JOptionPane.showMessageDialog(this, "Quantity cannot be less than 1!!!");
                    amount.setText("");
                    amount.requestFocusInWindow();
                    return;
                }
                if (crntQty == -1) {
                    JOptionPane.showMessageDialog(this, "This product is not in the stock!!!");
                    ItemSelecter.setSelectedIndex(-1);
                    amount.setText("");
                    ItemSelecter.requestFocusInWindow();
                    return;
                } else if (crntQty < quantity) {
                    JOptionPane.showMessageDialog(this, "There is only " + crntQty + " items left in the stock!!!");
                    amount.setText("");
                    amount.requestFocusInWindow();
                    return;
                }
                try {
                    String txt = (String) ItemSelecter.getEditor().getItem();
                    ResultSet rst = dbOps.getPID(txt);

                    while (rst.next()) {
                        if (rawNo != 0) {
                            for (int i = 0; i < rawNo; i++) {
                                if (Integer.parseInt(BillingTable.getValueAt(i, 0).toString()) == rst.getInt(1)) {
                                    int oldQuantity = Integer.parseInt(BillingTable.getValueAt(i, 2).toString());
                                    int a = rst.getInt(3);
                                    BillingTable.setValueAt(a * quantity, i, 3);
                                    String c = rst.getString(1);
                                    BillingTable.setValueAt(c, i, 0);
                                    String b = rst.getString(2);
                                    BillingTable.setValueAt(b, i, 1);
                                    BillingTable.setValueAt(quantity, i, 2);
                                    txtTotal.setText(String.valueOf(Integer.parseInt(txtTotal.getText()) + a * quantity - (oldQuantity * a)));
                                    amount.setText("");
                                    ItemSelecter.setSelectedIndex(-1);
                                    return;
                                }
                            }
                            int a = rst.getInt(3);
                            BillingTable.setValueAt(a * quantity, rawNo, 3);
                            txtTotal.setText(String.valueOf(Integer.parseInt(txtTotal.getText()) + a * quantity));
                            String c = rst.getString(1);
                            BillingTable.setValueAt(c, rawNo, 0);
                            String b = rst.getString(2);
                            BillingTable.setValueAt(b, rawNo, 1);
                            BillingTable.setValueAt(quantity, rawNo, 2);
                            rawNo++;

                        } else {
                            int a = rst.getInt(3);
                            BillingTable.setValueAt(a * quantity, rawNo, 3);
                            txtTotal.setText(String.valueOf(Integer.parseInt(txtTotal.getText()) + a * quantity));
                            String c = rst.getString(1);
                            BillingTable.setValueAt(c, rawNo, 0);
                            String b = rst.getString(2);
                            BillingTable.setValueAt(b, rawNo, 1);
                            BillingTable.setValueAt(quantity, rawNo, 2);
                            rawNo++;
                        }

                    }
                    amount.setText("");
                    ItemSelecter.setSelectedIndex(-1);
                    ItemSelecter.requestFocusInWindow();
                    BillingTable.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent ke) {
                            int code = ke.getKeyCode();
                            DefaultTableModel model = (DefaultTableModel) BillingTable.getModel();
                            int selectedRow = BillingTable.getSelectedRow();
                            if ((code == KeyEvent.VK_DELETE) && (selectedRow != -1)) {
                                int tot = (int) model.getValueAt(selectedRow, 3);
                                int temp = Integer.parseInt(txtTotal.getText());
                                txtTotal.setText(temp - tot + "");
                                model.removeRow(selectedRow);
                                rawNo--;
                            }
                            ItemSelecter.requestFocusInWindow();
                        }
                    });

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error occured while the transaction");
                }
            }
        }
    }//GEN-LAST:event_amountKeyPressed

    private void btnBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBalanceActionPerformed
        String amount = txtTotal.getText();
        if (txtCash.getText().equals("") || txtTotal.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "No text feild should be empty");
        } else {
            String payment = txtCash.getText();
            float paymenti = 0;
            float amounti = Float.parseFloat(amount);
            try {
                paymenti = Float.parseFloat(payment);
                if (paymenti < 0) {
                    JOptionPane.showMessageDialog(this, "Only positive numbers are allowed");
                    txtCash.setText("");
                    return;
                }
                if (paymenti < amounti) {
                    JOptionPane.showMessageDialog(this, "Please enter an amonut larger than total");
                    txtCash.setText("");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter only numbers");
                txtCash.setText("");
                return;
            }

            float balance = paymenti - amounti;
            txtBalance.setText(String.valueOf(balance));
            int result = JOptionPane.showConfirmDialog(null, "Your balance is Rs " + String.valueOf(balance) + " Print the bill? ", null, JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                String input = JOptionPane.showInputDialog(null, "Don't have change? enter balance you pay  or just enter", "0");
                try {
                    if (input == null) {
                        amounti = amounti;
                    } else if (Integer.parseInt(input) == 0) {
                        amounti = amounti;
                    } else if (Integer.parseInt(input) > 0) {
                        float actualBalance = Float.parseFloat(input);
                        amounti = paymenti - actualBalance;
                    }
                } catch (NumberFormatException e) {

                }
                dbOps.addTransaction(timeLabel.getText(), today);
                int billNo = dbOps.getBillID(timeLabel.getText(), today);

                //add data of the transaction to the income and expenditure table in database and the interface
                tblIncome.setModel(incomeModel);
                String descript = "bill " + Integer.toString(billNo);
                int userID = dbOps.getID(name1.getText());
                incomeModel.addRow(new Object[]{descript, amounti, null});
                dbOps.addToIncomeAndExpenditure(userID, descript, amounti, 0);

                DefaultTableModel model = (DefaultTableModel) this.tableProduct.getModel();//update stock table from 
                //from transactions(here we get the table model of the stock table)
                DefaultTableModel model2 = (DefaultTableModel) this.tblOrder.getModel();
                Bill b1 = new Bill();

                for (int i = 0; i < rawNo; i++) {
                    int id = Integer.parseInt(BillingTable.getValueAt(i, 0).toString());
                    String prdctName = BillingTable.getValueAt(i, 1).toString();
                    int quantity = Integer.parseInt(BillingTable.getValueAt(i, 2).toString());
                    int subTot = Integer.parseInt(BillingTable.getValueAt(i, 3).toString());

                    b1.printBill.setValueAt(prdctName, i, 0);
                    b1.printBill.setValueAt(quantity, i, 1);
                    b1.printBill.setValueAt(subTot, i, 2);

                    dbOps.addTransaction_2(billNo, id, quantity);
                    int rslt = dbOps.updateTodayStockByTransactions(id, quantity);
                    boolean flag = true;

                    if (rslt == 11) {
                        for (int k = 0; k < model2.getRowCount(); k++) {
                                if (model2.getValueAt(k, 2) == null) {
                                    orderRowNo = 0;
                                    flag = true;
                                    break;
                                }
                                
                                int id2 = 0,id3 = 0;
                                try {
                                    id2 = (int) model2.getValueAt(k, 2);
                                    id3 = (int) model2.getValueAt(k, 7);
                                } catch (NullPointerException ex) {
                                    
                                }
                                
                                if((id == id2) && (model2.getValueAt(k, 7) == null)){
                                    orderRowNo = 0;
                                    flag = true;
                                    model2.setValueAt(1, k, 7);
                                    break;
                                }
                                
                                if ((id == id2) && (id3 == 1)) {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag == true) {
                                NotificationPopup nw2 = new NotificationPopup();
                                nw2.main1("Quantity limit reached for " + prdctName);
                                boolean checkOrder = true;
                                for (int k = 0; k < model2.getRowCount(); k++) {
                                    if (model2.getValueAt(k, 2).equals(id)) {
                                        checkOrder = false;
                                    }
                                }
                                if (checkOrder == true) {
                                    int max = dbOps.getMaxOrderID();
                                    model2.insertRow(orderRowNo, new Object[]{true, max + 1, id, prdctName, today, timeLabel.getText(), 0, 1, 0});
                                    orderRowNo++;
                                }
                            }
                    }

                    for (int j = 0; j < model.getRowCount(); j++) {
                        if (id == Integer.parseInt(model.getValueAt(j, 1).toString())) {
                            int current = (int) model.getValueAt(j, 5);
                            model.setValueAt(current - quantity, j, 5);
                        }
                    }
                }

                b1.total.setText(amounti + "");
                b1.recieve.setText(paymenti + "");
                b1.balance.setText(balance + "");
                int max1 = dbOps.getMaxBillID();
                b1.billnum.setText(max1 + 1 + "");
                //b1.setSize(464, 568);
                b1.setVisible(true);
                b1.setDefaultCloseOperation(HIDE_ON_CLOSE);

                for (int i = 0; i < BillingTable.getRowCount(); i++) {
                    for (int j = 0; j < 4; j++) {
                        BillingTable.setValueAt("", i, j);
                    }
                }
                txtTotal.setText("0");
                txtCash.setText("");
                txtBalance.setText("");
                rawNo = 0;

                int max = dbOps.getMaxBillID();
                this.billno.setText(max + 1 + "");
                ItemSelecter.requestFocusInWindow();
            } else if (result == JOptionPane.NO_OPTION) {
                txtCash.setText("");
                txtBalance.setText("");
                ItemSelecter.requestFocusInWindow();
            }

        }
    }//GEN-LAST:event_btnBalanceActionPerformed

    private void btnSaveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveChangesActionPerformed
        DefaultTableModel model = (DefaultTableModel) this.tableProduct.getModel();
        String cdate = today;
        for (int j = 0; j < model.getRowCount(); j++) {
            int id = Integer.parseInt(tableProduct.getModel().getValueAt(j, 1).toString());

            //SimpleDateFormat javadate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dte = "0000-00-00";
            int crnt = 0, totl = 0;
            try {
                crnt = Integer.parseInt(tableProduct.getModel().getValueAt(j, 5).toString());
                totl = Integer.parseInt(tableProduct.getModel().getValueAt(j, 6).toString());
                SimpleDateFormat javadate = new SimpleDateFormat("yyyy-MM-dd");
                dte = javadate.format(tableProduct.getModel().getValueAt(j, 4));
            } catch (NullPointerException | IllegalArgumentException np) {

            }

            try {
                if (dbOps.getTodayStockQty(id).getInt(2) != totl) {
                    try {
                        ResultSet rs = dbOps.getTodayStockQty(id);
                        crnt = crnt + totl;
                        totl = totl + rs.getInt(2);

                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }

                    boolean c = dbOps.updateTodayStockQty2(id, cdate, totl, crnt, dte);
                    if ("0000-00-00".equals(dte)) {
                        model.setValueAt("", j, 4);
                        model.setValueAt(crnt, j, 5);
                        model.setValueAt(totl, j, 6);
                    } else {
                        model.setValueAt(dte, j, 4);
                        model.setValueAt(crnt, j, 5);
                        model.setValueAt(totl, j, 6);
                    }
                    if (c == false) {
                        JOptionPane.showMessageDialog(this, "Error occured while updating a product in current Today Stock");
                        return;
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }


        /*if (tableProduct.isColumnSelected(6)) {
         for (int i = 0; i < tableProduct.getRowCount(); i++) {
         //tableProduct.setValueAt("mika", i, 5);
         }
         }*/

    }//GEN-LAST:event_btnSaveChangesActionPerformed


    private void ItemSelecterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ItemSelecterFocusLost

        // TODO add your handling code here:
    }//GEN-LAST:event_ItemSelecterFocusLost

    private void btnProcessOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessOrderActionPerformed
        OrderConfirmation oc = new OrderConfirmation();
        oc.setVisible(true);
        oc.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }//GEN-LAST:event_btnProcessOrderActionPerformed

    private void ItemSelecterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ItemSelecterKeyPressed
        int code = evt.getKeyCode();

        if (code == KeyEvent.VK_F2) {
            txtCash.requestFocusInWindow();
        }

    }//GEN-LAST:event_ItemSelecterKeyPressed

    private void jcomboAddTodaysStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcomboAddTodaysStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcomboAddTodaysStockActionPerformed

    private void jcomboAddTodaysStockKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcomboAddTodaysStockKeyPressed

    }//GEN-LAST:event_jcomboAddTodaysStockKeyPressed

    private void btnAddOrderToStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddOrderToStockActionPerformed
        AddOrderToStock order = new AddOrderToStock();
        order.setVisible(true);
        order.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }//GEN-LAST:event_btnAddOrderToStockActionPerformed

    private void btnRefillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefillActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) this.tblOrder.getModel();
        int rowCount = model2.getRowCount();
        int current = 0, regular = 0;
        for (int i = 0; i < rowCount; i++) {
            if (model2.getValueAt(i, 2) == null) {
                break;
            } else {
                int id = Integer.parseInt(model2.getValueAt(i, 2).toString());

                ResultSet result = dbOps.searchTodayStock();
                try {
                    while (result.next()) {
                        int ID = result.getInt(1);
                        if (id == ID) {
                            ResultSet rst = dbOps.getTodayStockQty(id);
                            //while(rst.next()){
                            current = rst.getInt(1);
                            regular = rst.getInt(4);
                            System.out.println(current);
                            //model2.setValueAt((regular - current), i, 6);
                            //}
                            System.out.println(current);
                            //System.out.println(regular);
                            model2.setValueAt((regular - current), i, 6);
                        }
                    }
                } catch (SQLException ex) {

                }

            }

        }
    }//GEN-LAST:event_btnRefillActionPerformed

    // set table model for the income and expenditure table
    IncomeTableModel incomeModel = new IncomeTableModel();

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        tblIncome.setModel(incomeModel);
        String description = txtDescription.getText();

        DecimalFormat roundValue = new DecimalFormat("###.##");
        float paidAmount = Float.valueOf(roundValue.format(Float.parseFloat(txtAmount.getText())));
        mhp.incomeModel.addRow(new Object[]{description, null, paidAmount});
        txtDescription.setText("");
        txtAmount.setText("");
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String description = txtDescription.getText();
            DecimalFormat roundValue = new DecimalFormat("###.##");
            float paidAmount = Float.valueOf(roundValue.format(Float.parseFloat(txtAmount.getText())));
            tblIncome.setModel(incomeModel);
            incomeModel.addRow(new Object[]{description, null, paidAmount});

            txtDescription.setText("");
            txtAmount.setText("");

            txtDescription.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAmountKeyPressed

    private void txtDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescriptionKeyPressed
        int code = evt.getKeyCode();
        if (code == KeyEvent.VK_ENTER) {
            txtAmount.requestFocusInWindow();
        }
        if (code == KeyEvent.VK_TAB) {
            txtAmount.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtDescriptionKeyPressed

    private void btnTotalIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalIncomeActionPerformed
        int rows = tblIncome.getRowCount();
        float totalIncome = 0;
        for (int i = 0; i < rows; i++) {

            if (tblIncome.getValueAt(i, 1) != null) {
                totalIncome = totalIncome + Float.parseFloat(tblIncome.getValueAt(i, 1).toString());
            } else {
                totalIncome = totalIncome + 0;
            }
        }
        txtTotalIncome.setText(Float.toString(totalIncome));
    }//GEN-LAST:event_btnTotalIncomeActionPerformed

    private void btnTotalExpencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalExpencesActionPerformed
        int rows = tblIncome.getRowCount();

        float totalExpences = 0;
        for (int i = 0; i < rows; i++) {

            if (tblIncome.getValueAt(i, 2) != null) {
                totalExpences = totalExpences + Float.parseFloat(tblIncome.getValueAt(i, 2).toString());
            } else {
                totalExpences = totalExpences + 0;
            }
        }
        DecimalFormat roundValue = new DecimalFormat("###.##");
        //float profit = Float.valueOf(roundValue.format(totalExpences));
        txtTotalExpences.setText(roundValue.format(totalExpences));
    }//GEN-LAST:event_btnTotalExpencesActionPerformed

    private void btnProfitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfitActionPerformed
        float income = Float.parseFloat(txtTotalIncome.getText());
        DecimalFormat roundValue = new DecimalFormat("###.##");
        float expence = Float.parseFloat(txtTotalExpences.getText());
        //roundValue.format returns a string.So it should be converted to float.
        float profit = Float.valueOf(roundValue.format(income - expence));
        txtProfit.setText(Float.toString(profit));
    }//GEN-LAST:event_btnProfitActionPerformed

    private void btnDeletePrdctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePrdctActionPerformed
        DefaultTableModel model = (DefaultTableModel) BillingTable.getModel();
        int selectedRow = BillingTable.getSelectedRow();
        if (selectedRow != -1) {
            int tot = (int) model.getValueAt(selectedRow, 3);
            int temp = Integer.parseInt(txtTotal.getText());
            txtTotal.setText(temp - tot + "");
            model.removeRow(selectedRow);
            rawNo--;
        }
        ItemSelecter.requestFocusInWindow();
    }//GEN-LAST:event_btnDeletePrdctActionPerformed

    private void btnSaveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveUserActionPerformed
        boolean result = true;
        for (int i = 0; i < tblUsers.getRowCount(); i++) {
            String desig = (String) tblUsers.getValueAt(i, 4);
            int id = (int) tblUsers.getValueAt(i, 0);
            result = dbOps.promoteUser(desig, id);
        }
        if (result == false) {
            JOptionPane.showMessageDialog(this, "Error occured while changing the designation");
        } else {
            JOptionPane.showMessageDialog(this, "User successfully promoted");
        }
    }//GEN-LAST:event_btnSaveUserActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        viewOrders view = new viewOrders();
        view.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @return the name1
     */
    public javax.swing.JLabel getName1() {
        return name1;
    }

    /**
     * @param name1 the name1 to set
     */
    public void setName1(javax.swing.JLabel name1) {
        this.name1 = name1;
    }

    public class ButtonImage extends JFrame {

        ButtonImage() {
            ImageIcon logout = new ImageIcon("C:\\MINE\\2nd year- 1st semester\\Group Project\\simz\\src\\simz\\logout_logo.png");
            btnLogOut = new JButton(logout);
        }
    }

    public static ImageIcon resizeImageIcon(ImageIcon imageIcon, Integer width, Integer height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(imageIcon.getImage(), 0, 0, width, height, null);
        graphics2D.dispose();

        return new ImageIcon(bufferedImage, imageIcon.getDescription());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManagerHomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerHomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerHomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerHomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagerHomeScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable BillingTable;
    private javax.swing.JComboBox ItemSelecter;
    public javax.swing.JComboBox Search;
    public javax.swing.JTextField amount;
    private javax.swing.JLabel billLabel;
    private javax.swing.JLabel billno;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddOrderToStock;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnBalance;
    private javax.swing.JButton btnDeletePrdct;
    private javax.swing.JButton btnGenerateReport;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnNewUser;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnProcessOrder;
    private javax.swing.JButton btnProfit;
    private javax.swing.JButton btnRefill;
    private javax.swing.JButton btnRemoveUser;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSaveChanges;
    private javax.swing.JButton btnSaveUser;
    private javax.swing.JButton btnSetStock;
    private javax.swing.JButton btnTotalExpences;
    private javax.swing.JButton btnTotalIncome;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox jcomboAddTodaysStock;
    public javax.swing.JLabel lablePic;
    private javax.swing.JLabel lblOrderStatus;
    private javax.swing.JLabel lblStockStatus;
    public javax.swing.JLabel name;
    public javax.swing.JLabel name1;
    public javax.swing.JTable tableProduct;
    public javax.swing.JTable tblIncome;
    public javax.swing.JTable tblOrder;
    public javax.swing.JTable tblUsers;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel total;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtBalance;
    public javax.swing.JTextField txtCash;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtProfit;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalExpences;
    private javax.swing.JTextField txtTotalIncome;
    public javax.swing.JPanel users;
    // End of variables declaration//GEN-END:variables
}
