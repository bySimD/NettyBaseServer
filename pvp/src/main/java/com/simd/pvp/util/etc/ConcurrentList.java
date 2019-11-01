package com.simd.pvp.util.etc;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentList<T>  implements List<T> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private final List<T> list;

    public ConcurrentList(List<T> list) {
        this.list = list;
    }

    @Override
    public int size() {
        readLock.lock();
        try {
            return list.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    public boolean contains(Object o) {
        readLock.lock();
        try {
            for (Object item : list){
                if (item.equals(o))
                    return true;
            }
        } finally {
            readLock.unlock();
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        readLock.lock();
        try
        {
            return new ArrayList<T>(list).iterator();
        }
        finally
        {
            readLock.unlock();
        }
    }

    @Override
    public Object[] toArray() {

        readLock.lock();
        try
        {
            return list.toArray();
        }
        finally
        {
            readLock.unlock();
        }
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        readLock.lock();
        try
        {
            return list.toArray(a);
        }
        finally
        {
            readLock.unlock();
        }
    }

    @Override
    public boolean add(T t) {
        readLock.lock();
        try {
            return list.add(t);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        writeLock.lock();
        try {
            return list.remove(o);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        readLock.lock();
        try {
            return list.containsAll(c);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        writeLock.lock();
        try {
            return list.addAll(c);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        writeLock.lock();
        try {
            return list.addAll(index, c);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        writeLock.lock();
        try {
            return list.removeAll(c);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        writeLock.lock();
        try {
            return list.retainAll(c);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            list.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public T get(int index) {
        readLock.lock();
        try {
            return list.get(index);
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public T set(int index, T element) {
        writeLock.lock();
        try {
            return list.set(index, element);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void add(int index, T element) {
        writeLock.lock();
        try {
            list.add(index, element);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public T remove(int index) {
        writeLock.lock();
        try {
            return list.remove(index);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        readLock.lock();
        try {
            return list.indexOf(o);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        readLock.lock();
        try {
            return list.lastIndexOf(o);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        readLock.lock();
        try
        {
            return list.listIterator();
        }
        finally
        {
            readLock.unlock();
        }
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        readLock.lock();
        try
        {
            return list.listIterator(index);
        }
        finally
        {
            readLock.unlock();
        }
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        readLock.lock();
        try
        {
            return list.subList(fromIndex, toIndex);
        }
        finally
        {
            readLock.unlock();
        }
    }

}
