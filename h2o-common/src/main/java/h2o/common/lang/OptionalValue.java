package h2o.common.lang;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface OptionalValue<T> {

    T getValue();

    default boolean isPresent() {
        return this.getValue() != null;
    }

    default void ifPresent(Consumer<? super T> consumer) {
        if (this.isPresent()) {
            consumer.accept(this.getValue());
        }
    }

    default T get() {
        if (this.isPresent()) {
            return this.getValue();
        } else {
            throw new NoSuchElementException("No value present");
        }
    }

    default T orElse(T other) {
        return this.isPresent() ? this.getValue() : other;
    }

    default T orElseGet(Supplier<? extends T> other) {
        return this.isPresent() ? this.getValue() : other.get();
    }

    default <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isPresent()) {
            return this.getValue();
        } else {
            throw exceptionSupplier.get();
        }
    }


}
