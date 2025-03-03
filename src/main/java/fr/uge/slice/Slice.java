package fr.uge.slice;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Slice<E> {
    int size();
    E get(int index);

    static <E> Slice<E> of(E[] elements, int from, int to) {
        Objects.checkFromToIndex(from, to, elements.length);
        return new SliceImpl<>(elements, from, to);
    }

    final class SliceImpl<E> implements Slice<E>{
        private final E[] elements;
        private final int from;
        private final int to;

        public SliceImpl(E[] elements, int from, int to) {
            this.elements = elements;
            this.from = from;
            this.to = to;
        }

        @Override
        public int size() {
            return to - from;
        }

        @Override
        public E get(int index) {
            Objects.checkIndex(index, size());
            return elements[from + index];
        }

        @Override
        public String toString(){
            return Arrays.stream(elements, from, to)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));
        }
    }
}