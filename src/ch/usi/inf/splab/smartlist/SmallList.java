package ch.usi.inf.splab.smartlist;

import java.util.*;

public class SmallList<E> implements List<E> {

    private class Wrapper<T> {
        T value;

        Wrapper() {
        }

        Wrapper(T t) {
            value = t;
        }
    }

    private Object element;

    public SmallList() {
        element = null;
    }

    public SmallList(int capacity) {
        if (capacity == 1) {
            element = new Wrapper();
        } else {
            element = new ArrayList<>(capacity);
        }
    }

    public SmallList(Collection<? extends E> c) {
        element = new ArrayList<>(c);
    }

    private void forceToArrayList() {
        if (element == null)
            element = new ArrayList<>();
        if (element instanceof Wrapper) {
            E temp = ((Wrapper<E>) element).value;
            ArrayList<E> al = new ArrayList<>();
            al.add(temp);
            element = al;
        }
    }

    @Override
    public int size() {
        if (element instanceof Wrapper)
            return 1;
        if (element instanceof ArrayList)
            return ((ArrayList) element).size();
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return element == null;
    }

    @Override
    public boolean contains(Object o) {
        if (element instanceof Wrapper)
            return o.equals(((Wrapper<E>) element).value);
        return element instanceof ArrayList && ((ArrayList) element).contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        forceToArrayList();
        return ((ArrayList<E>) element).iterator();
    }

//    @Override
//    public void forEach(Consumer<? super E> action) {
//        traceCall("forEach", new String[]{action.toString()});
//        // too expensive to trace these gets
//        super.forEach(action);
//    }

    @Override
    public Object[] toArray() {
        forceToArrayList();
        return ((ArrayList<E>) element).toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        forceToArrayList();
        return ((ArrayList<E>) element).toArray(a);
    }

    @Override
    public boolean add(E e) {
        if (element == null) {
            element = new Wrapper<>(e);
            return true;
        } else {
            forceToArrayList();
            return ((ArrayList<E>) element).add(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        if (element instanceof Wrapper) {
            if (((Wrapper<E>) element).value.equals(o)) {
                element = null;
                return true;
            }
            return false;
        }
        return element != null && ((ArrayList<E>) element).remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        forceToArrayList();
        return ((ArrayList<E>) element).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        forceToArrayList();
        return ((ArrayList<E>) element).addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        forceToArrayList();
        return ((ArrayList<E>) element).addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        forceToArrayList();
        return ((ArrayList<E>) element).removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        forceToArrayList();
        return ((ArrayList<E>) element).retainAll(c);
    }

    @Override
    public void clear() {
        element = null;
    }

    @Override
    public E get(int index) {
        if (element instanceof Wrapper) {
            if (index > 0)
                throw new IndexOutOfBoundsException();
            return ((Wrapper<E>) element).value;
        }
        if (element == null)
            throw new IndexOutOfBoundsException();
        return ((ArrayList<E>) element).get(index);
    }

    @Override
    public E set(int index, E e) {
        if (element instanceof Wrapper) {
            if (index > 0)
                throw new IndexOutOfBoundsException();
            return ((Wrapper<E>) element).value = e;
        }
        if (element == null)
            throw new IndexOutOfBoundsException();
        return ((ArrayList<E>) element).set(index, e);
    }

    @Override
    public void add(int index, E e) {
        if (element == null) {
            if (index > 0)
                throw new IndexOutOfBoundsException();
            this.element = new Wrapper<E>(e);
        }
        forceToArrayList();
        ((ArrayList<E>) element).add(index, e);
    }

    @Override
    public E remove(int index) {
        if (element instanceof Wrapper) {
            if (index > 0)
                throw new IndexOutOfBoundsException();
            E out = ((Wrapper<E>) element).value;
            element = null;
            return out;
        }
        if (element == null)
            throw new IndexOutOfBoundsException();
        return ((ArrayList<E>) element).remove(index);
    }

    @Override
    public int indexOf(Object o) {
        if (element instanceof Wrapper) {
            E val = ((Wrapper<E>) element).value;
            if (val.equals(o))
                return 0;
            return -1;
        }
        if (element == null)
            return -1;
        return ((ArrayList<E>) element).indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        forceToArrayList();
        return ((ArrayList<E>) element).lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        forceToArrayList();
        return ((ArrayList<E>) element).listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        forceToArrayList();
        return ((ArrayList<E>) element).listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        forceToArrayList();
        return ((ArrayList<E>) element).subList(fromIndex, toIndex);
    }
}
