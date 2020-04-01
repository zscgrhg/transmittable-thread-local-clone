package com.alibaba.ttl;

import com.alibaba.ttl.spi.TtlEnhanced;
import com.alibaba.ttl.spi.TtlWrapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.function.*;

import static com.alibaba.ttl.TransmittableThreadLocal.Transmitter.*;

/**
 * Util methods for TTL Wrapper: wrap common {@code Functional Interface}.
 * <p>
 * <b><i>Note:</i></b>
 * <ul>
 * <li>all methods is {@code null}-safe, when input parameter is {@code null}, return {@code null}.</li>
 * <li>all wrap method skip wrap (aka. just return input parameter), when input parameter is already wrapped.</li>
 * </ul>
 *
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @see TtlRunnable
 * @see TtlCallable
 * @see TtlUnwrap
 * @see TtlWrapper
 * @since 2.11.4
 */
public class TtlWrappers {
    /**
     * wrap input {@link Supplier} to TTL wrapper.
     *
     * @param supplier input {@link Supplier}
     * @return Wrapped {@link Supplier}
     * @see TtlUnwrap#unwrap(Object)
     * @since 2.11.4
     */
    @Nullable
    public static <T> Supplier<T> wrap(@Nullable Supplier<T> supplier) {
        if (supplier == null) {
            return null;
        } else if (supplier instanceof TtlEnhanced) {
            return supplier;
        } else {
            return new TtlSupplier<T>(supplier);
        }
    }

    private static class TtlSupplier<T> implements Supplier<T>, TtlWrapper<Supplier<T>>, TtlEnhanced {
        final Supplier<T> supplier;
        final Object capture;

        TtlSupplier(@NonNull Supplier<T> supplier) {
            this.supplier = supplier;
            this.capture = capture();
        }

        @Override
        public T get() {
            final Object backup = replay(capture);
            try {
                return supplier.get();
            } finally {
                restore(backup);
            }
        }

        @NonNull
        @Override
        public Supplier<T> unwrap() {
            return supplier;
        }
    }

    /**
     * wrap input {@link Consumer} to TTL wrapper.
     *
     * @param consumer input {@link Consumer}
     * @return Wrapped {@link Consumer}
     * @see TtlUnwrap#unwrap(Object)
     * @since 2.11.4
     */
    @Nullable
    public static <T> Consumer<T> wrap(@Nullable Consumer<T> consumer) {
        if (consumer == null) {
            return null;
        } else if (consumer instanceof TtlEnhanced) {
            return consumer;
        } else {
            return new TtlConsumer<T>(consumer);
        }
    }

    private static class TtlConsumer<T> implements Consumer<T>, TtlWrapper<Consumer<T>>, TtlEnhanced {
        final Consumer<T> consumer;
        final Object capture;

        TtlConsumer(@NonNull Consumer<T> consumer) {
            this.consumer = consumer;
            this.capture = capture();
        }

        @Override
        public void accept(T t) {
            final Object backup = replay(capture);
            try {
                consumer.accept(t);
            } finally {
                restore(backup);
            }
        }

        @NonNull
        @Override
        public Consumer<T> unwrap() {
            return consumer;
        }
    }


    /**
     * wrap input {@link BiConsumer} to TTL wrapper.
     *
     * @param consumer input {@link BiConsumer}
     * @return Wrapped {@link BiConsumer}
     * @see TtlUnwrap#unwrap(Object)
     * @since 2.11.4
     */
    @Nullable
    public static <T, U> BiConsumer<T, U> wrap(@Nullable BiConsumer<T, U> consumer) {
        if (consumer == null) {
            return null;
        } else if (consumer instanceof TtlEnhanced) {
            return consumer;
        } else {
            return new TtlBiConsumer<T, U>(consumer);
        }
    }

    private static class TtlBiConsumer<T, U> implements BiConsumer<T, U>, TtlWrapper<BiConsumer<T, U>>, TtlEnhanced {
        final BiConsumer<T, U> consumer;
        final Object capture;

        TtlBiConsumer(@NonNull BiConsumer<T, U> consumer) {
            this.consumer = consumer;
            this.capture = capture();
        }

        @Override
        public void accept(T t, U u) {
            final Object backup = replay(capture);
            try {
                consumer.accept(t, u);
            } finally {
                restore(backup);
            }
        }

        @NonNull
        @Override
        public BiConsumer<T, U> unwrap() {
            return consumer;
        }
    }

    /**
     * wrap input {@link Function} to TTL wrapper.
     *
     * @param fn input {@link Function}
     * @return Wrapped {@link Function}
     * @see TtlUnwrap#unwrap(Object)
     * @since 2.11.4
     */
    @Nullable
    public static <T, R> Function<T, R> wrap(@Nullable Function<T, R> fn) {
        if (fn == null) {
            return null;
        } else if (fn instanceof TtlEnhanced) {
            return fn;
        } else {
            return new TtlFunction<T, R>(fn);
        }
    }

    private static class TtlFunction<T, R> implements Function<T, R>, TtlWrapper<Function<T, R>>, TtlEnhanced {
        final Function<T, R> fn;
        final Object capture;

        TtlFunction(@NonNull Function<T, R> fn) {
            this.fn = fn;
            this.capture = capture();
        }

        @Override
        public R apply(T t) {
            final Object backup = replay(capture);
            try {
                return fn.apply(t);
            } finally {
                restore(backup);
            }
        }

        @NonNull
        @Override
        public Function<T, R> unwrap() {
            return fn;
        }
    }


    /**
     * wrap input {@link BiFunction} to TTL wrapper.
     *
     * @param fn input {@link BiFunction}
     * @return Wrapped {@link BiFunction}
     * @see TtlUnwrap#unwrap(Object)
     * @since 2.11.4
     */
    @Nullable
    public static <T, U, R> BiFunction<T, U, R> wrap(@Nullable BiFunction<T, U, R> fn) {
        if (fn == null) {
            return null;
        } else if (fn instanceof TtlEnhanced) {
            return fn;
        } else {
            return new TtlBiFunction<T, U, R>(fn);
        }
    }

    private static class TtlBiFunction<T, U, R> implements BiFunction<T, U, R>, TtlWrapper<BiFunction<T, U, R>>, TtlEnhanced {
        final BiFunction<T, U, R> fn;
        final Object capture;

        TtlBiFunction(@NonNull BiFunction<T, U, R> fn) {
            this.fn = fn;
            this.capture = capture();
        }

        @Override
        public R apply(T t, U u) {
            final Object backup = replay(capture);
            try {
                return fn.apply(t, u);
            } finally {
                restore(backup);
            }
        }

        @NonNull
        @Override
        public BiFunction<T, U, R> unwrap() {
            return fn;
        }
    }

    private TtlWrappers() {
        throw new InstantiationError("Must not instantiate this class");
    }
}
