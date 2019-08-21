import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class TetrisGame extends Game {
    private final int WIDTH = 20;
    private final int HEIGHT = 30;
    private int turnDelay = 500;
    private static Field field;
    private static Figure figure;
    private boolean isGameOver;
    private static int gameScore;

    /**
     * Геттер переменной field.
     */
    public static Field getField() {
        return field;
    }

    /**
     * Геттер переменной figure.
     */
    public static Figure getFigure() {
        return figure;
    }

    public static void setGameScore(int score) {
        gameScore += score;
    }

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        showGrid(true);
        setTurnTimer(turnDelay);
        setScore(gameScore);
        field = new Field(WIDTH, HEIGHT);
        //выставляем начальное значение переменной "игра окончена" в ЛОЖЬ
        isGameOver = false;
        //создаем первую фигурку посередине сверху: x - половина ширины, y - 0.
        figure = FigureFactory.createRandomFigure(WIDTH / 2, 0);
        drawField();
    }

    @Override
    public void onTurn(int delay) {
        if (!isGameOver) {
            //опускаем фигурку вниз
            figure.down();

            //если разместить фигурку на текущем месте невозможно
            if (!figure.isCurrentPositionAvailable()) {
                figure.up();                    //поднимаем обратно
                figure.landed();                //приземляем
                gameScore++;

                isGameOver = figure.getY() <= 1;//если фигурка приземлилась на самом верху - игра окончена

                field.removeFullLines();        //удаляем заполненные линии

                figure = FigureFactory.createRandomFigure(field.getWidth() / 2, 0); //создаем новую фигурку
            }

            drawField();
            setScore(gameScore);
        } else {
            showMessageDialog(Color.YELLOW, "Game over!", Color.GREEN, 20);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.LEFT)
            figure.left();
            //Если "стрелка вправо" - сдвинуть фигурку вправо
        else if (key == Key.RIGHT)
            figure.right();
            //Если "пробел" - повернуть фигурку
        else if (key == Key.SPACE)
            figure.rotate();
            //Если "вниз" - фигурка падает вниз на максимум
        else if (key == Key.DOWN)
            figure.downMaximum();
    }

    public void drawField() {
        //Создаем массив, куда будем "рисовать" текущее состояние игры
        int[][] canvas = new int[HEIGHT][WIDTH];

        //Копируем "матрицу поля" в массив
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                canvas[i][j] = field.getMatrix()[i][j];
            }
        }

        //Копируем фигурку в массив, только непустые клетки
        int left = figure.getX();
        int top = figure.getY();
        int[][] brickMatrix = figure.getMatrix();
        Color color = figure.getColor();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (top + i >= HEIGHT || left + j >= WIDTH) continue;
                if (brickMatrix[i][j] == 1)
                    canvas[top + i][left + j] = 2;
            }
        }

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                int index = canvas[i][j];
                if (index == 0)
                    setCellColor(j, i, Color.GRAY);
                else if (index == 1 || index == 2)
                    setCellColor(j, i, color);
            }
        }
    }
}