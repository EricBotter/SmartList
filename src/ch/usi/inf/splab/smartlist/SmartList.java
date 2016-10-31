package ch.usi.inf.splab.smartlist;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//import java.util.function.UnaryOperator;
//import java.util.stream.Stream;

public class SmartList<E> extends ArrayList<E> {

    private static int globalCounter = 0;

    private String filename;

//    private HashMap<String, Integer> callCounter;
    private HashMap<Integer, Integer> insertCounter;
    private HashMap<Integer, Integer> removeCounter;
    private HashMap<Integer, Integer> getCounter;
    private HashMap<Integer, Integer> setCounter;

    @Override
    public void finalize() throws Throwable { // Nice Java coding skills
        dumpStatsToFile();
        super.finalize();
    }

    private void customInit() {
        filename = "./log_" + (globalCounter++) + ".txt";
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write("CALLS\n".getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        callCounter = new HashMap<>();
        insertCounter = new HashMap<>();
        removeCounter = new HashMap<>();
        getCounter = new HashMap<>();
        setCounter = new HashMap<>();
    }

    public SmartList() {
        super();
        customInit();
        traceCall("<init>", new String[]{});
    }

    public SmartList(int capacity) {
        super(capacity);
        customInit();
        traceCall("<init>", new String[]{Integer.toString(capacity)});
    }

    public SmartList(Collection<? extends E> c) {
        super(c);
        customInit();
        traceCall("<init>", new String[]{c == null ? "null" : c.toString()});
    }

    private <K, V> V mapGetOrDefault(Map<K, V> map, K key, V value) {
        if (map.containsKey(key))
            return map.get(key);
        return value;
    }

    private <T> String stringJoin(String delimiter, T[] array) {
        if (array.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i].toString());
            sb.append(delimiter);
        }
        sb.append(array[array.length-1].toString());
        return sb.toString();
    }

    private void traceCall(String method, String[] arguments) {
//        callCounter.put(method, mapGetOrDefault(callCounter, method, 0) + 1);

        try {
            FileOutputStream fos = new FileOutputStream(filename, true);
            fos.write(('\t' + method + '(' + stringJoin(", ", arguments) + ")\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dumpStatsToFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("\nSTATS\n");
        List<HashMap<Integer, Integer>> list = Arrays.asList(getCounter, insertCounter, removeCounter);
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

        FileOutputStream fos = new FileOutputStream(filename, true);
        fos.write(sb.toString().getBytes());
        fos.close();
    }

    @Override
    public int size() {
        traceCall("size", new String[]{});
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        traceCall("isEmpty", new String[]{});
        return super.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        traceCall("contains", new String[]{o == null ? "null" : o.toString()});
        int out = super.indexOf(o);
        for (int i = 0; i <= out; i++) {
            getCounter.put(i, mapGetOrDefault(getCounter, i, 0) + 1);
        }
        return out >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        traceCall("iterator", new String[]{});
        return super.iterator();
    }

//    @Override
//    public void forEach(Consumer<? super E> action) {
//        traceCall("forEach", new String[]{action.toString()});
//        // too expensive to trace these gets
//        super.forEach(action);
//    }

    @Override
    public Object[] toArray() {
        traceCall("toArray", new String[]{});
        return super.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        traceCall("toArray", new String[]{a == null ? "null" : a.toString()});
        return super.toArray(a);
    }

    @Override
    public boolean add(E e) {
        traceCall("add", new String[]{e == null ? "null" : e.toString()});
        insertCounter.put(super.size(), mapGetOrDefault(insertCounter, super.size(), 0) + 1);
        return super.add(e);
    }

    @Override
    public boolean remove(Object o) {
        traceCall("remove", new String[]{o == null ? "null" : o.toString()});
        removeCounter.put(super.size(), mapGetOrDefault(removeCounter, super.size(), 0) + 1);
        return super.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        traceCall("containsAll", new String[]{c == null ? "null" : c.toString()});
        // too expensive to trace these gets
        return super.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        traceCall("addAll(Collection)", new String[]{c == null ? "null" : c.toString()});
        for (int i = super.size(); i < super.size() + c.size(); i++) { // FIXME - is this really a bunch of adds?
            insertCounter.put(i, mapGetOrDefault(insertCounter, i, 0) + 1);
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        traceCall("addAll", new String[]{Integer.toString(index), c == null ? "null" : c.toString()});
        for (int i = index; i < index + c.size(); i++) { // FIXME - is this really a bunch of adds?
            insertCounter.put(i, mapGetOrDefault(insertCounter, i, 0) + 1);
        }
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        traceCall("removeAll", new String[]{c == null ? "null" : c.toString()});
        // too expensive to trace these removes
        return super.removeAll(c);
    }

//    @Override
//    public boolean removeIf(Predicate<? super E> filter) {
//        traceCall("removeIf", new String[]{filter.toString()});
//        // too expensive to trace these removes
//        return super.removeIf(filter);
//    }

    @Override
    public boolean retainAll(Collection<?> c) {
        traceCall("retainAll", new String[]{c == null ? "null" : c.toString()});
        return super.retainAll(c);
    }

//    @Override
//    public void replaceAll(UnaryOperator<E> operator) {
//        traceCall("replaceAll", new String[]{operator.toString()});
//        super.replaceAll(operator);
//    }

//    @Override
//    public void sort(Comparator<? super E> c) {
//        traceCall("sort", new String[]{c == null ? "null" : c.toString()});
//        super.sort(c);
//    }

    @Override
    public void clear() {
        traceCall("clear", new String[]{});
        super.clear();
    }

    @Override
    public E get(int index) {
        traceCall("get", new String[]{Integer.toString(index)});
        getCounter.put(index, mapGetOrDefault(getCounter, index, 0) + 1);
        return super.get(index);
    }

    @Override
    public E set(int index, E element) {
        traceCall("set", new String[]{Integer.toString(index), element == null ? "null" : element.toString()});
        setCounter.put(index, mapGetOrDefault(setCounter, index, 0) + 1);
        return super.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        //traceCall("add", new String[]{Integer.toString(index), element == null ? "null" : element.toString()});
        //insertCounter.put(index, mapGetOrDefault(insertCounter, index, 0) + 1);
        super.add(index, element);
    }

    @Override
    public E remove(int index) {
        traceCall("remove", new String[]{Integer.toString(index)});
        removeCounter.put(index, mapGetOrDefault(removeCounter, index, 0) + 1);
        return super.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        traceCall("indexOf", new String[]{o == null ? "null" : o.toString()});
        int out = super.indexOf(o);
        for (int i = 0; i <= out; i++) {
            getCounter.put(i, mapGetOrDefault(getCounter, i, 0) + 1);
        }
        return out;
    }

    @Override
    public int lastIndexOf(Object o) {
        traceCall("lastIndexOf", new String[]{o == null ? "null" : o.toString()});
        int out = super.lastIndexOf(o);
        for (int i = out; i < super.size(); i++) {
            getCounter.put(i, mapGetOrDefault(getCounter, i, 0) + 1);
        }
        return out;
    }

    @Override
    public ListIterator<E> listIterator() {
        traceCall("listIterator()", new String[]{});
        return super.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        traceCall("listIterator(int)", new String[]{Integer.toString(index)});
        return super.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        traceCall("subList", new String[]{Integer.toString(fromIndex), Integer.toString(toIndex)});
        return super.subList(fromIndex, toIndex);
    }

//    @Override
//    public Spliterator<E> spliterator() {
//        traceCall("spliterator", new String[]{});
//        return super.spliterator();
//    }

//    @Override
//    public Stream<E> stream() {
//        traceCall("stream", new String[]{});
//        return super.stream();
//    }

//    @Override
//    public Stream<E> parallelStream() {
//        traceCall("parallelStream", new String[]{});
//        return super.parallelStream();
//    }
}
