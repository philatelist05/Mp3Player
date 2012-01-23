package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomAccessIterator<T> extends InfiniteIterator<T> {

	public RandomAccessIterator(List<T> list) {
		super(list);
		this._nextIndex = calculateRandomIndex();
	}

	public RandomAccessIterator(RandomAccessIterator<T> iterator) {
		super(iterator);
	}

	@Override
	public T next() {
		if (hasNext()) {
			T nextElement = this.list.get(this._nextIndex);
			this._prevIndex = this._nextIndex;
			this._nextIndex = calculateRandomIndex();
			return nextElement;
		}
		throw new NoSuchElementException("No next element available");
	}

	@Override
	public T previous() {
		if (hasPrevious()) {
			T prevElement = this.list.get(this._prevIndex);
			this._nextIndex = this._prevIndex;
			this._prevIndex = calculateRandomIndex();
			return prevElement;
		}
		throw new NoSuchElementException("No previous element available");
	}

	private int calculateRandomIndex() {
		Random random = new Random();
		return random.nextInt(this.list.size());
	}

}
