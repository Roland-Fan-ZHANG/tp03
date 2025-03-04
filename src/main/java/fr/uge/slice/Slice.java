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

    Slice<E> subSlice(int from, int to);

    default Slice<E> reversed(){
        return new Slice<E>() {
            @Override
            public int size() {
                return Slice.this.size();
            }

            @Override
            public E get(int index) {
                Objects.checkIndex(index, size());
                return Slice.this.get(size() - 1 - index);
            }

            @Override
            public Slice<E> subSlice(int from, int to) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String toString(){
                return reversed().toString();
            }

            @Override
            public Slice<E> reversed() {
                return Slice.this;
            }
        };
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

        @Override
        public Slice<E> subSlice(int fromOffset, int toOffset) {
            Objects.checkFromToIndex(fromOffset, toOffset, size());
            return new SliceImpl<>(elements, this.from + fromOffset, this.from + toOffset);
        }
    }
}