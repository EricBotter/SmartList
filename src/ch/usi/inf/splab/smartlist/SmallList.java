package ch.usi.inf.splab.smartlist;

import java.util.*;

public class SmallList<E> extends ArrayList<E> {

	private static final long serialVersionUID = -7869404750138322883L;

	private static final class Wrapper<T> {
        T value;

        Wrapper(T t) {
            value = t;
        }

        @Override
        public boolean equals(Object o) {
            if (value == null)
                return o == null;
            return value.equals(o);
        }
    }

    private final static class LongElement {}

    private Object element;

    public SmallList() {
        super(0);
        element = null;
    }

    public SmallList(int capacity) {
        super(0);
        if (capacity > 1) {
            element = new LongElement();
            super.ensureCapacity(capacity);
        }
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        forceToArrayList();
        super.ensureCapacity(minCapacity);
    }

    public SmallList(Collection<? extends E> c) {
        super(c);
        element = new LongElement();
    }

    private void forceToArrayList() {
        if (element == null)
            element = new LongElement();
        if (element instanceof Wrapper) {
            E temp = ((Wrapper<E>) element).value;
            super.add(temp);
            element = new LongElement();
        }
    }
    
    public void trimToSize(){
    	if (element instanceof LongElement)
    		super.trimToSize();
    }

    @SuppressWarnings("rawtypes")
	@Override
    public int size() {
        if (element instanceof Wrapper)
            return 1;
        if (element instanceof LongElement)
            return super.size();
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return element == null;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public boolean contains(Object o) {
        if (element instanceof Wrapper)
            return element.equals(o);
        return element instanceof LongElement && super.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        forceToArrayList();
        return super.iterator();
    }

    @Override
    public Object[] toArray() {
        forceToArrayList();
        return super.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        forceToArrayList();
        return super.toArray(a);
    }

    @Override
    public boolean add(E e) {
        if (element == null) {
            element = new Wrapper<>(e);
            return true;
        } else {
            forceToArrayList();
            return super.add(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        if (element instanceof Wrapper) {
            if (element.equals(o)) {
                element = null;
                return true;
            }
            return false;
        }
        return element != null && super.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        forceToArrayList();
        return super.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        forceToArrayList();
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        forceToArrayList();
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        forceToArrayList();
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        forceToArrayList();
        return super.retainAll(c);
    }

    @Override
    public void clear() {
        element = null;
    }

    @Override
    public E get(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        if (element instanceof Wrapper) {
            if (index > 0)
                throw new IndexOutOfBoundsException();
            return ((Wrapper<E>) element).value;
        }
        if (element == null)
            throw new IndexOutOfBoundsException();
        return super.get(index);
    }

    @Override
    public E set(int index, E e) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        if (element instanceof Wrapper) {
            if (index > 0)
                throw new IndexOutOfBoundsException();
            E value = ((Wrapper<E>) element).value;
            ((Wrapper<E>) element).value = e;
            return value;
        }
        if (element == null)
            throw new IndexOutOfBoundsException();
        return super.set(index, e);
    }

    @Override
    public void add(int index, E e) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        if (element == null) {
            if (index > 0)
                throw new IndexOutOfBoundsException();
            element = new Wrapper<E>(e);
        }
        forceToArrayList();
        super.add(index, e);
    }

    @Override
    public E remove(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        if (element instanceof Wrapper) {
            if (index > 0)
                throw new IndexOutOfBoundsException();
            E out = ((Wrapper<E>) element).value;
            element = null;
            return out;
        }
        if (element == null)
            throw new IndexOutOfBoundsException();
        return super.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        if (element instanceof Wrapper) {
            if (element.equals(o))
                return 0;
            return -1;
        }
        if (element == null)
            return -1;
        return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        forceToArrayList();
        return super.lastIndexOf(o);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        forceToArrayList();
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public ListIterator<E> listIterator() {
        forceToArrayList();
        return super.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        forceToArrayList();
        return super.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        forceToArrayList();
        return super.subList(fromIndex, toIndex);
    }
}
