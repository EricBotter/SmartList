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

    private void customInit() {
        filename = "./log_" + String.format("%04d", globalCounter++) + ".txt";
    }

    public SmartList() {
        super();
        customInit();
        traceCall("<init>", null);
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

//    private <K, V> V mapGetOrDefault(Map<K, V> map, K key, V value) {
//        if (map.containsKey(key))
//            return map.get(key);
//        return value;
//    }

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
        traceCall(method, arguments, null);
    }

    private void traceCall(String method, String[] arguments, Map<String, String> extra) {
//        callCounter.put(method, mapGetOrDefault(callCounter, method, 0) + 1);

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Size: ");
            sb.append(super.size());
            sb.append('\t');
            if (extra != null) {
                for (Map.Entry<String, String> entry : extra.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(": ");
                    sb.append(entry.getValue());
                    sb.append('\t');
                }
            }

            sb.append("Method: ");
            sb.append(method);
            sb.append('(');
            if (arguments != null) {
                sb.append(stringJoin(", ", arguments));
            }
            sb.append(")\n");

            FileOutputStream fos = new FileOutputStream(filename, true);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        traceCall("size", null);
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        traceCall("isEmpty", null);
        return super.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        traceCall("contains", new String[]{o == null ? "null" : o.toString()});
        return super.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        traceCall("iterator", null);
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
        traceCall("toArray", null);
        return super.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        traceCall("toArray", new String[]{a == null ? "null" : a.toString()});
        return super.toArray(a);
    }

    @Override
    public boolean add(E e) {
        HashMap<String, String> extra = new HashMap<>();
        extra.put("Insert", Integer.toString(super.size()));
        traceCall("add", new String[]{e == null ? "null" : e.toString()}, extra);
        return super.add(e);
    }

    @Override
    public boolean remove(Object o) {
        HashMap<String, String> extra = new HashMap<>();
        extra.put("Remove", Integer.toString(super.indexOf(o)));
        traceCall("remove", new String[]{o == null ? "null" : o.toString()}, extra);
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
        // TODO trace these inserts
        //for (int i = super.size(); i < super.size() + c.size(); i++) { // FIXME - is this really a bunch of adds?
        //    insertCounter.put(i, mapGetOrDefault(insertCounter, i, 0) + 1);
        //}
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        traceCall("addAll", new String[]{Integer.toString(index), c == null ? "null" : c.toString()});
        // TODO trace these inserts
        //for (int i = index; i < index + c.size(); i++) { // FIXME - is this really a bunch of adds?
        //    insertCounter.put(i, mapGetOrDefault(insertCounter, i, 0) + 1);
        //}
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        traceCall("removeAll", new String[]{c == null ? "null" : c.toString()});
        // FIXME - too expensive to trace these removes
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
        traceCall("clear", null);
        super.clear();
    }

    @Override
    public E get(int index) {
        HashMap<String, String> extra = new HashMap<>();
        extra.put("Get", Integer.toString(index));
        traceCall("get", new String[]{Integer.toString(index)}, extra);
        return super.get(index);
    }

    @Override
    public E set(int index, E element) {
        HashMap<String, String> extra = new HashMap<>();
        extra.put("Set", Integer.toString(index));
        traceCall("set", new String[]{Integer.toString(index), element == null ? "null" : element.toString()}, extra);
        return super.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        HashMap<String, String> extra = new HashMap<>();
        extra.put("Insert", Integer.toString(index));
        traceCall("add", new String[]{Integer.toString(index), element == null ? "null" : element.toString()}, extra);
        super.add(index, element);
    }

    @Override
    public E remove(int index) {
        HashMap<String, String> extra = new HashMap<>();
        extra.put("Remove", Integer.toString(index));
        traceCall("remove", new String[]{Integer.toString(index)}, extra);
        return super.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        traceCall("indexOf", new String[]{o == null ? "null" : o.toString()});
        return super.indexOf(o); // TODO - should we trace this?
    }

    @Override
    public int lastIndexOf(Object o) {
        traceCall("lastIndexOf", new String[]{o == null ? "null" : o.toString()});
        return super.lastIndexOf(o); // TODO - should we trace this?
    }

    @Override
    public ListIterator<E> listIterator() {
        traceCall("listIterator()", null);
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
//        traceCall("spliterator", null);
//        return super.spliterator();
//    }

//    @Override
//    public Stream<E> stream() {
//        traceCall("stream", null);
//        return super.stream();
//    }

//    @Override
//    public Stream<E> parallelStream() {
//        traceCall("parallelStream", null);
//        return super.parallelStream();
//    }
}
