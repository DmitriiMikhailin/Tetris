import java.util.ArrayList;

/**
 * Класс Field описывает "поле клеток" игры Тетрис
 */
public class Field {
    //ширина и высота
    private int width;
    private int height;

    //матрица поля: 1 - клетка занята, 0 - свободна
    private int[][] matrix;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        matrix = new int[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Метод возвращает значение, которое содержится в матрице с координатами (x,y)
     * Если координаты за пределами матрицы, метод возвращает null.
     */
    public Integer getValue(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            return matrix[y][x];

        return null;
    }

    /**
     * Метод устанавливает переданное значение(value) в ячейку матрицы с координатами (x,y)
     */
    public void setValue(int x, int y, int value) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            matrix[y][x] = value;
    }

    /**
     * Удаляем заполненные линии
     */
    public void removeFullLines() {
        //Создаем список для хранения линий
        ArrayList<int[]> lines = new ArrayList<int[]>();

        //Копируем все непустые линии в список.
        for (int i = 0; i < height; i++) {
            //подсчитываем количество единиц в строке - просто суммируем все ее значения
            int count = 0;
            for (int j = 0; j < width; j++) {
                count += matrix[i][j];
            }

            //Если сумма строки не равна ее ширине - добавляем в список
            if (count != width)
                lines.add(matrix[i]);
        }

        //Добавляем недостающие строки в начало списка.
        while (lines.size() < height) {
            lines.add(0, new int[width]);
            TetrisGame.setGameScore(10);
        }

        //Преобразуем список обратно в матрицу
        matrix = lines.toArray(new int[height][width]);
    }
}
