class HeapImpl<T extends Comparable<? super T>> implements Heap<T> {
	private static final int INITIAL_CAPACITY = 128;
	private T[] _storage;
	private int _numElements;

	@SuppressWarnings("unchecked")
	public HeapImpl () {
		_storage = (T[]) new Comparable[INITIAL_CAPACITY];
		_numElements = 0;
	}

	@SuppressWarnings("unchecked")
	public void add (T data) {
		// TODO: implement me
		// handle resizing of array for more events being added
		if (this.size() == this._storage.length) {
			T[] temp = (T[]) new Comparable[this.size() * 2];
			for (int i = 0; i < this.size(); i++) {
				temp[i] = this._storage[i];
			}
			this._storage = temp;
		}

		// insert first element into list
		if (this.size() == 0) {
			this._storage[this._numElements++] = data;
			return;
		}

		this._storage[_numElements++] = data;
		this.swim(this._numElements - 1);
	}

	/**
	 * return the parent node's index within the heap from a given node
	 * @param i the index of the current node in the heap
	 * @return the index of the parent node of a certain node
	 */
	private int parent(int i) {
		if (i == 0) return 0;
		return (i - 1) / 2;
	}

	/**
	 * after inserting an element into the bottom of the heap, this method "swims" the element up to its proper place
	 * in the heap based on the value of the element with respect to its parent
	 * @param pos the index of the element being examined to bubble up the heap
	 */
	private void swim(int pos) {
		// while element is smaller than its parent, swap it with its parent and continue to swim up the heap;

		// if already at root, return;
		if (this.parent(pos) == pos) {
			return;
		}

		T t = this._storage[pos];
		if (t.compareTo(this._storage[this.parent(pos)]) > 0) {
			this._storage[pos] = this._storage[this.parent(pos)];
			this._storage[this.parent(pos)] = t;
			swim(this.parent(pos));
		}
	}

	public T removeFirst () {
		// TODO: implement me

		// store the root element in an instance variable then move the last element to the root and sink it down;
		// return the element that is stored in the instance variable
		if (this.size() == 0) return null;
		T t = this._storage[0];
		this._storage[0] = this._storage[--this._numElements];
		//this._storage[--this._numElements] = null;
		this.sink(0);
		return t;
	}

	/**
	 * return the left child node's index within a heap from a given node
	 * @param i the index of the current node in the heap
	 * @return the index of the node's left child in the heap
	 */
	private int leftChild(int i) {
		return i * 2 + 1;
	}

	/**
	 * return the right child node's index within a heap from a given node
	 * @param i the index of the current node in the heap
	 * @return the index of the node's left child in the heap
	 */
	private int rightChild(int i) {
		return i * 2 + 2;
	}

	/**
	 * after removing an element from the root of the heap, "sink" the new element down to its respective position
	 * @param pos the index of the element being examined to trickle down the heap
	 */
	private void sink(int pos) {
		// while element is larger than one of its children, swap element with the smaller child;

		int left = this.leftChild(pos);
		int right = this.rightChild(pos);
		int largest = pos;

		if (left < this._numElements && this._storage[left].compareTo(this._storage[largest]) > 0) {
			largest = left;
		}

		if (right < this._numElements && this._storage[right].compareTo(this._storage[largest]) > 0) {
			largest = right;
		}

		if (largest != pos) {
			T temp = this._storage[pos];
			this._storage[pos] = this._storage[largest];
			this._storage[largest] = temp;
			sink(largest);
		}

	}

	public int size () {
		return _numElements;
	}
}
