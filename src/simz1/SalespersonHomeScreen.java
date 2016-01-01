/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simz1;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import static simz1.LoginFrame1.mhp;
import static simz1.LoginFrame1.spi;
import static simz1.ManagerHomeScreen.model1;
import static simz1.ManagerHomeScreen.orderRowNo;
import static simz1.ManagerHomeScreen.resizeImageIcon;

/**
 *
 * @author DELL
 */
public class SalespersonHomeScreen extends javax.swing.JFrame {

    public static MyTableModel model;
    DBOperations dbOps = new DBOperations();
    AutoSuggest as = new AutoSuggest();
    Vector<String> v = new Stack<String>();
    JTextField tx;
    private boolean hide_flag = false;
    int rawNo = 0;

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
        jComboBoxSearch.removeAllItems();
        try {
            ResultSet rst = dbOps.getProducts();
            rst.first();
            if (jComboBoxSearch.getItemCount() == 0) {
                do {
                    jComboBoxSearch.addItem(rst.getString(1));
                    v.addElement(rst.getString(1));
                    jComboBoxSearch.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent ie) {
                            if (ie.getStateChange() == ItemEvent.SELECTED) {
                                jComboBoxSearch.getSelectedIndex();

                            }
                        }
                    });
                } while (rst.next());
            } else {
                jComboBoxSearch.addItem("");
            }
        } catch (SQLException e) {
        }

        //jComboBoxSearch.setEditable(true);
        tx = (JTextField) jComboBoxSearch.getEditor().getEditorComponent();
        tx.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        String text = tx.getText();
                        if (text.length() == 0) {
                            jComboBoxSearch.hidePopup();
                            setModel(new DefaultComboBoxModel(v), "");
                        } else {
                            DefaultComboBoxModel m = getSuggestedModel(v, text);
                            if (m.getSize() == 0) {
                                jComboBoxSearch.hidePopup();
                            } else {
                                setModel(m, text);
                                jComboBoxSearch.showPopup();
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
                            ViewProductForSP vpSP = new ViewProductForSP(str);
                            vpSP.setVisible(true);
                            vpSP.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            return;
                        } else if (str.equals(tx.getText())) {
                            ViewProductForSP vpSP = new ViewProductForSP(str);
                            vpSP.setVisible(true);
                            vpSP.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            return;
                        }
                    }
                }
            }

        });
    }

    private void setModel(DefaultComboBoxModel mdl, String str) {
        jComboBoxSearch.setModel(mdl);
        tx.setText(str);
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

    public SalespersonHomeScreen() {
        initComponents();
        this.autoSuggest();
        as.autoSuggest(ItemSelecter);
        ItemSelecter.setSelectedIndex(-1);
        jComboBoxSearch.setSelectedIndex(-1);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo1.jpg")));
        this.ItemSelecter.requestFocusInWindow();

        this.dateLabel.setText(today);
        this.clocker();
        int max = dbOps.getMaxBillID();
        this.billno.setText(max + 1 + "");
        try {
            setStock();
        } catch (Exception e) {

        }

        SalesPStock.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                int tmpEx = Integer.parseInt(table.getModel().getValueAt(row, 5).toString());
                if (tmpEx == 1) {
                    setBackground(Color.RED);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }

                return this;
            }

        });
    }

    DBOperations dbops = new DBOperations();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        name1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        name2 = new javax.swing.JLabel();
        btnLogOut1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        name3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lablePic = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        BillingTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        txtCash = new javax.swing.JTextField();
        ItemSelecter = new javax.swing.JComboBox();
        amount = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btnBalance = new javax.swing.JButton();
        txtBalance = new javax.swing.JTextField();
        total = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        billno = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        btnDeletePrdct = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        SalesPStock = new javax.swing.JTable();
        jComboBoxSearch = new javax.swing.JComboBox();

        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 36)); // NOI18N
        jLabel1.setText("SIMZ");

        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 14)); // NOI18N
        jButton1.setText("Edit My Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        name1.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 14)); // NOI18N
        name1.setText("Lalith");

        jLabel2.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 18)); // NOI18N
        jLabel2.setText("Logged in As: ");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(name)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(name1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(name)
                        .addGap(8, 8, 8)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(name1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLogOut)))))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 36)); // NOI18N
        jLabel4.setText("SIMZ");

        btnLogOut1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simz1/1439648143_logout.png"))); // NOI18N
        btnLogOut1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOut1ActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simz1/logo1.jpg"))); // NOI18N

        jButton2.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 14)); // NOI18N
        jButton2.setText("Edit My Profile");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        name3.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 14)); // NOI18N
        name3.setText("Jagath");

        jLabel6.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 18)); // NOI18N
        jLabel6.setText("Logged in As: ");

        lablePic.setMaximumSize(new java.awt.Dimension(60, 60));
        lablePic.setMinimumSize(new java.awt.Dimension(60, 60));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(12, 12, 12)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(name2)
                        .addGap(144, 144, 144))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(name3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(lablePic, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogOut1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(name2)
                        .addGap(8, 8, 8)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(name3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLogOut1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lablePic, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        BillingTable.setGridColor(new java.awt.Color(51, 51, 51));
        BillingTable.setRowHeight(20);
        jScrollPane4.setViewportView(BillingTable);

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
        ItemSelecter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ItemSelecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemSelecterActionPerformed(evt);
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

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText(" Quantity");

        btnBalance.setText("Balance (Rs.)");
        btnBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBalanceActionPerformed(evt);
            }
        });

        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setText("Total (Rs.)");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Bill No:");

        billno.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        billno.setText("Bill No");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Time:");

        timeLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        timeLabel.setText("Time");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Date:");

        dateLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        dateLabel.setText("Date");

        btnDeletePrdct.setText("Delete Product");
        btnDeletePrdct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePrdctActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ItemSelecter, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnOK)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(billno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeletePrdct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(txtCash)
                    .addComponent(txtBalance))
                .addGap(31, 31, 31))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ItemSelecter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billno, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDeletePrdct))))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBalance)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Transactions  ", jPanel3);

        jLabel8.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Search");

        SalesPStock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        SalesPStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Product Code", "Name", "Price", "Expiry Date", "Current Quantity", "If Expired"
            }
        ));
        SalesPStock.setGridColor(new java.awt.Color(51, 51, 51));
        SalesPStock.setRowHeight(20);
        jScrollPane3.setViewportView(SalesPStock);

        jComboBoxSearch.setEditable(true);
        jComboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE))
                .addGap(81, 81, 81))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Stock Details ", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        this.setVisible(false);
        LoginFrame1 lf = new LoginFrame1();
        lf.setVisible(true);
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        ManagerProfileFrame mpf = new ManagerProfileFrame();
        mpf.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnLogOut1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOut1ActionPerformed
        this.setVisible(false);
        LoginFrame1 lf = new LoginFrame1();
        lf.setSize(755, 610);
        lf.setVisible(true);
        lf.btnHint.setVisible(false);
        lf.btnHint.setVisible(false);
    }//GEN-LAST:event_btnLogOut1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //this.setVisible(false);
        SalesPersonProfileFrame spf = new SalesPersonProfileFrame();
        spf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        spf.name.setText(spi.name3.getText());
        spf.name2.setText(spi.name3.getText());

        String s1 = spf.name2.getText();
        String rst = dbops.getPropic(s1);
        String rst2 = dbops.getName(spf.name2.getText());
        String rst4 = dbops.getNic(spf.name2.getText());
        int rst3 = dbops.getID(spf.name2.getText());

        ImageIcon image1 = new ImageIcon(rst);
        ImageIcon image2 = resizeImageIcon(image1, 100, 100);
        spf.jLabel18.setIcon(image2);
        spf.txtName.setText(rst2);
        spf.empId.setText("PSB" + rst3);
        spf.nicLabel.setText(rst4);
        spf.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

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

                    //add data of the transaction to the income and expenditure table in database and the interface
                    mhp.tblIncome.setModel(mhp.incomeModel);
                    String descript = "bill " + Integer.toString(billNo);
                    int userID = dbOps.getID(name1.getText());
                    mhp.incomeModel.addRow(new Object[]{descript, amounti, null});
                    dbOps.addToIncomeAndExpenditure(userID, descript, amounti, 0);

                    DefaultTableModel model = (DefaultTableModel) spi.SalesPStock.getModel();
                    DefaultTableModel model2 = (DefaultTableModel) mhp.tblOrder.getModel();
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

                                int id2 = 0, id3 = 0;
                                try {
                                    id2 = (int) model2.getValueAt(k, 2);
                                    id3 = (int) model2.getValueAt(k, 7);
                                } catch (NullPointerException ex) {

                                }

                                if ((id == id2) && (model2.getValueAt(k, 7) == null)) {
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

    private void ItemSelecterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemSelecterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ItemSelecterActionPerformed

    private void amountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountActionPerformed

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
                            ItemSelecter.requestFocusInWindow();
                        }
                    });

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error occured while the transaction");
                }
            }
        }
    }//GEN-LAST:event_amountKeyPressed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        int quantity = 0;
        if (amount.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "First you should select an item");
        } else if (ItemSelecter.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Quantity field cannot be null");
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

                //add data of the transaction to the income and expenditure table in database and the interface
                mhp.tblIncome.setModel(mhp.incomeModel);
                String descript = "bill " + Integer.toString(billNo);
                int userID = dbOps.getID(name1.getText());
                mhp.incomeModel.addRow(new Object[]{descript, amounti, null});
                dbOps.addToIncomeAndExpenditure(userID, descript, amounti, 0);

                DefaultTableModel model = (DefaultTableModel) spi.SalesPStock.getModel();
                DefaultTableModel model2 = (DefaultTableModel) mhp.tblOrder.getModel();
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

                            int id2 = 0, id3 = 0;
                            try {
                                id2 = (int) model2.getValueAt(k, 2);
                                id3 = (int) model2.getValueAt(k, 7);
                            } catch (NullPointerException ex) {

                            }

                            if ((id == id2) && (model2.getValueAt(k, 7) == null)) {
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
                //b1.setSize(350, 500);
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

    private void ItemSelecterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ItemSelecterKeyPressed

    }//GEN-LAST:event_ItemSelecterKeyPressed

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

    private void setStock() {
        model1 = new MyTableModel();
        SalesPStock.setModel(model1);
        ResultSet rst = dbOps.getTodayStock();
        try {
            while (rst.next()) {
                model.addRow(new Object[]{rst.getString(1), rst.getString(2), rst.getString(3), rst.getDate(4), rst.getString(5), 0});
            }
        } catch (SQLException e) {

        }
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
            java.util.logging.Logger.getLogger(SalespersonHomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalespersonHomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalespersonHomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalespersonHomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalespersonHomeScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable BillingTable;
    private javax.swing.JComboBox ItemSelecter;
    public javax.swing.JTable SalesPStock;
    public javax.swing.JTextField amount;
    private javax.swing.JLabel billno;
    private javax.swing.JButton btnBalance;
    private javax.swing.JButton btnDeletePrdct;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnLogOut1;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBoxSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JLabel lablePic;
    public javax.swing.JLabel name;
    public javax.swing.JLabel name1;
    public javax.swing.JLabel name2;
    public javax.swing.JLabel name3;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel total;
    private javax.swing.JTextField txtBalance;
    public javax.swing.JTextField txtCash;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
