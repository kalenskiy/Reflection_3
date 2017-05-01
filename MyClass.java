package Task3;


import java.io.Serializable;

public class MyClass{
    @Save private int numb;
    @Save private String text;

    private char cymb;

    public void setCymb(char cymb) {
        this.cymb = cymb;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text + " " + numb + " " + cymb;
    }
}
