package ch.usi.inf.splab.smartlist;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class SmartList<E> implements List<E> {

    private final String FILENAME = "./log.txt";

    private ArrayList<E> arraylist;

    private HashMap<String, Integer> callCounter;
    private HashMap<Integer, Integer> insertCounter;
    private HashMap<Integer, Integer> removeCounter;
    private HashMap<Integer, Integer> getCounter;

    public SmartList() {
        arraylist = new ArrayList<>();
        callCounter = new HashMap<>();
        insertCounter = new HashMap<>();
        removeCounter = new HashMap<>();
        getCounter = new HashMap<>();
    }

    public void dumpStatsToFile() throws IOException {
        dumpStatsToFile(FILENAME);
    }

    public void dumpStatsToFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("CALLS\n");
        for (String method : callCounter.keySet()) {
            sb.append('\t');
            sb.append(method);
            sb.append(": ");
            sb.append(callCounter.get(method));
            sb.append('\n');
        }
        sb.append("\nSTATS\n\tGET\n");
        for (int index: getCounter.keySet()) {
            sb.append("\t\t");
            sb.append(index);
            sb.append(": ");
            sb.append(getCounter.get(index));
            sb.append('\n');
        }
        sb.append("\tINSERT\n");
        for (int index: insertCounter.keySet()) {
            sb.append("\t\t");
            sb.append(index);
            sb.append(": ");
            sb.append(insertCounter.get(index));
            sb.append('\n');
        }
        sb.append("\tREMOVE\n");
        for (int index: removeCounter.keySet()) {
            sb.append("\t\t");
            sb.append(index);
            sb.append(": ");
            sb.append(removeCounter.get(index));
            sb.append('\n');
        }

        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(sb.toString().getBytes());
        fos.close();
    }

    @Override
    public int size() {
        callCounter.put("size", callCounter.getOrDefault("size", 0) + 1);
        return arraylist.size();
    }

    @Override
    public boolean isEmpty() {
        callCounter.put("isEmpty", callCounter.getOrDefault("isEmpty", 0) + 1);
        return arraylist.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        callCounter.put("contains", callCounter.getOrDefault("contains", 0) + 1);
        return arraylist.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        callCounter.put("iterator", callCounter.getOrDefault("iterator", 0) + 1);
        return arraylist.iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        callCounter.put("forEach", callCounter.getOrDefault("forEach", 0) + 1);
        arraylist.forEach(action);
    }

    @Override
    public Object[] toArray() {
        return arraylist.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return arraylist.toArray(a);
    }

    @Override
    public boolean add(E e) {
        callCounter.put("add", callCounter.getOrDefault("add", 0) + 1);
        insertCounter.put(arraylist.size(), insertCounter.getOrDefault(arraylist.size(), 0) + 1);
        return arraylist.add(e);
    }

    @Override
    public boolean remove(Object o) {
        callCounter.put("remove", callCounter.getOrDefault("remove", 0) + 1);
        removeCounter.put(arraylist.size(), removeCounter.getOrDefault(arraylist.size(), 0) + 1);
        return arraylist.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return arraylist.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        callCounter.put("addAll", callCounter.getOrDefault("addAll", 0) + 1);
        return arraylist.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return arraylist.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return arraylist.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return arraylist.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return arraylist.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        arraylist.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        arraylist.sort(c);
    }

    @Override
    public void clear() {
        callCounter.put("clear", callCounter.getOrDefault("clear", 0) + 1);
        arraylist.clear();
    }

    @Override
    public E get(int index) {
        callCounter.put("get", callCounter.getOrDefault("get", 0) + 1);
        getCounter.put(index, getCounter.getOrDefault(index, 0) + 1);
        return arraylist.get(index);
    }

    @Override
    public E set(int index, E element) {
        callCounter.put("set", callCounter.getOrDefault("set", 0) + 1);
        //TODO
        return arraylist.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        callCounter.put("add", callCounter.getOrDefault("add", 0) + 1);
        insertCounter.put(index, insertCounter.getOrDefault(index, 0) + 1);
        arraylist.add(index, element);
    }

    @Override
    public E remove(int index) {
        callCounter.put("remove", callCounter.getOrDefault("remove", 0) + 1);
        removeCounter.put(index, removeCounter.getOrDefault(index, 0) + 1);
        return arraylist.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return arraylist.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return arraylist.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return arraylist.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return arraylist.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return arraylist.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return arraylist.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return arraylist.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return arraylist.parallelStream();
    }
}
