package project3;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static project3.ScreenDisplay.CurrentParkStatus;

public class ListModel extends AbstractTableModel {


    private ArrayList<CampSite> listCampSites;
    private ArrayList<CampSite> fileredListCampSites;

    // enum
    private ScreenDisplay display;

    // column names
    private String[] columnNamesCurrentPark = {"Guest Name", "Est. Cost",
            "Check in Date", "EST. Check out Date ", "Max Power", "Num of Tenters"};

    // column names
    private String[] columnNamesforCheckouts = {"Guest Name", "Est. Cost",
            "Check in Date", "ACTUAL Check out Date ", " Real Cost"};

    private String[] columnNamesForOverdue = {"Guest Name", "Est. Cost", "EST. Check Out Date", "Days Overdue"};


    private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private String date;

    public ListModel() {
        super();

        // The display to be shown first
        display = CurrentParkStatus;

        // Initialize the listCampSites ArrayList
        listCampSites = new ArrayList<CampSite>();


        UpdateScreen();
        createList();
    }

    public void setDisplay(ScreenDisplay selected) {
        // Sets the enum display to the passed argument and updates the screen to reflect that.
        display = selected;
        UpdateScreen();
    }

    private void UpdateScreen() {

        // switch takes the value of the enum
        switch (display) {

            // First case, status of current campers in the park
            case CurrentParkStatus:

                // Filters out the campsites still currently occupied and adds them to the
                // ArrayList 'filteredListCampSites'
                fileredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
                        filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

                // Note: This uses Lambda function
                // Sorts the guests alphabetically
                Collections.sort(fileredListCampSites, (n1, n2) -> n1.getGuestName().compareTo(n2.guestName));
                break;

            // Second case, status of guests that have checked out of the park
            case CheckOutGuest:

                // Filters out all the campers/campsites that have checked out of the park
                fileredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
                        filter(n -> n.getActualCheckOut() != null).collect(Collectors.toList());

                // Note: This uses an anonymous class.
                Collections.sort(fileredListCampSites, new Comparator<CampSite>() {
                    @Override
                    public int compare(CampSite n1, CampSite n2) {
                        return n1.getGuestName().compareTo(n2.guestName);
                    }
                });
                break;
            case OverdueGuests:
                fileredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
                        filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

                Collections.sort(fileredListCampSites, Comparator.comparingInt(CampSite::getDaysOverdue));
                Collections.reverse(fileredListCampSites);
                break;
            default:
                throw new RuntimeException("upDate is in undefined state: " + display);
        }
        // Notifies all listeners that the table's structure has changed. The number of columns in the table, and
        // the names and types of the new columns may be different from the previous state. If the JTable receives
        // this event and its autoCreateColumnsFromModel flag is set it discards any table columns that it had and
        // reallocates default columns in the order they appear in the model. This is the same as calling
        // setModel(TableModel) on the JTable.
        fireTableStructureChanged();
        }

    @Override
    public String getColumnName(int col) {
        switch (display) {
            case CurrentParkStatus:
                return columnNamesCurrentPark[col];
            case CheckOutGuest:
                return columnNamesforCheckouts[col];
            case OverdueGuests:
                return columnNamesForOverdue[col];
        }
        throw new RuntimeException("Undefined state for Col Names: " + display);
    }

    @Override
    public int getColumnCount() {
        switch (display) {
            case CurrentParkStatus:
                return columnNamesCurrentPark.length;
            case CheckOutGuest:
                return columnNamesforCheckouts.length;
            case OverdueGuests:
                return columnNamesForOverdue.length;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int getRowCount() {
        return fileredListCampSites.size();     // returns number of items in the arraylist
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (display) {
            case CurrentParkStatus:
                return currentParkScreen(row, col);
            case CheckOutGuest:
                return checkOutScreen(row, col);
            case OverdueGuests:
                return overdueScreen(row, col);
          }
        throw new IllegalArgumentException();
    }

    private Object currentParkScreen(int row, int col) {
        switch (col) {
            case 0:
                return (fileredListCampSites.get(row).guestName);

            case 1:
                return (fileredListCampSites.get(row).getCost(fileredListCampSites.
                        get(row).estimatedCheckOut));

            case 2:
                return (formatter.format(fileredListCampSites.get(row).checkIn.getTime()));

            case 3:
                if (fileredListCampSites.get(row).estimatedCheckOut == null)
                    return "-";

                return (formatter.format(fileredListCampSites.get(row).estimatedCheckOut.
                                getTime()));

            case 4:
            case 5:
                if (fileredListCampSites.get(row) instanceof RV)
                    if (col == 4)
                        return (((RV) fileredListCampSites.get(row)).getPower());
                    else
                        return "";

                else {
                    if (col == 5)
                        return (((TentOnly) fileredListCampSites.get(row)).
                                getNumberOfTenters());
                    else
                        return "";
                }
            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    private Object checkOutScreen(int row, int col) {
        switch (col) {
            case 0:
                return (fileredListCampSites.get(row).guestName);

            case 1:
                return (fileredListCampSites.
                        get(row).getCost(fileredListCampSites.get(row).
                        estimatedCheckOut));
            case 2:
                return (formatter.format(fileredListCampSites.get(row).checkIn.
                                getTime()));

            case 3:
                return (formatter.format(fileredListCampSites.get(row).actualCheckOut.
                                getTime()));

            case 4:
                return (fileredListCampSites.
                        get(row).getCost(fileredListCampSites.get(row).
                        actualCheckOut
                ));

            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    private Object overdueScreen(int row, int col) {
        switch (col) {
            case 0:
                return (fileredListCampSites.get(row).guestName);

            case 1:
                return (fileredListCampSites.get(row).getCost(fileredListCampSites.get(row).estimatedCheckOut));

            case 2:
                if (fileredListCampSites.get(row).estimatedCheckOut == null)
                    return "-";

                return (formatter.format(fileredListCampSites.get(row).estimatedCheckOut.
                        getTime()));

            case 3:
                return (fileredListCampSites.get(row).daysOverdue);

            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    public void add(CampSite a) {
        listCampSites.add(a);
        UpdateScreen();
    }

    public CampSite get(int i) {
        return fileredListCampSites.get(i);
    }

    public void upDate(int index, CampSite unit) {
        UpdateScreen();
    }

    public void saveDatabase(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(listCampSites);
            os.close();
        } catch (IOException ex) {
            throw new RuntimeException("Saving problem! " + display);
        }
    }

    public void saveTextDatabase(String filename) {

        // Code for saving the JTable to a .txt file sourced from
        // https://stackoverflow.com/questions/43151376/exporting-jtable-to-txt-file

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
            PrintWriter fileWriter = new PrintWriter(bufferedWriter);

            // Loop through each row of our JTable
            for (int i = 0; i < getRowCount(); ++i) {

                // If the object on this row is of type RV, print "RV" at the start of the line on the file
                // in order to distinguish what type of object we need to create when the file is loaded. If it
                // is not an RV, prints "Tent" instead so an object of type Tent can be created instead.
                if (fileredListCampSites.get(i) instanceof RV) {
                    fileWriter.print("RV");
                } else {
                    fileWriter.print("Tent");
                }
                // Tabs to separate items for increased readability in the file.
                fileWriter.print("\t\t");

                // Loop through each cell in the current row.
                for (int j = 0; j < getColumnCount(); ++j) {
                    // Gets the value of the current cell and stores it in a string.
                    String s = getValueAt(i,j).toString();
                    // Print the value to the text file
                    fileWriter.print(s);
                    // Tabs to separate this from the next item
                    fileWriter.print("\t\t");
                }
                // After this row has been written to the file, go to the next line.
                fileWriter.println("");
            }

            // Closes the fileWriter
            fileWriter.close();

        }catch(Exception ex) {
            throw new RuntimeException("Saving problem! " + display);
        }
    }

    public void loadTextDatabase(String filename) {
        // new DateFormat
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        // Clears the currently displayed camp sites.
        listCampSites.clear();

        // Local variables for each column to be added back into the JTable after being read from file.
        String guestName;
        String cost;
        GregorianCalendar checkIn;
        GregorianCalendar estCheckOut;
        int maxPowerTenters;

        try {
            // Scanner to read our text file
            Scanner scnr = new Scanner(new File(filename));

            // As long as there are new lines, continue reading the file
            while (scnr.hasNextLine()) {

                // New Scanner to read the each item in the current line
                Scanner scnr2 = new Scanner(scnr.nextLine());

                // Reads each word on the line and assigns them to corresponding value of the object.
                String campType = scnr2.next();
                guestName = scnr2.next();
                cost = scnr2.next();
                checkIn = new GregorianCalendar();
                checkIn.setTime(df.parse(scnr2.next()));
                estCheckOut = new GregorianCalendar();
                estCheckOut.setTime(df.parse(scnr2.next()));
                maxPowerTenters = Integer.parseInt(scnr2.next());

                // If the first word of the line was RV, we know the entry should be of type RV, otherwise it should
                // be a tent and adds a new object to the list of campsites accordingly.
                if (campType.equals("RV")) {
                    listCampSites.add(new RV(guestName, checkIn, estCheckOut, null, maxPowerTenters));
                } else {
                    listCampSites.add(new TentOnly(guestName, checkIn, estCheckOut, null, maxPowerTenters));
                }
            }

            UpdateScreen();


        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            throw new RuntimeException("Loading problem: " + display);
        }
    }

    public void loadDatabase(String filename) {
        listCampSites.clear();

        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            listCampSites = (ArrayList<CampSite>) is.readObject();
            UpdateScreen();
            is.close();
        } catch (Exception ex) {
            throw new RuntimeException("Loading problem: " + display);
        }
    }

    public void createList() {
        // new DateFormat
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        // Creates six GregorianCalenders
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();
        GregorianCalendar g6 = new GregorianCalendar();

        try {

            // Creates a Date parsed from the given string
            // And sets the GregorianCalander object to that date
            Date d1 = df.parse("1/20/2020");
            g1.setTime(d1);
            Date d2 = df.parse("12/22/2020");
            g2.setTime(d2);
            Date d3 = df.parse("12/20/2019");
            g3.setTime(d3);
            Date d4 = df.parse("3/25/2020");
            g4.setTime(d4);
            Date d5 = df.parse("1/20/2010");
            g5.setTime(d5);
            Date d6 = df.parse("3/29/2020");
            g6.setTime(d6);

            // Create Tent sites with using the newly generated dates for the check in/check out times
            TentOnly tentOnly1 = new TentOnly("Amanda", g3, g2,null,4);
            TentOnly tentOnly11 = new TentOnly("Adam", g3,g1, null, 8);
            TentOnly tentOnly111 = new TentOnly("Bob", g5,g3, null, 8);
            TentOnly tentOnly1111 = new TentOnly("Amanda", g3, g3,null,77);
            TentOnly tentOnly2 = new TentOnly("Vern", g5, g3,null, 1);
            TentOnly tentOnly3 = new TentOnly("B3", g3, g1, g1,7);
            TentOnly tentOnly4 = new TentOnly("Vern", g5, g2,null, 5);
            TentOnly tentOnly5 = new TentOnly("Vern", g5, g6,null, 4);
            TentOnly tentOnly6 = new TentOnly("Vern", g5, g1,null, 2);
            TentOnly tentOnly7 = new TentOnly("Vern", g5, g4,null, 3);

            // Create RV sites with using the newly generated dates for the check in/check out times
            RV RV1 = new RV("Zach",g4,g6,null, 1000);
            RV RV2 = new RV("Tim",g5,g3,null, 1000);
            RV RV22 = new RV("Sharon", g3,g1,null, 2000);
            RV RV222 = new RV("Becky", g3,g6,null, 2000);
            RV RV3 = new RV("Amanda",g5,g4,g3, 1000);

            add(tentOnly1);
            add(tentOnly2);
            add(tentOnly3);
            add(tentOnly11);
            add(tentOnly111);
            add(tentOnly1111);
            add(tentOnly4);
            add(tentOnly5);
            add(tentOnly6);
            add(tentOnly7);

            add(RV1);
            add(RV2);
            add(RV3);
            add(RV22);
            add(RV222);

        } catch (ParseException e) {
            throw new RuntimeException("Error in testing, creation of list");
        }
    }

    public void setAllOverdueDays(GregorianCalendar date) {

        for (int i = 0; i < getRowCount(); i++) {
            fileredListCampSites.get(i).setDaysOverdue(date);
        }
    }

    public void sortRVTent() {

        Collections.sort(fileredListCampSites, Comparator.comparing(e -> e instanceof TentOnly));

        //fileredListCampSites = (ArrayList<CampSite>) fileredListCampSites.stream().sorted((n1, n2) -> n1.getGuestName().compareTo(n2.guestName)).collect(Collectors.toList());
    }

    public void sortTentRV() {

        GregorianCalendar now = new GregorianCalendar();

        Collections.sort(fileredListCampSites, new Comparator<CampSite>() {
            @Override
            public int compare(CampSite n1, CampSite n2) {
                if (n1.getGuestName().equals(n2.guestName)) {
                    return n1.getEstimatedCheckOut().compareTo(n2.estimatedCheckOut);
                } else {
                    return n1.getGuestName().compareTo(n2.guestName);
                }
            }
        });
        Collections.sort(fileredListCampSites, Comparator.comparing(e -> e instanceof RV));

    }
}

