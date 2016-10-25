package ch.usi.inf.splab.smartlist;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class SmartList<E> extends ArrayList<E> {

    private final String FILENAME = "./log.txt";

    private HashMap<String, Integer> callCounter;
    private HashMap<Integer, Integer> insertCounter;
    private HashMap<Integer, Integer> removeCounter;
    private HashMap<Integer, Integer> getCounter;
    private HashMap<Integer, Integer> setCounter;

    public SmartList() {
        super();
        callCounter = new HashMap<>();
        insertCounter = new HashMap<>();
        removeCounter = new HashMap<>();
        getCounter = new HashMap<>();
        setCounter = new HashMap<>();
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
        sb.append("\nSTATS\n");
        List<Map<Integer, Integer>> list = Arrays.asList(getCounter, insertCounter, removeCounter);
        for (int i = 0; i < 3; i++) {
            Map<Integer, Integer> m = list.get(i);
            switch (i) {
                case 0:
                    sb.append("\tGET\n");
                    break;
                case 1:
                    sb.append("\tINSERT\n");
                    break;
                case 2:
                    sb.append("\tREMOVE\n");
            }
            for (int index : m.keySet()) {
                sb.append("\t\t");
                sb.append(index);
                sb.append(": ");
                sb.append(m.get(index));
                sb.append('\n');
            }
        }

        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(sb.toString().getBytes());
        fos.close();
    }

    @Override
    public int size() {
        callCounter.put("size", callCounter.getOrDefault("size", 0) + 1);
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        callCounter.put("isEmpty", callCounter.getOrDefault("isEmpty", 0) + 1);
        return super.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        callCounter.put("contains", callCounter.getOrDefault("contains", 0) + 1);
        int out = super.indexOf(o);
        for (int i = 0; i <= out; i++) {
            getCounter.put(i, getCounter.getOrDefault(i, 0) + 1);
        }
        return out >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        callCounter.put("iterator", callCounter.getOrDefault("iterator", 0) + 1);
        return super.iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        callCounter.put("forEach", callCounter.getOrDefault("forEach", 0) + 1);
        // too expensive to trace these gets
        super.forEach(action);
    }

    @Override
    public Object[] toArray() {
        callCounter.put("toArray()", callCounter.getOrDefault("toArray()", 0) + 1);
        return super.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        callCounter.put("toArray(T[])", callCounter.getOrDefault("toArray(T[])", 0) + 1);
        return super.toArray(a);
    }

    @Override
    public boolean add(E e) {
        callCounter.put("add", callCounter.getOrDefault("add", 0) + 1);
        insertCounter.put(super.size(), insertCounter.getOrDefault(super.size(), 0) + 1);
        return super.add(e);
    }

    @Override
    public boolean remove(Object o) {
        callCounter.put("remove", callCounter.getOrDefault("remove", 0) + 1);
        removeCounter.put(super.size(), removeCounter.getOrDefault(super.size(), 0) + 1);
        return super.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        callCounter.put("containsAll", callCounter.getOrDefault("containsAll", 0) + 1);
        // too expensive to trace these gets
        return super.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        callCounter.put("addAll(Collection)", callCounter.getOrDefault("addAll(Collection)", 0) + 1);
        for (int i = super.size(); i < super.size() + c.size(); i++) { // FIXME - is this really a bunch of adds?
            insertCounter.put(i, insertCounter.getOrDefault(i, 0) + 1);
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        callCounter.put("addAll(int, Collection)", callCounter.getOrDefault("addAll(int, Collection)", 0) + 1);
        for (int i = index; i < index + c.size(); i++) { // FIXME - is this really a bunch of adds?
            insertCounter.put(i, insertCounter.getOrDefault(i, 0) + 1);
        }
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        callCounter.put("removeAll(Collection)", callCounter.getOrDefault("removeAll(Collection)", 0) + 1);
        // too expensive to trace these removes
        return super.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        callCounter.put("removeIf", callCounter.getOrDefault("removeIf", 0) + 1);
        // too expensive to trace these removes
        return super.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        callCounter.put("retainAll", callCounter.getOrDefault("retainAll", 0) + 1);
        return super.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        callCounter.put("replaceAll", callCounter.getOrDefault("replaceAll", 0) + 1);
        super.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        callCounter.put("sort", callCounter.getOrDefault("sort", 0) + 1);
        super.sort(c);
    }

    @Override
    public void clear() {
        callCounter.put("clear", callCounter.getOrDefault("clear", 0) + 1);
        super.clear();
    }

    @Override
    public E get(int index) {
        callCounter.put("get", callCounter.getOrDefault("get", 0) + 1);
        getCounter.put(index, getCounter.getOrDefault(index, 0) + 1);
        return super.get(index);
    }

    @Override
    public E set(int index, E element) {
        callCounter.put("set", callCounter.getOrDefault("set", 0) + 1);
        setCounter.put(index, setCounter.getOrDefault(index, 0) + 1);
        return super.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        callCounter.put("add", callCounter.getOrDefault("add", 0) + 1);
        insertCounter.put(index, insertCounter.getOrDefault(index, 0) + 1);
        super.add(index, element);
    }

    @Override
    public E remove(int index) {
        callCounter.put("remove", callCounter.getOrDefault("remove", 0) + 1);
        removeCounter.put(index, removeCounter.getOrDefault(index, 0) + 1);
        return super.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        callCounter.put("indexOf", callCounter.getOrDefault("indexOf", 0) + 1);
        int out = super.indexOf(o);
        for (int i = 0; i <= out; i++) {
            getCounter.put(i, getCounter.getOrDefault(i, 0) + 1);
        }
        return out;
    }

    @Override
    public int lastIndexOf(Object o) {
        callCounter.put("lastIndexOf", callCounter.getOrDefault("lastIndexOf", 0) + 1);
        int out = super.lastIndexOf(o);
        for (int i = out; i < super.size(); i++) {
            getCounter.put(i, getCounter.getOrDefault(i, 0) + 1);
        }
        return out;
    }

    @Override
    public ListIterator<E> listIterator() {
        callCounter.put("listIterator()", callCounter.getOrDefault("listIterator()", 0) + 1);
        return super.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        callCounter.put("listIterator(int)", callCounter.getOrDefault("listIterator(int)", 0) + 1);
        return super.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        callCounter.put("subList", callCounter.getOrDefault("subList", 0) + 1);
        return super.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        callCounter.put("spliterator", callCounter.getOrDefault("spliterator", 0) + 1);
        return super.spliterator();
    }

    @Override
    public Stream<E> stream() {
        callCounter.put("stream", callCounter.getOrDefault("stream", 0) + 1);
        return super.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        callCounter.put("parallelStream", callCounter.getOrDefault("parallelStream", 0) + 1);
        return super.parallelStream();
    }
}
