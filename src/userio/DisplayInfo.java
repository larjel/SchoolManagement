package userio;

/**
 * Class for displaying formatted (fixed length) tables.
 *
 * @author Lars Jelleryd
 */
public class DisplayInfo {

    final String[] headers;

    /**
     * Constructs a new display info instance with given column headers. The
     * columns should have the desired length, i.e. they should be appended with
     * spaces until the desired length is achieved.
     *
     * @param headers Column headers (1-n) with desired length.
     */
    public DisplayInfo(String... headers) {
        this.headers = headers;
    }

    /**
     * Print columns of one row with fixed length.
     *
     * @param columns Columns to print
     */
    private void printFixedLength(String[] columns) {

        for (int i = 0; i < columns.length; i++) {
            if (columns[i].length() >= headers[i].length()) {
                // What to print is as long or longer than column length, cut it and append 
                // one space character before next column
                System.out.print(columns[i].substring(0, headers[i].length() - 1) + " ");
            } else {
                // What to print is shorter than column length, append spaces
                System.out.print(columns[i]);
                for (int j = 0; j < (headers[i].length() - columns[i].length()); j++) {
                    System.out.print(" ");
                }
            }
        }
        System.out.println();
    }

    /**
     * Print the column headers
     */
    public void printHeader() {
        printFixedLength(headers);
    }

    /**
     * Print given row. The number of columns must be the same as used in the
     * constructor.
     *
     * @param columns Columns to print.
     */
    public void printRow(String... columns) {
        if (columns.length != headers.length) {
            throw new IllegalArgumentException("Bad argument/element count, should be "
                    + headers.length + " but got " + columns.length);
        }
        printFixedLength(columns);
    }

    /**
     * Test and demonstration method.
     *
     * @param args Not used
     */
    public static void main(String[] args) {

        DisplayInfo di = new DisplayInfo(
                "ID  ", "NAME              ", "GENDER       ",
                "BIRTHDAY   ", "SALARY    ", "BONUS    ", "PROFESSION  ");

        di.printHeader();
        di.printRow("1", "Anders Andersson", "Man", "19660101", "37000", "42000", "Secretary");
        di.printRow("2", "Berit Sunesson", "Woman", "19831210", "43000", "18000", "Technician");
        di.printRow("3456", "Erik Superlångtefternamn", "Man", "19931011", "18500", "21000", "Programmer");
    }

}
