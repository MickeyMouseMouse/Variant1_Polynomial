import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;

public class Tests {
    @Test
    public void createNewPolynomials() {
        // Создание полинома из ArrayList
        ArrayList<Double> array = new ArrayList<>();
        array.add(1.0);
        array.add(2.0);
        array.add(0.0);
        Polynomial p1 = new Polynomial(array);

        // Создание полинома из String (вариант 1)
        Polynomial p2 = new Polynomial("1 2 3");

        System.out.println(p1.toString());

        // Задание нужного шаблона для вывода вещественных чисел
        p2.setPattern("#0.000");
        System.out.println(p2.toString());

        // Проверка выкидывания ошибки при введении неправильной строки
        boolean fl = false;
        try {
            Polynomial p3 = new Polynomial("qwerty");
        }
        catch (IllegalArgumentException e) {
            fl = true;
        }
        assertEquals(true, fl);

        // Создание полинома из String (вариант 2)
        Polynomial p4 = new Polynomial("x^3-5x^2+12345");
        Polynomial p5 = new Polynomial("x^4+0");
    }

    @Test
    public void compareEquals() {
        // Равенство/неравенство двух полиномов
        Polynomial p1 = new Polynomial("1 2 3");
        Polynomial p2 = new Polynomial("1 2 3");
        Polynomial p3 = new Polynomial("3 3 3");
        assertEquals(true, p1.equals(p2));
        assertEquals(false, p1.equals(p3));
    }

    @Test
    public void valueOfPolynomial() {
        // Получение значения полинома при заданном x
        Polynomial p1 = new Polynomial("7 3 -6 1 -8");
        assertEquals(-3.0, p1.getValueWith(1), 0.0);
        assertEquals(4597.0, p1.getValueWith(5), 0.0);
    }

    @Test
    public void summationOfPolynomials() {
        // Сумма двух полиномов
        Polynomial p1 = new Polynomial("1 2 3");
        Polynomial p2 = new Polynomial("-1 6 4");
        assertEquals(new Polynomial("8 7"), p1.plus(p2));

        p1 = new Polynomial("1 2 3");
        p2 = new Polynomial("0 3 8 0 -1");
        assertEquals(new Polynomial("3 9 2 2"), p1.plus(p2));
    }

    @Test
    public void subtractOfPolynomials() {
        // Разность двух полиномов
        Polynomial p1 = new Polynomial("4 5 2");
        Polynomial p2 = new Polynomial("-1 5 0");
        assertEquals(new Polynomial("5 0 2"), p1.minus(p2));

        p1 = new Polynomial("1 0 -1");
        p2 = new Polynomial("1 -3 0 8");
        assertEquals(new Polynomial("-1 4 0 -9"), p1.minus(p2));
    }

    @Test
    public void multiplyOfPolynomials() {
        // Умножение двух полиномов
        Polynomial p1 = new Polynomial("3 8 5");
        Polynomial p2 = new Polynomial("2 7");
        assertEquals(new Polynomial("6 37 66 35"), p1.multiply(p2));

        p1 = new Polynomial("1 0");
        p2 = new Polynomial("1 0 1");
        assertEquals(new Polynomial("1 0 1 0"), p1.multiply(p2));
    }

    @Test
    public void integerPartOfDivision() {
        // Получение целой части от деления двух полиномов
        Polynomial p1 = new Polynomial("1 2 3");
        Polynomial p2 = new Polynomial("-1 2 0");
        assertEquals(new Polynomial("-1"), p1.division(p2, '/'));

        // Проверка выбрасывания ошибки при неверном формате аргумента mode
        boolean fl = false;
        try {
            Polynomial p3 = p1.division(p2, 'q');
        }
        catch (IllegalArgumentException e) {
            fl = true;
        }
        assertEquals(true, fl);
    }

    @Test
    public void remainderOfDivision() {
        // Получение остатка от деления двух полиномов
        Polynomial p1 = new Polynomial("5 -3 6");
        Polynomial p2 = new Polynomial("3 2");
        assertEquals(new Polynomial("10.222222222222221"), p1.division(p2, '%'));

        p1 = new Polynomial("1 2 0 -3");
        p2 = new Polynomial("1 8");
        assertEquals(new Polynomial("-387"), p1.division(p2, '%'));
    }
}
