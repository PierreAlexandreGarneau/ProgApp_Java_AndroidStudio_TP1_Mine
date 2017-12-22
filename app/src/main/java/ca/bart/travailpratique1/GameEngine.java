package ca.bart.travailpratique1;

        import android.content.Context;
        import android.util.Log;
        import android.view.View;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import ca.bart.travailpratique1.util.Generator;
        import ca.bart.travailpratique1.util.PrintGrid;
        import ca.bart.travailpratique1.views.Cell;

public class GameEngine {
    private static GameEngine instance;

    public static final int BOMB_NUMBER = 10;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    public int mineRestante = BOMB_NUMBER;
    public boolean gridCreated = false;

    private Context context;

    private Cell[][] MinesweeperGrid = new Cell[WIDTH][HEIGHT];

    public static GameEngine getInstance() {
        if( instance == null ){
            instance = new GameEngine();
        }
        return instance;
    }

    private GameEngine(){ }

    public void createGrid(Context context){
        if (gridCreated == false)
        {
            Log.e("GameEngine","createGrid is working");
            this.context = context;

            // Cree le tableau et le stocké
            int[][] GeneratedGrid = Generator.generate(BOMB_NUMBER, WIDTH, HEIGHT);
            PrintGrid.print(GeneratedGrid,WIDTH, HEIGHT);
            setGrid(context,GeneratedGrid);
            gridCreated = true;
        }
    }

    private void setGrid( final Context context, final int[][] grid ){

        for( int x = 0 ; x < WIDTH ; x++ ){
            for( int y = 0 ; y < HEIGHT ; y++ ){
                if( MinesweeperGrid[x][y] == null ){
                    Cell cell = new Cell( context , x,y);
                    float scale = context.getResources().getDisplayMetrics().density;
                    int px = (int) (20 * scale);
                    RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(px, px);
                    cell.setLayoutParams(layoutParams);
                    MinesweeperGrid[x][y] = cell;
                }
                MinesweeperGrid[x][y].setValue(grid[x][y]);
                MinesweeperGrid[x][y].invalidate();
            }
        }
    }

    public Cell getCellAt(int position) {
        int x = position % WIDTH;
        int y = position / WIDTH;

        return MinesweeperGrid[x][y];
    }

    public Cell getCellAt( int x , int y ){
        return MinesweeperGrid[x][y];
    }

    public void click( int x , int y ){
        if( x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT && !getCellAt(x,y).isClicked() ){
            getCellAt(x,y).setClicked();

            if( getCellAt(x,y).getValue() == 0 ){
                for( int xt = -1 ; xt <= 1 ; xt++ ){
                    for( int yt = -1 ; yt <= 1 ; yt++){
                        if( xt != yt ){
                            click(x + xt , y + yt);
                        }
                    }
                }
            }

            if( getCellAt(x,y).isBomb() ){
                onGameLost();
            }
        }

        checkEnd();
    }

    private boolean checkEnd(){
        int bombNotFound = BOMB_NUMBER;
        int notRevealed = WIDTH * HEIGHT;
        for ( int x = 0 ; x < WIDTH ; x++ ){
            for( int y = 0 ; y < HEIGHT ; y++ ){
                if( getCellAt(x,y).isRevealed() || getCellAt(x,y).isFlagged() ){
                    notRevealed--;
                }

                if( getCellAt(x,y).isFlagged() && getCellAt(x,y).isBomb() ){
                    bombNotFound--;
                }
            }
        }
        if( bombNotFound == 0 && notRevealed == 0 ){
            Toast.makeText(context,"Game won", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void flag( int x , int y ){
        boolean isFlagged = getCellAt(x,y).isFlagged();
        getCellAt(x,y).setFlagged(!isFlagged);
        getCellAt(x,y).invalidate();

        if (isFlagged)
            mineRestante++;
        else
            mineRestante--;

        aText.setText("Mines restantes : " + mineRestante);
    }

    private void onGameLost(){
        // handle lost game
        Toast.makeText(context,"Game lost", Toast.LENGTH_SHORT).show();

        for ( int x = 0 ; x < WIDTH ; x++ ) {
            for (int y = 0; y < HEIGHT; y++) {
                getCellAt(x,y).setRevealed();
            }
        }
    }

    private TextView aText;
    public void SetMineText(TextView text) {
        aText = text;
        aText.setText("Mines restantes : " + mineRestante);
    }

    public void Reset() {
        gridCreated = false;
        createGrid(context);
        mineRestante = BOMB_NUMBER;
        aText.setText("Mines restantes : " + mineRestante);
    }
}
