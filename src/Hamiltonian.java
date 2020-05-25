public class Hamiltonian {

    private double totaalAfstand;
    private int beginX;
    private int beginY;
    private int eindX;
    private int eindY;

    public Hamiltonian(int beginX, int beginY, int eindX, int eindY) {
        this.beginX = beginX;
        this.beginY = beginY;
        this.eindX = eindX;
        this.eindY = eindY;
    }

    public Hamiltonian(double totaalAfstand, int beginX, int beginY, int eindX, int eindY) {
        this.totaalAfstand = totaalAfstand;
        this.beginX = beginX;
        this.beginY = beginY;
        this.eindX = eindX;
        this.eindY = eindY;
    }

    public int getBeginX() {
        return beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public int getEindX() {
        return eindX;
    }

    public int getEindY() {
        return eindY;
    }

    public void setTotaalAfstand(double totaalAfstand) {
        this.totaalAfstand = totaalAfstand;
    }

    public double getTotaalAfstand() {
        return totaalAfstand;
    }

    @Override
    public String toString() {
        return "Hamiltonian{" +
                "totaalAfstand=" + totaalAfstand +
                ", beginX=" + beginX +
                ", beginY=" + beginY +
                ", eindX=" + eindX +
                ", eindY=" + eindY +
                '}';
    }
}
