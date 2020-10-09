package h2o.common.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListBuilder<E> {
	
	private List<E> l;
	
	public ListBuilder() {
		this.l = new ArrayList<E>();
	}
	
	public ListBuilder( int i ) {
		this.l = new ArrayList<E>(i);
	}
	
	public ListBuilder( List<E> l ) {
		this.l = l;
	}
	
	public static <F> ListBuilder<F> start() {
		return new ListBuilder<F>();
	}
	
	public static <F> ListBuilder<F> start( int i ) {
		return new ListBuilder<F>(i);
	}
	
	public static <F> ListBuilder<F> start( List<F> l ) {
		return new ListBuilder<F>(l);
	}
	
	
	public static <F> List<F> newEmptyList() {
		return new ArrayList<F>(0);
	}
	
	public static <F> List<F> newList() {
		return new ArrayList<F>();
	}

	public static <F> List<F> newList( int i ) {
		return new ArrayList<F>(i);
	}
	
	public static <F> List<F> newList( F... es ) {
		return new ListBuilder<F>(es.length).add(es).get();
	}
	
	public static <F> List<F> newListAndAddAll( Collection<? extends F> c ) {
		return new ListBuilder<F>( c == null ? 0 :c.size() ).addAll(c).get();
	}

    public static <F> List<F> newListAndFillFromIterable( Iterable<? extends F> it ) {

	    if ( it instanceof Collection ) {
            return newListAndAddAll( ( Collection<? extends F> )it );
        }
        return fillFromIterable( new ArrayList<F>() , it );

    }
	
	public static <F> CopyOnWriteArrayList<F> newCopyOnWriteArrayList() {
		return new CopyOnWriteArrayList<F>();
	}
	
	public static <F> CopyOnWriteArrayList<F> newCopyOnWriteArrayList( F... es ) {
		return (CopyOnWriteArrayList<F>) new ListBuilder<F>( new CopyOnWriteArrayList<F>() ).add(es).get();
	}

	public static <F> List<F> newCopyOnWriteArrayListAndAddAll( Collection<? extends F> c ) {
        return new ListBuilder<F>( new CopyOnWriteArrayList<F>() ).addAll(c).get();
    }

    public static <F> List<F> newCopyOnWriteArrayListAndFillFromIterable( Iterable<? extends F> it ) {

        if ( it instanceof Collection ) {
            return newCopyOnWriteArrayListAndAddAll( ( Collection<? extends F> )it );
        }

        return fillFromIterable( new CopyOnWriteArrayList<F>() , it );

    }


    private static <F> List<F> fillFromIterable( List<F> list , Iterable<? extends F> it ) {

        if ( it != null ) {
            Iterator<? extends F> ir = it.iterator();
            while ( ir.hasNext() ) {
                list.add( ir.next() );
            }
        }

        return list;
    }


	
	@SuppressWarnings("unchecked")
	public <F> List<F> cget() {
		return (List<F>) this.l;
	}
	
	public List<E> get() {
		return  this.l;
	}

	public ListBuilder<E> add(E... es) {
		for( E e : es ) {
			l.add(e);
		}
		return this;
	}

	public ListBuilder<E> add(int index, E element) {
		l.add(index, element);
		return this;
	}

	public ListBuilder<E> addAll(Collection<? extends E> c) {
		if( c != null ) l.addAll(c);
		return this;
	}

	public ListBuilder<E> addAll(int index, Collection<? extends E> c) {
		if( c != null ) l.addAll(index, c);
		return this;
	}

	public ListBuilder<E> clear() {
		l.clear();
		return this;
	}

	public boolean contains(Object o) {
		return l.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return l.containsAll(c);
	}

	public boolean isEmpty() {
		return l.isEmpty();
	}

	public Iterator<E> iterator() {
		return l.iterator();
	}

	public ListBuilder<E> remove(int index) {
		l.remove(index);
		return this;
	}

	public ListBuilder<E> remove(Object o) {
		l.remove(o);
		return this;
	}

	public ListBuilder<E> removeAll(Collection<?> c) {
		l.removeAll(c);
		return this;
	}

	public ListBuilder<E> retainAll(Collection<?> c) {
		l.retainAll(c);
		return this;
	}

	public ListBuilder<E> set(int index, E element) {
		l.set(index, element);
		return this;
	}

	public int size() {
		return l.size();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return l.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return l.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return l.toArray(a);
	}	


}
