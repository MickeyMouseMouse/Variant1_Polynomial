import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import static java.lang.Math.pow;
import java.text.DecimalFormat;

/*
Вариант 1. Полином

Хранить полином вида 7x^4+3x^3-6x^2+x-8. Коэффициенты - вещественные числа.
Количество коэффициентов не оговаривается. Методы: сравнить два полинома на равенство,
рассчитать значение при данном x, сложить/вычесть/умножить/поделить два полинома,
найти остаток от деления одного полинома на другой.
*/
public final class Polynomial {
    // Массив с коэффициентами полинома
    // Коэффициенты идут по убыванию степеней
    private ArrayList<Double> coeff = new ArrayList<>();

    // Приватный метод для удаления ненужных нулевых коэффициентов спереди
    private void deleteNeedlessZeros(ArrayList<Double> array) {
        while (!array.isEmpty())
            if (array.get(0) >= -0.001 && array.get(0) <= 0.001)
                array.remove(0);
            else
                break;
    }

    // Конструктор с инициализацией из ArrayList
    Polynomial(ArrayList<Double> coefficients) {
        deleteNeedlessZeros(coeff = new ArrayList<>(coefficients));
        if (coeff.size() == 0) coeff.add(0.0);
    }

    // Конструктор с инициализацией из String (два вида строк)
    Polynomial(String coefficients) {
        if (coefficients.length() == 0) coeff.add(0.0);
        else {
            // Задана строка вида "1 2 3"
            if (coefficients.matches("^((-|)[0-9]+([.,][0-9]+|))(( (-|)([0-9]+([.,][0-9]+|))|([0-9]+([.,][0-9]+|)))+|)")) {
                for (String str : coefficients.split(" ")) {
                    double tmp = Double.parseDouble(str);
                    if (coeff.isEmpty() && tmp == 0.0) continue;

                    coeff.add(tmp);
                }

                if (coeff.isEmpty()) coeff.add(0.0);
            } else {
                // Задана строка вида "x^3+1"
                if (coefficients.matches("(((\\+|-|)([0-9]+([.,][0-9]+|)|)x(?!x)(\\^[0-9]+|))|((\\+|-|)([0-9]+([.,][0-9]+|))))+")) {
                    int i = 0;
                    int start = 0;
                    boolean fl = true;
                    while (i != coefficients.length() - 1) {
                        while (coefficients.charAt(i) != 'x' && i != coefficients.length() - 1)
                            i++;

                        double tmpCoeff = 0;

                        if (start == i) tmpCoeff = 1.0;

                        if (i - start == 1 && coefficients.charAt(i) == 'x')
                            if (coefficients.charAt(start) == '+') tmpCoeff = 1.0;
                            else if (coefficients.charAt(start) == '-') tmpCoeff = -1.0;

                        int tmpDegree = -1;

                        if (tmpCoeff == 0)
                            if (i == coefficients.length() - 1 && coefficients.charAt(i) != 'x') {
                                tmpCoeff = Double.parseDouble(coefficients.substring(start));
                                tmpDegree = 0;
                            } else
                                tmpCoeff = Double.parseDouble(coefficients.substring(start, i));

                        if (tmpDegree == -1) {
                            if (i + 1 == coefficients.length()) tmpDegree = 1;
                            else if (coefficients.charAt(++i) == '^') {
                                start = ++i;

                                while (coefficients.charAt(i) != '+' &&
                                        coefficients.charAt(i) != '-' &&
                                        i != coefficients.length() - 1)
                                    i++;

                                if (i != coefficients.length() - 1)
                                    tmpDegree = Integer.parseInt(coefficients.substring(start, i));
                                else
                                    tmpDegree = Integer.parseInt(coefficients.substring(start));
                            } else
                                tmpDegree = 1;
                        }

                        if (coeff.size() <= tmpDegree + 1) {
                            if (fl) {
                                while (coeff.size() < tmpDegree + 1)
                                    coeff.add(0.0);

                                fl = false;
                            } else
                                throw new IllegalArgumentException("Invalid string for polynomial");
                        }

                        coeff.set(coeff.size() - tmpDegree - 1, tmpCoeff);
                        start = i;
                    }
                } else
                    throw new IllegalArgumentException("Invalid string for polynomial");
            }
        }
    }

    // Переопределение equals для проверки двух полиномов на равенство
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (this.getClass() == obj.getClass()) {
            Polynomial other = (Polynomial) obj;
            return coeff.equals(other.coeff);
        }

        return false;
    }

    // Вывод полинома в привычном виде (можно задать шаблон вывода вещественных чисел)
    @Override
    public String toString() {
        final String pattern = "#0.0";
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < coeff.size(); i++) {
            if (coeff.get(i) == 0) continue;

            if (coeff.get(i) > 0 && string.length() != 0) string.append("+");

            if (coeff.get(i) != -1 && coeff.get(i) != 1)
                string.append(new DecimalFormat(pattern).format(coeff.get(i)));

            if (coeff.get(i) == -1) string.append("-");

            if (coeff.size() - i > 2)
                string.append("x^").append(coeff.size() - 1 - i);

            if (coeff.size() - i == 2) string.append("x");

            if (coeff.size() - i == 1 && (coeff.get(i) == 1 || coeff.get(i) == -1))
                string.append(new DecimalFormat(pattern).format(1.0));
        }

        if (string.length() == 0)
            return "0";
        else
            return string.toString();
    }

    // Расчет значения полинома при заданном x
    double getValueWith(double x) {
        AtomicInteger i = new AtomicInteger(coeff.size() - 1);
        return coeff
                .stream()
                .reduce(0.0, (a, b) -> a + b * pow(x, i.getAndDecrement()));

        /*
        // Другой вариант
        double result = 0;

        for (int i = 0; i < coeff.size(); i++)
            result += coeff.get(i) * pow(x, coeff.size() - 1 - i);

        return result;
        */
    }

    // Приватный метод для сложения и вычитания
    // Вынесен отдельно, так как оба действия отличиаются всего на один знак (true = '+', false = '-')
    private Polynomial plusAndMinus(Polynomial first, Polynomial second, boolean mode) {
        final double sign;
        if (mode)
            sign = 1.0;
        else
            sign = -1.0;

        ArrayList<Double> coeffThis = new ArrayList<>(first.coeff);
        ArrayList<Double> coeffOther = new ArrayList<>(second.coeff);

        while (coeffThis.size() > coeffOther.size())
            coeffOther.add(0, 0.0);

        while (coeffThis.size() < coeffOther.size())
            coeffThis.add(0, 0.0);

        ArrayList<Double> answer = new ArrayList<>();

        for (int i = 0; i < coeffThis.size(); i++)
            answer.add(coeffThis.get(i) + sign * coeffOther.get(i));

        deleteNeedlessZeros(answer);

        return new Polynomial(answer);
    }

    // Сложение двух полиномов (результат помещается в третий полином)
    Polynomial plus(Polynomial other) {
        return plusAndMinus(this, other, true);
    }

    // Вычитание двух полиномов (результат помещается в третий полином)
    Polynomial minus(Polynomial other) {
        return plusAndMinus(this, other, false);
    }

    // Умножене двух полиномов (результат помещается в третий полином)
    Polynomial multiply(Polynomial other) {
        ArrayList<Double> coeffThis = new ArrayList<>(coeff);
        ArrayList<Double> coeffOther = new ArrayList<>(other.coeff);

        ArrayList<Double> answer = new ArrayList<>();

        while (answer.size() <= other.coeff.size() + coeff.size() - 2)
            answer.add(0.0);

        for (int i = 0; i < coeff.size(); i++)
            for (int j = 0; j < other.coeff.size(); j++)
                answer.set(i + j, answer.get(i + j) + coeffThis.get(i) * coeffOther.get(j));

        return new Polynomial(answer);
    }

    // Деление двух полиномов (результат помещается в третий полином)
    // В зависимости от значения mode (/ или %) возвращается целая или дробная часть
    Polynomial division(Polynomial other, char mode) {
        if (mode != '/' && mode != '%')
            throw new IllegalArgumentException("mode: / or %");

        if (other.equals(new Polynomial("")))
            throw new IllegalArgumentException("Division on zero");

        int degreeThis = coeff.size() - 1;
        final int degreeOther = other.coeff.size() - 1;
        if (degreeThis < degreeOther)
            if (mode == '/')
                return new Polynomial("");
            else
                return other;

        Polynomial newThis = new Polynomial(coeff);
        Polynomial answer = new Polynomial("");
        while (degreeThis >= degreeOther) {
            Polynomial partOfAnswer = new Polynomial("");

            for (int i = 0; i < degreeThis - degreeOther; i++)
                partOfAnswer.coeff.add(0.0);

            partOfAnswer.coeff.set(0, newThis.coeff.get(0) / other.coeff.get(0));

            newThis = newThis.minus(other.multiply(partOfAnswer));
            degreeThis = newThis.coeff.size() - 1;

            answer = answer.plus(partOfAnswer);
        }

        if (mode == '/')
            return answer;
        else
            return this.minus(other.multiply(answer));
    }
}
