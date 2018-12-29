public interface GameMap {
    Pair<Long, Long> getDimensions();
    long getHeight();
    long getWidth();
    MapCell getCell(long x, long y);
    MapCell getCell(Pair<Long, Long> location);
}
