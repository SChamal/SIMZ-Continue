/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simz1;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import static simz1.LoginFrame1.mhp;
import static simz1.LoginFrame1.spi;

/**
 *
 * @author DELL
 */
public class ManagerHomeScreen extends javax.swing.JFrame {

    AutoSuggest as = new AutoSuggest();
    DBOperations dbOps = new DBOperations();
    Vector<String> v = new Stack<String>();
    Vector<String> v2 = new Stack<String>();
    private boolean hide_flag = false;
    JTextField tx, tx2;
    public int rawNo = 0;
    public static int orderRowNo;

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
                                    model.addRow(new Object[]{true, rst.getInt(1), rst.getString(3), rst.getString(5), rst.getString(6), 0, 0});
                                }
                                return;
                            } catch (SQLException ex) {
                                Logger.getLogger(ManagerHomeScreen.class.getName()).log(Level.SEVERE, null, ex);
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
        initComponents();
        this.btnReset.setVisible(false);
        this.btnSaveChanges.setVisible(false);
        autoSuggest();
        autoSuggest2();
        Search.setSelectedIndex(-1);

        jcomboAddTodaysStock.setSelectedIndex(-1);

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo1.jpg")));

        ResultSet rst = dbOps.viewUser();
        tblUsers.setModel(DbUtils.resultSetToTableModel(rst));
        //this.Search.requestFocusInWindow();
        /*this.jComboBoxSearch = new JComboBox(new Object[] { "Ester", "Jordi",
         "Jordina", "Jorge", "Sergi" });
         AutoCompleteDecorator.decorate(this.jComboBoxSearch);*/
        setMorningStock();
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
        btnCancel = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        users = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        btnNewUser = new javax.swing.JButton();
        btnRemoveUser = new javax.swing.JButton();
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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Product Code", "Name", "Price", "Expiry Date", "Quantity", "Received Qty."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, true
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 457, Short.MAX_VALUE)
                        .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 910, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jcomboAddTodaysStock, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btnAddOrderToStock, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnSaveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSetStock)
                        .addGap(26, 26, 26))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddOrderToStock)
                            .addComponent(btnSetStock)
                            .addComponent(btnSaveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcomboAddTodaysStock, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(60, Short.MAX_VALUE))
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
        jLabel7.setText("Cash");

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

        btnBalance.setText("Balance");
        btnBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBalanceActionPerformed(evt);
            }
        });

        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setText("Total");

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
        jLabel10.setText("Time:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Date:");

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
                .addGap(0, 0, Short.MAX_VALUE)
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
                        .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBalance)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
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
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
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
                                    .addComponent(txtTotalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(btnGenerateReport, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)))))
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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Order No", "Product ID", "Product Name", "Date", "Time", "Order Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, true
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

        btnCancel.setText("Cancel");

        jLabel6.setText("LABEL");

        jLabel8.setText("alert");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnProcessOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(74, 74, 74)
                                .addComponent(btnRefill)
                                .addGap(56, 56, 56)
                                .addComponent(btnCancel))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 852, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(50, 50, 50))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProcessOrder)
                            .addComponent(btnRefill)
                            .addComponent(btnCancel))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
            .addGap(0, 545, Short.MAX_VALUE)
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

        javax.swing.GroupLayout usersLayout = new javax.swing.GroupLayout(users);
        users.setLayout(usersLayout);
        usersLayout.setHorizontalGroup(
            usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemoveUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        usersLayout.setVerticalGroup(
            usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(usersLayout.createSequentialGroup()
                        .addComponent(btnNewUser, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btnRemoveUser, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(412, Short.MAX_VALUE))
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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
            //SimpleDateFormat javadate = new SimpleDateFormat("yyyy-MM-dd");
            //java.util.Date dte = null;
            String dateCrnt = today;
            String dte = "0000-00-00";
            //java.sql.Date sqldte = null;

            //sqldateCrnt = new java.sql.Date(dateCrnt.getDate());
            //dte = new Date(0);
            //sqldte = new java.sql.Date(dte.getDay());
            //String dte = "0000-00-00";
            try {
                //SimpleDateFormat javadate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                dte = (tableProduct.getModel().getValueAt(j, 4)).toString();
                if ("".equals(dte)) {
                    dte = "0000-00-00";
                }
                //sqldte = new java.sql.Date(dte.getDay());
            } catch (NullPointerException ex) {
                //System.out.println(ex);
            }

            int crnt = 0, totl = 0;
            try {
                crnt = Integer.parseInt(tableProduct.getModel().getValueAt(j, 5).toString());
                totl = Integer.parseInt(tableProduct.getModel().getValueAt(j, 6).toString());
                if (totl < 0 || crnt < 0) {
                    JOptionPane.showMessageDialog(this, "Please enter only positive numbers in quantity field!!!");
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
                        ResultSet rs = dbOps.combineTwoTables(id1);
                        while (rs.next()) {
                            String s1 = rs.getString(1);
                            int s2 = rs.getInt(2);
                            String s3 = rs.getString(3);
                            int s4 = rs.getInt(4);
                            int s5 = rs.getInt(5);
                            model.addRow(new Object[]{true, id1, s1, s2, s3, s4, s5});
                        }

                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        //this.btnSaveChanges.setVisible(true);
        //this.btnSetStock.setVisible(false);
        //this.btnReset.setVisible(false);
        as.autoSuggest(ItemSelecter);
        ItemSelecter.setSelectedIndex(-1);

        tableModelSalesperson tmSPmodel = new tableModelSalesperson();
        spi.SalesPStock.setModel(tmSPmodel);
        for (int k = 0; k < model.getRowCount(); k++) {
            int Id = Integer.parseInt(tableProduct.getModel().getValueAt(k, 1).toString());
            ResultSet rs = dbOps.combineTwoTablesForSP(Id);
            try {
                while (rs.next()) {
                    if (rs.isFirst()) {
                        tmSPmodel.addRow(new Object[]{Id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        JOptionPane.showMessageDialog(this, "Todays Stock has been created successfully");

    }//GEN-LAST:event_btnSetStockActionPerformed

    private void SearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchKeyPressed

    }//GEN-LAST:event_SearchKeyPressed

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchActionPerformed
    //Set default morning stock to the tableProduct table
    private void setMorningStock() {
        ResultSet rst = dbOps.combineMorningStockAndStock();
        MyTableModel model = new MyTableModel();
        tableProduct.setModel(model);
        try {
            while (rst.next()) {
                model.addRow(new Object[]{true, rst.getString(1), rst.getString(2), rst.getString(3), rst.getDate(4), rst.getString(5), rst.getString(6)});
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
                    dbOps.addTransaction(timeLabel.getText(), today);
                    int billNo = dbOps.getBillID(timeLabel.getText(), today);

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
                                int id2 = 0;
                                try {
                                    //id2 = Integer.parseInt((String) model2.getValueAt(k, 2));
                                    id2 = (int) model2.getValueAt(k, 2);
                                } catch (NullPointerException ex) {
                                    //flag = true;
                                    //break;
                                }/*catch(ClassCastException s){
                                 flag = false;
                                 id2 = (int) model2.getValueAt(k, 2);
                                 }*/

                                if (id == id2) {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag == true) {
                                NotificationPopup nw2 = new NotificationPopup();
                                nw2.main1("Quantity limit reached for " + prdctName);
                                //model2.addRow(new Object[]{false, 01, id, prdctName, today, timeLabel.getText(), 0, 0});
                                model2.insertRow(orderRowNo, new Object[]{false, 01, id, prdctName, today, timeLabel.getText(), 0, 0});
                                orderRowNo++;
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
                } else if (result == JOptionPane.NO_OPTION) {
                    txtCash.setText("");
                    txtBalance.setText("");
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
                    quantity = Integer.parseInt(amount.getText().toString());
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
                    ItemSelecter.requestFocusInWindow();
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
                dbOps.addTransaction(timeLabel.getText(), today);
                int billNo = dbOps.getBillID(timeLabel.getText(), today);

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
                            int id2 = 0;
                            try {
                                id2 = (int) model2.getValueAt(k, 2);
                            } catch (NullPointerException ex) {
                               
                            }

                            if (id == id2) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag == true) {
                            NotificationPopup nw2 = new NotificationPopup();
                            nw2.main1("Quantity limit reached for " + prdctName);
                            //model2.addRow(new Object[]{false, 01, id, prdctName, today, timeLabel.getText(), 0, 0});
                            model2.insertRow(orderRowNo, new Object[]{false, 01, id, prdctName, today, timeLabel.getText(), 0, 0});
                            orderRowNo++;
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
            } else if (result == JOptionPane.NO_OPTION) {
                txtCash.setText("");
                txtBalance.setText("");
            }

        }
    }//GEN-LAST:event_btnBalanceActionPerformed

    private void btnSaveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveChangesActionPerformed
        DefaultTableModel model = (DefaultTableModel) this.tableProduct.getModel();
        for (int j = 0; j < model.getRowCount(); j++) {
            int id = Integer.parseInt(tableProduct.getModel().getValueAt(j, 1).toString());
            int lmt = 0;
            //SimpleDateFormat javadate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dte = (tableProduct.getModel().getValueAt(j, 4)).toString();
            int crnt = Integer.parseInt(tableProduct.getModel().getValueAt(j, 5).toString());
            int totl = Integer.parseInt(tableProduct.getModel().getValueAt(j, 6).toString());

            try {
                if (dbOps.getTodayStockQty(id).getInt(2) != totl) {
                    try {
                        crnt = crnt + totl;
                        totl = totl + dbOps.getTodayStockQty(id).getInt(2);
                        /*if ("".equals(dte)) {
                         dte = dbOps.getTodayStockQty(id).getString(2);
                         }*/

                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                    boolean c = dbOps.updateTodayStockQty2(id, lmt, totl, crnt, dte);
                    model.setValueAt(crnt, j, 5);
                    model.setValueAt(dte, j, 4);
                    model.setValueAt(totl, j, 6);
                    if (c == false) {
                        JOptionPane.showMessageDialog(this, "Error occured while updating a product in current Today Stock");
                        return;
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

        if (tableProduct.isColumnSelected(6)) {
            for (int i = 0; i < tableProduct.getRowCount(); i++) {
                //tableProduct.setValueAt("mika", i, 5);
            }
        }
    }//GEN-LAST:event_btnSaveChangesActionPerformed


    private void ItemSelecterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ItemSelecterFocusLost

        // TODO add your handling code here:
    }//GEN-LAST:event_ItemSelecterFocusLost

    private void btnProcessOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessOrderActionPerformed
        OrderConfirmation oc = new OrderConfirmation();
        oc.setVisible(true);
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
                ResultSet rst = dbOps.getTodayStockQty(id);
                try {
                    current = rst.getInt(1);
                    regular = rst.getInt(4);
                } catch (SQLException ex) {

                }

                model2.setValueAt((regular - current), i, 6);
            }

        }
    }//GEN-LAST:event_btnRefillActionPerformed
    IncomeTableModel incomeModel = new IncomeTableModel();
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        
        String description =  txtDescription.getText();
        int amount = Integer.parseInt(txtAmount.getText());
        
        tblIncome.setModel(incomeModel);       
        incomeModel.addRow(new Object[]{ description, null, amount});  
        
        txtDescription.setText("");
        txtAmount.setText("");
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String description =  txtDescription.getText();
            int amount = Integer.parseInt(txtAmount.getText());

            tblIncome.setModel(incomeModel);       
            incomeModel.addRow(new Object[]{ description, null, amount});  

            txtDescription.setText("");
            txtAmount.setText("");
            
            txtDescription.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtAmountKeyPressed

    private void txtDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescriptionKeyPressed
        int code = evt.getKeyCode();
        if (code== KeyEvent.VK_ENTER) {
            txtAmount.requestFocusInWindow();
        }
        if (code== KeyEvent.VK_TAB) {
            txtAmount.requestFocusInWindow();
        }
    }//GEN-LAST:event_txtDescriptionKeyPressed

    private void btnTotalIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalIncomeActionPerformed
        int rows = tblIncome.getRowCount();
        int totalIncome=0;
        for(int i=0;i<rows;i++){
            
            if(tblIncome.getValueAt(i, 1)!=null){
                totalIncome=totalIncome+Integer.parseInt((String) tblIncome.getValueAt(i, 1));
            }else{
                totalIncome=totalIncome+0;
            }
        }
        txtTotalIncome.setText(Integer.toString(totalIncome));
    }//GEN-LAST:event_btnTotalIncomeActionPerformed

    private void btnTotalExpencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalExpencesActionPerformed
        int rows = tblIncome.getRowCount();
        int totalExpences=0;
        for(int i=0;i<rows;i++){
            
            if(tblIncome.getValueAt(i, 2)!=null){                
                totalExpences=totalExpences+Integer.parseInt(tblIncome.getValueAt(i, 2).toString());
            }else{
                totalExpences=totalExpences+0;
            }
        }
        txtTotalExpences.setText(Integer.toString(totalExpences));
    }//GEN-LAST:event_btnTotalExpencesActionPerformed

    private void btnProfitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfitActionPerformed
        int income= Integer.parseInt(txtTotalIncome.getText());
        int expence= Integer.parseInt(txtTotalExpences.getText());
        int profit= income-expence;
        txtProfit.setText(Integer.toString(profit));
    }//GEN-LAST:event_btnProfitActionPerformed

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
    private javax.swing.JButton btnCancel;
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
    private javax.swing.JButton btnSetStock;
    private javax.swing.JButton btnTotalExpences;
    private javax.swing.JButton btnTotalIncome;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    public javax.swing.JLabel name;
    public javax.swing.JLabel name1;
    public javax.swing.JTable tableProduct;
    private javax.swing.JTable tblIncome;
    public javax.swing.JTable tblOrder;
    public javax.swing.JTable tblUsers;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel total;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtCash;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtProfit;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalExpences;
    private javax.swing.JTextField txtTotalIncome;
    public javax.swing.JPanel users;
    // End of variables declaration//GEN-END:variables
}
