package fields;

import java.util.Arrays;
import java.util.BitSet;

/**
 * Класс простого поля. Поддерживает операции сложения,
 * умножения и деления полей.
 * Задавать поля можно с помощью строки, состоящей из нулей и единиц
 * (можно записывать без пробелов, с пробелами, с запятыми и т.д., как удобно,
 * конструктор удалит лишние символы, кроме нулей и единиц).
 * Также поддерживается запись как от младших разрядов к старшим, так и
 * наоборот, но стандартно принимается запись от "старших к младшим".
 */
public class Field {

    private static final int P = 2;
    private static final int M = 1;

    // элементы поля записываются по убыванию (desc)
    // (от старших разрядов к младшим)
    private BitSet items;

    // направление задания простого поля из двоичного вида
    public enum Direction {
        ASC, // по возрастанию
        DESC // по убыванию
    }

    private static final String regex = "[^01]";

    /**
     * @param items строка с положением битов "от старших к младшим"
     */
    public Field(String items) {
        this(Direction.DESC, items);
    }

    /**
     * @param direction DESC - от старших к младшим битам,
     *                  ASC - от младших к старшим
     * @param items
     */
    public Field(Direction direction, String items) {
        // удаляем лишние символы из входящей строки
        String string = items.replaceAll(regex, "");

        this.items = new BitSet(string.length());

        // если подана строка в "от старших битов к младшим"
        if (direction == Direction.DESC) {
            string = new StringBuilder(string).reverse().toString();
        }

        // перевод строки в BitSet
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                this.items.set(i);
            }
        }
    }

    /**
     * Приватный конструктор на основе BitSet
     * @param set сет, на основе которого нужно создать полином
     */
    private Field(BitSet set) {
        items = set;
    }

    /**
     * Операция сложения двух полиномов
     * @param field полином, с которым нужно сложить исходный полином
     * @return результат сложения полиномов
     */
    public Field add(Field field) {
        BitSet bits = (BitSet) items.clone();
        bits.xor(field.items);
        return new Field(bits);
    }

    /**
     * @return количество используемых бит
     */
    public int length() {
        return items.length();
    }

    /**
     * @return представление в виде [1 0 0 1 1]
     */
    @Override
    public String toString() {
        if (items == null)
            return "null";

        int iMax = length() - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(items.get(iMax - i) ? '1' : '0');
            if (i == iMax)
                return b.append(']').toString();
            b.append(' '); // b.append(", ");
        }
    }

    /**
     * Статический метод перевода полинома в десятичное число
     * @param field исходный полином
     * @return полином в десятичном виде
     */
    public static int toInt(Field field) {
        return Integer.parseInt(field.toString().replaceAll(regex, ""), 2);
    }

    /**
     * Статический метод перевода полинома в десятичное число
     * @param field исходный полином в виде строки (в свободном стиле)
     * @return полином в десятичном виде
     */
    public static int toInt(String field) {
        return toInt(new Field(field));
    }

    /**
     * Метод перевода полинома в десятичное число
     * @return полином в десятичном виде
     */
    public int toInt() {
        return toInt(this);
    }
}