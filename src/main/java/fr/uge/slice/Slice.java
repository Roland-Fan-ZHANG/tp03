package fr.uge.slice;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public interface Slice<E> {
    int size();
    E get(int index);

    static <E> Slice<E> of(E[] elements, int from, int to) {
        Objects.checkFromToIndex(from, to, elements.length);
        return new SliceImpl<>(elements, from, to);
    }

    Slice<E> subSlice(int from, int to);

    void replaceAll(UnaryOperator<E> operator);

    default Slice<E> reversed(){
        return new Slice<>() {
            @Override
            public int size() {
                return reversed().size();
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
            public void replaceAll(UnaryOperator<E> replacer) {
                Objects.requireNonNull(replacer);
                Slice.this.replaceAll(replacer);
            }

            @Override
            public String toString(){
                return reversed().toString();
            }

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

        @Override
        public void replaceAll(UnaryOperator<E> replacer) {
            Objects.requireNonNull(replacer);
            for (var i = from; i < to; i++) {
                elements[i] = replacer.apply(elements[i]);
            }
        }
    }
}