public class Hamiltonian {

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

    @Override
    public String toString() {
        return "Hamiltonian{" +
                "beginX=" + beginX +
                ", beginY=" + beginY +
                ", eindX=" + eindX +
                ", eindY=" + eindY +
                '}';
    }


}
