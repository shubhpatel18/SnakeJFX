public record Coords(int x, int y) {
    public Coords getAbove() {
        return new Coords(this.x, this.y+1);
    }

    public Coords getBelow() {
        return new Coords(this.x, this.y-1);
    }

    public Coords getLeft() {
        return new Coords(this.x-1, this.y);
    }

    public Coords getRight() {
        return new Coords(this.x+1, this.y);
    }
}
