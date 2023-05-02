package model;

/**
 * Models for Outsourced part.
 *
 * @author Joshua Nantroup
 *
 */
public class Outsourced extends Part {

    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    // Getter for company name.
    public String getCompanyName() {
        return companyName;
    }

    // Setter for company name.
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
