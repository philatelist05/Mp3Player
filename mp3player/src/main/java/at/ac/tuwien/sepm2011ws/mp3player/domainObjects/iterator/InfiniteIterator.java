package at.ac.tuwien.sepm2011ws.mp3player.domainObjects.iterator;

import java.util.List;
import java.util.NoSuchElementException;

public class InfiniteIterator<T> extends NormalIterator<T> {

	public InfiniteIterator(List<T> list) {
		super(list);
	}

	public InfiniteIterator(NormalIterator<T> iterator) {
		super(iterator);
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

	private int calculateNextIndex() {
		if (this._nextIndex >= this.list.size())
			this._nextIndex = 0;
		else
			this._nextIndex++;

		return this._nextIndex;
	}

	private int calculatePrevRandomIndex() {
		if (this._prevIndex < 0)
			this._prevIndex = this.list.size() - 1;
		else
			this._prevIndex--;

		return this._prevIndex;
	}
}
