import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class AddressBookUI {
    // components
    JFrame window;
    JPanel form, rowOne, rowTwo, rowThree, rowFour, rowFive;
    JLabel name, street, city, state, zip, errorMsg;
    JTextField _name, _street, _city, _state, _zip;
    JButton add, first, next, previous, last, update;
    int placeHolder, totalBytes, addressSize = 91;

    // methods
    public void createForm() {
        // initialize frame
        window = new JFrame("Address Book");
        window.setSize(500,200);
        // initialize panels
        form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.PAGE_AXIS));
        rowOne = new JPanel(new FlowLayout());
        rowTwo = new JPanel(new FlowLayout());
        rowThree = new JPanel(new FlowLayout());
        rowFour = new JPanel(new FlowLayout());
        rowFive = new JPanel(new FlowLayout());
        rowFive.setBackground(Color.darkGray);
        setRowOne();
        setRowTwo();
        setRowThree();
        setRowFour();
        setRowFive();
        // add rows to 'form' panel
        form.add(rowOne);
        form.add(rowTwo);
        form.add(rowThree);
        form.add(rowFour);
        form.add(rowFive);
        form.setOpaque(true);
        // finalize frame
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(form);
        window.pack();
        window.setVisible(true);
        initializeButtons();
        initializeFile();
    }

    /////////////////////////////////
    // BUILD EACH ROW OF FORM
    /////////////////////////////////

    public void setRowOne() {
        // populate first row
        name = new JLabel("Name");
        _name = new JTextField(35);
        name.setLabelFor(_name);
        // add to panel
        rowOne.add(name);
        rowOne.add(_name);
        rowOne.setOpaque(true);
    }

    public void setRowTwo() {
        // populate second row
        street = new JLabel("Street");
        _street = new JTextField(35);
        street.setLabelFor(_street);
        // add to panel
        rowTwo.add(street);
        rowTwo.add(_street);
        rowTwo.setOpaque(true);
    }

    public void setRowThree() {
        // populate third row
        city = new JLabel("City  ");
        _city = new JTextField(15);
        city.setLabelFor(_city);
        state = new JLabel("State");
        _state = new JTextField(5);
        state.setLabelFor(_state);
        zip = new JLabel("Zip");
        _zip = new JTextField(7);
        // add to panel
        rowThree.add(city);
        rowThree.add(_city);
        rowThree.add(state);
        rowThree.add(_state);
        zip.setLabelFor(_zip);
        rowThree.add(zip);
        rowThree.add(_zip);
        rowThree.setOpaque(true);
    }

    public void setRowFour() {
        // populate fourth row
        add = new JButton("Add");
        add.setMargin(new Insets(0,-5,0,-5));
        first = new JButton("First");
        first.setMargin(new Insets(0,-5,0,-5));
        next = new JButton("Next");
        next.setMargin(new Insets(0,-5,0,-5));
        previous = new JButton("Previous");
        previous.setMargin(new Insets(0,-5,0,-5));
        last = new JButton("Last");
        last.setMargin(new Insets(0,-5,0,-5));
        update = new JButton("Update");
        update.setMargin(new Insets(0,-5,0,-5));
        // add to panel
        rowFour.add(add);
        rowFour.add(first);
        rowFour.add(next);
        rowFour.add(previous);
        rowFour.add(last);
        rowFour.add(update);
        rowFour.setOpaque(true);
    }

    public void setRowFive() {
        // populate fifth row
        errorMsg = new JLabel(" ");
        errorMsg.setForeground(Color.white);
        rowFive.add(errorMsg);
    }

    /////////////////////////////////
    // FORM FUNCTIONS
    /////////////////////////////////

    public void clearForm() {
        // reset all text-fields
        _name.setText(null);
        _street.setText(null);
        _city.setText(null);
        _state.setText(null);
        _zip.setText(null);
    }

    public void clearErrorMsg() {
        errorMsg.setText(" ");
    }

    public void initializeFile() {
        // access file and read key data
        try(RandomAccessFile file = new RandomAccessFile("AddressBook.dat", "rw")) {
            placeHolder = (int)file.getFilePointer();
            totalBytes = (int)file.length();
            if(file.length() == 0) {
                first.setEnabled(false);
                last.setEnabled(false);
            }
            next.setEnabled(false);
            previous.setEnabled(false);
            update.setEnabled(false);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            errorMsg.setText("Error: File Not Found");
        } catch(IOException e) {
            e.printStackTrace();
            errorMsg.setText("Error: Input/Output");
        } catch(NullPointerException e) {
            e.printStackTrace();
            errorMsg.setText("Error: Data Not Found");
        }
    }

    // program button reactions
    public void disableButton(String name) {
        switch(name) {
            case "first":
                first.setEnabled(false);
                previous.setEnabled(false);
                // is there more than one address?
                if(totalBytes > 91) {
                    next.setEnabled(true);
                    last.setEnabled(true);
                } else {
                    next.setEnabled(false);
                    last.setEnabled(false);
                }
                break;
            case "next":
                first.setEnabled(true);
                previous.setEnabled(true);
                // is this the last address?
                if(placeHolder == totalBytes - addressSize) {
                    last.setEnabled(false);
                    next.setEnabled(false);
                }
                break;
            case "previous":
                next.setEnabled(true);
                last.setEnabled(true);
                // is this the first address?
                if(placeHolder == 0) {
                    first.setEnabled(false);
                    previous.setEnabled(false);
                }
                break;
            case "last":
                first.setEnabled(true);
                next.setEnabled(false);
                previous.setEnabled(true);
                last.setEnabled(false);
                break;
            case "reset":
                first.setEnabled(true);
                next.setEnabled(true);
                previous.setEnabled(true);
                last.setEnabled(true);
                break;
        }
        update.setEnabled(true);
    }

    public Boolean validateInput() {
        Boolean isValid = true;
        // name
        char[] n = _name.getText().toCharArray();
        if(n.length == 0) {
            isValid = false;
            errorMsg.setText("Error: Please fill in a name");
        } else if(n.length > 32) {
            isValid = false;
            errorMsg.setText("Error: 32 byte limit for name");
        }
        if(isValid) {
            for (int i = 0; i < n.length; i++) {
                if(!Character.isLetter(n[i])) {
                    isValid = (n[i] == ' ' || n[i] == '\0');
                    if(!isValid){
                        isValid = false;
                        errorMsg.setText("Error: Name can only be characters");
                        break;
                    }
                }
            }
        }
        // street
        if(isValid) {
            char[] s = _street.getText().toCharArray();
            if(s.length == 0) {
                isValid = false;
                errorMsg.setText("Error: Please fill in a street");
            } else if(s.length > 32) {
                isValid = false;
                errorMsg.setText("Error: 32 byte limit for street");
            }
            for(int i = 0; i < s.length; i++) {
                isValid = (s[i] == ' ' || s[i] == '.' || s[i] == '\0');
                if(!isValid) {
                    if(!Character.isLetter(s[i]) && !Character.isDigit(s[i])) {
                        isValid = false;
                        errorMsg.setText("Error: Street can only be characters and numbers");
                        break;
                    }
                }
            }
        }
        // city
        if(isValid) {
            char[] c = _city.getText().toCharArray();
            if(c.length == 0) {
                isValid = false;
                errorMsg.setText("Error: Please fill in a city");
            } else if(c.length > 20) {
                isValid = false;
                errorMsg.setText("Error: 20 byte limit for city");
            }
            for(int i = 0; i < c.length; i++) {
                if (!Character.isLetter(c[i])) {
                    isValid =  (c[i] == '\0' || c[i] == ' ');
                    if(!isValid) {
                        isValid = false;
                        errorMsg.setText("Error: City can only be characters");
                        break;
                    }
                }
            }
        }
        // state
        if(isValid) {
            char[] st = _state.getText().toCharArray();
            if(st.length == 0) {
                isValid = false;
                errorMsg.setText("Error: Please fill in a state");
            }else if(st.length != 2) {
                isValid = false;
                errorMsg.setText("Error: State needs to be 2 characters");
            }
            for(int i = 0; i < st.length; i++) {
                if (!Character.isLetter(st[i]) || st[i] == ' ') {
                    isValid = false;
                    errorMsg.setText("Error: State can only be characters");
                    break;
                }
            }
        }
        // zip code
        if(isValid) {
            char[] z = _zip.getText().toCharArray();
            if(z.length == 0) {
                isValid = false;
                errorMsg.setText("Error: Input a zip code");
            }else if(z.length != 5) {
                isValid = false;
                errorMsg.setText("Error: Zip code needs to be 5 digits");
            }
            for(int i = 0; i < z.length; i++) {
                if (!Character.isDigit(z[i])) {
                    isValid = false;
                    errorMsg.setText("Error: Zip code can only be numbers");
                    break;
                }
            }
        }
        return isValid;
    }

    public void initializeButtons() {
        // add new address
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearErrorMsg();
                if(validateInput()) {
                    modifyEngine("add");
                    disableButton("reset");
                    initializeFile();
                }
            }
        });
        // show first address
        first.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearErrorMsg();
                displayEngine("first");
                disableButton("first");
            }
        });
        // show next address
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearErrorMsg();
                displayEngine("next");
                disableButton("next");
            }
        });
        // show previous address
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearErrorMsg();
                displayEngine("previous");
                disableButton("previous");
            }
        });
        // show last address
        last.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearErrorMsg();
                displayEngine("last");
                disableButton("last");
            }
        });
        // update address data
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearErrorMsg();
                if(validateInput()) {
                    modifyEngine("update");
                }
            }
        });
    }

    public void displayEngine(String name) {
        try(RandomAccessFile file = new RandomAccessFile("AddressBook.dat", "rw")) {
            switch(name) {
                case "first":
                    placeHolder = 0;
                    break;
                case "next":
                    placeHolder += addressSize;
                    break;
                case "previous":
                    placeHolder -= addressSize;
                    break;
                case "last":
                    placeHolder = totalBytes - addressSize;
                    break;
            }
            file.seek(placeHolder);
            // name
            String __name = "";
            for(int i = 0; i < 32; i++) {
                __name += (char)file.readByte();
            }
            _name.setText(__name);
            // street
            String __street = "";
            for(int i = 0; i < 32; i++) {
                __street += (char)file.readByte();
            }
            _street.setText(__street);
            // city
            String __city = "";
            for(int i = 0; i < 20; i++) {
                __city += (char)file.readByte();
            }
            _city.setText(__city);
            // state
            String __state = "";
            for(int i = 0; i < 2; i++) {
                __state += (char)file.readByte();
            }
            _state.setText(__state);
            // zip code
            String __zip = "";
            for(int i = 0; i < 5; i++) {
                __zip += (char)file.readByte();
            }
            _zip.setText(__zip);
        }catch(FileNotFoundException e) {
            e.printStackTrace();
            errorMsg.setText("Error: File Not Found");
        } catch(IOException e) {
            e.printStackTrace();
            errorMsg.setText("Error: Input/Output");
        } catch(NullPointerException e) {
            e.printStackTrace();
            errorMsg.setText("Error: Data Not Found");
        }
    }

    public void modifyEngine(String name) {
        try(RandomAccessFile file = new RandomAccessFile("AddressBook.dat", "rw")) {
            switch(name) {
                case "add":
                    file.seek(file.length());
                    errorMsg.setText("Address Saved");
                    break;
                case "update":
                    file.seek(placeHolder);
                    errorMsg.setText("Address Updated");
                    break;
            }
            // name
            byte[] nArray = new byte[32];
            byte[] n = _name.getText().getBytes(StandardCharsets.UTF_8);
            for(int i = 0; i < n.length; i++) {
                nArray[i] = n[i];
            }
            file.write(nArray);
            // street
            byte[] sArray = new byte[32];
            byte[] s = _street.getText().getBytes(StandardCharsets.UTF_8);
            for(int i = 0; i < s.length; i++) {
                sArray[i] = s[i];
            }
            file.write(sArray);
            // city
            byte[] cArray = new byte[20];
            byte[] c = _city.getText().getBytes(StandardCharsets.UTF_8);
            for(int i = 0; i < c.length; i++) {
                cArray[i] = c[i];
            }
            file.write(cArray);
            // state
            byte[] stArray = new byte[2];
            byte[] st = _state.getText().getBytes(StandardCharsets.UTF_8);
            for(int i = 0; i < st.length; i++) {
                stArray[i] = st[i];
            }
            file.write(stArray);
            // zip code
            byte[] zArray = new byte[5];
            byte[] z = _zip.getText().getBytes(StandardCharsets.UTF_8);
            for(int i = 0; i < z.length; i++) {
                zArray[i] = z[i];
            }
            file.write(zArray);
            if(name.equals("add")) {
                clearForm();
            }
        }catch(FileNotFoundException e) {
            e.printStackTrace();
            errorMsg.setText("Error: File Not Found");
        } catch(IOException e) {
            e.printStackTrace();
            errorMsg.setText("Error: Input/Output");
        } catch(NullPointerException e) {
            e.printStackTrace();
            errorMsg.setText("Error: Data Not Found");
        }
    }

} // class end
