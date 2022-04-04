import java.util.*;

public class Graphics implements GraphicsInterface {
    private CanvasInterface canvas = null;
    private final Set<Position> painted = new HashSet<>();
    private final List<Position> toPaint = new ArrayList<>();

    public static class Position2D implements Position {
        int row;
        int col;

        public Position2D(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int getRow() {
            return row;
        }

        @Override
        public int getCol() {
            return col;
        }
    }

    private boolean notContains(Collection<Position> collection, Position position) {
        for(Position pos : collection)
            if(position.getRow() == pos.getRow() && position.getCol() == pos.getCol())
                return false;
        return true;
    }

    public void setCanvas(CanvasInterface canvas) {
        this.canvas = canvas;
    }

    public void fillWithColor(Position startingPosition, Color color) throws WrongStartingPosition, NoCanvasException {
        if(canvas == null)
            throw new NoCanvasException();
        try {
            canvas.setColor(startingPosition, color);
        }
        catch(CanvasInterface.CanvasBorderException exception) {
            throw new WrongStartingPosition();
        }
        catch(CanvasInterface.BorderColorException exception) {
            try {
                canvas.setColor(startingPosition, exception.previousColor);
            }
            catch(Exception exception1) {
                return;
            }
            try {
                throw new WrongStartingPosition();
            }
            catch(WrongStartingPosition wrongExc) {
                return;
            }
        }

        toPaint.add(startingPosition);

        while(!toPaint.isEmpty()) {
            Position position = toPaint.remove(0);
            if(notContains(painted, position))
                painted.add(position);
            else
                continue;
            try {
                canvas.setColor(position, color);
            }
            catch(CanvasInterface.CanvasBorderException exception) {
                continue;
            }
            catch(CanvasInterface.BorderColorException exception) {
                try {
                    canvas.setColor(position, exception.previousColor);
                }
                catch(Exception exception1) {
                    continue;
                }
                continue;
            }
            if(notContains(painted, new Position2D(position.getRow()+1, position.getCol())))
                toPaint.add(new Position2D(position.getRow()+1, position.getCol()));
            if(notContains(painted, new Position2D(position.getRow()-1, position.getCol())))
                toPaint.add(new Position2D(position.getRow()-1, position.getCol()));
            if(notContains(painted, new Position2D(position.getRow(), position.getCol()+1)))
                toPaint.add(new Position2D(position.getRow(), position.getCol()+1));
            if(notContains(painted, new Position2D(position.getRow(), position.getCol()-1)))
                toPaint.add(new Position2D(position.getRow(), position.getCol()-1));
        }
    }
}
