package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class NormalIterator<T> implements ListIterator<T> {

	protected int _nextIndex;
	protected int _prevIndex;
	protected List<T> list;

	public NormalIterator(List<T> list) {
		this.list = list;
		this._prevIndex = -1;
		this._nextIndex = 0;
	}
	public NormalIterator(NormalIterator<T> iterator) {
		this._nextIndex=iterator._nextIndex;
		this._prevIndex=iterator._prevIndex;
		this.list =iterator.list;
	}

	@Override
	public void add(T arg) {
		throw new UnsupportedOperationException(
				"This Operation is currently not supported!");
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException(
				"This Operation is currently not supported!");
	}

	@Override
	public void set(T arg) {
		throw new UnsupportedOperationException(
				"This Operation is currently not supported!");
	}

	@Override
	public boolean hasNext() {
		return this._nextIndex >= 0 && this._nextIndex < this.list.size();
	}

	@Override
	public boolean hasPrevious() {
		return this._prevIndex >= 0 && this._prevIndex < this.list.size();
	}

	@Override
	public T next() {
		if (hasNext()) {
			T nextElement = this.list.get(this._nextIndex);
			this._prevIndex = this._nextIndex++;
			this._nextIndex = calculateNextIndex();
			return nextElement;
		}
		throw new NoSuchElementException("No next element available");
	}

	@Override
	public T previous() {
		if (hasPrevious()) {
			T prevElement = this.list.get(this._prevIndex);
			this._nextIndex = this._prevIndex;
			this._prevIndex = calculatePrevRandomIndex();
			return prevElement;
		}
		throw new NoSuchElementException("No previous element available");
	}

	@Override
	public int nextIndex() {
		return this._nextIndex;
	}

	@Override
	public int previousIndex() {
		return this._prevIndex;
	}

	private int calculateNextIndex() {
		return ++this._nextIndex;
	}

	private int calculatePrevRandomIndex() {
		return --this._prevIndex;
	}

}