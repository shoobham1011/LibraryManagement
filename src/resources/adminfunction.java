import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminFunctions {
    static JTable bookData = new JTable();
    static JScrollPane scrollPane = new JScrollPane();

    public static void makeATableBoii(String[][] data, String[] cols, String title) {
        bookData = new JTable(data, cols);
        scrollPane = new JScrollPane(bookData);
        bookData.setFont(new Font("Arial", Font.BOLD, 16));
        bookData.setRowHeight(30);
        scrollPane.setBounds(0, 0, 800, 700);
        LibMain.newFrame(title, 800, 700);
        LibMain.frame.add(scrollPane);
    }

    public static void addBook() {

        JFrame addFrame;
        addFrame = LibMain.newJframeWindow("Add Book", 600, 400, JFrame.DISPOSE_ON_CLOSE);
        JLabel name, auth, gen, price;
        JTextField nameIN = null, authIN = null, genIN = null, priceIN = null;
        JButton addBOOK = new JButton("Add Book");
        addFrame.add(addBOOK);
        addBOOK.setBounds(20, 300, 120, 30);

        name = new JLabel("Enter Book's Name");
        auth = new JLabel("Enter Book's Author");
        gen = new JLabel("Enter Book's Genre");
        price = new JLabel("Enter Book's Price");

        JLabel[] lables = { name, auth, gen, price };
        String[] lableINPUT = new String[lables.length];
        JTextField[] inputs = { nameIN, authIN, genIN, priceIN };

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = new JTextField();

            addFrame.add(inputs[i]);
        }

        int yoff = 0;
        for (int i = 0; i < lables.length; i++) {

            lables[i].setBounds(20, 40 + yoff, 120, 20);
            addFrame.add(lables[i]);
            inputs[i].setBounds(150, 40 + yoff, 400, 40);
            inputs[i].setFont(new Font("Arial",Font.PLAIN,20));
            yoff += 60;
        }

        addBOOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                lableINPUT[0] = inputs[0].getText();
                lableINPUT[1] = inputs[1].getText();
                lableINPUT[2] = inputs[2].getText();
                lableINPUT[3] = inputs[3].getText();
                boolean allow = true;
                for (int i = 0; i < 4; i++) {
                    if (lableINPUT[i].isEmpty()) {
                        allow = false;
                        JOptionPane.showMessageDialog(null, "Please don't leave any field blank", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
                if (allow) {
                    Connection connection = SQLUtils.connect("root", "");

                    SQLUtils.insertToTable(connection,
                            "insert into bookData(name,author,genre,price) values('" + lableINPUT[0] + "','"
                                    + lableINPUT[1] + "','" + lableINPUT[2] + "','" + lableINPUT[3] + "')");
                    JOptionPane.showMessageDialog(null, "Book added to database", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    try {
                        connection.close();
                        System.out.println("Connection closed");
                    } catch (SQLException e1) {

                        e1.printStackTrace();
                    }
                }
            }
        });

    }
    public static void logout() {
        int n = JOptionPane.showConfirmDialog(null, "Do you want to logout");
        if (n == JOptionPane.YES_OPTION) {
            LibMain.frame.dispose();
            LibMain.frame.setVisible(false);
            LibMain.mainMenu();
        }

    }

    public static void showBooks() {

        Connection connection = SQLUtils.connect("root", "");
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet set = statement.executeQuery("select * from bookdata");
            ResultSetMetaData metaData = set.getMetaData();
            String[] cols = { metaData.getColumnName(1), metaData.getColumnName(2), metaData.getColumnName(3),
                    metaData.getColumnName(4), metaData.getColumnName(5), metaData.getColumnName(6) };

            set.last();
            int size = set.getRow();
            set.beforeFirst();

            String[][] data;
            data = new String[size][];
            for (int i = 0; i < size; i++) {
                data[i] = new String[6];
            }

            int i = 0;
            while (set.next()) {
                data[i][0] = String.valueOf(set.getInt("id"));
                data[i][1] = set.getString("Name");
                data[i][2] = set.getString("Author");
                data[i][3] = set.getString("Genre");
                data[i][4] = String.valueOf(set.getFloat("Price"));
                data[i][5] = String.valueOf(set.getInt("Issued"));
                i++;
            }
            connection.close();
            makeATableBoii(data, cols, "Book Shelf");

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public static void viewUsers() {

        Connection connection = SQLUtils.connect("root", "");
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet set = statement.executeQuery("select * from users");
            ResultSetMetaData metaData = set.getMetaData();
            String[] cols = { metaData.getColumnName(1), metaData.getColumnName(2) };
            set.last();
            int size = set.getRow();
            set.beforeFirst();

            String[][] data;
            data = new String[size][];
            for (int i = 0; i < size; i++) {
                data[i] = new String[2];
            }

            int i = 0;
            while (set.next()) {
                data[i][0] = String.valueOf(set.getInt("id"));
                data[i][1] = set.getString("username");
                i++;
            }
            connection.close();
            makeATableBoii(data, cols, "Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }